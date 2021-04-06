package com.tencent.shadow.dynamic.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;

import com.tencent.shadow.core.common.InstalledApk;
import com.tencent.shadow.core.common.Logger;
import com.tencent.shadow.core.common.LoggerFactory;
import com.tencent.shadow.core.load_parameters.LoadParameters;
import com.tencent.shadow.core.manager.BasePluginManager;
import com.tencent.shadow.core.manager.installplugin.InstalledPlugin;
import com.tencent.shadow.core.manager.installplugin.InstalledType;
import com.tencent.shadow.dynamic.host.FailedException;
import com.tencent.shadow.dynamic.host.NotFoundException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.BIND_AUTO_CREATE;

abstract public class BaseDynamicPluginManager extends BasePluginManager implements UuidManagerImpl {
    private static final Logger mLogger = LoggerFactory.getLogger(BaseDynamicPluginManager.class);

    public BaseDynamicPluginManager(Context context) {
        super(context);
    }

    /**
     * 防止绑定service重入
     */
    private AtomicBoolean mServiceConnecting = new AtomicBoolean(false);
    /**
     * 等待service绑定完成的计数器
     */
    private AtomicReference<CountDownLatch> mConnectCountDownLatch = new AtomicReference<>();

    /**
     * 启动PluginProcessService
     *
     * @param serviceName 注册在宿主中的插件进程管理service完整名字
     */
    public final void bindPluginProcessService(final String serviceName) {
        if (mServiceConnecting.get()) {
            if (mLogger.isInfoEnabled()) {
                mLogger.info("pps service connecting");
            }
            return;
        }
        if (mLogger.isInfoEnabled()) {
            mLogger.info("bindPluginProcessService " + serviceName);
        }

        //设置原子引用类型CountDownLatch, 指定等待线程为1
        mConnectCountDownLatch.set(new CountDownLatch(1));

        //设置为在运行
        mServiceConnecting.set(true);

        //为什么还要 New 一个 CountDownLatch? 因为mConnectCountDownLatch 用来等待bindService 方法的
        //而startBindingLatch是用来等待 bindService 执行完毕的
        final CountDownLatch startBindingLatch = new CountDownLatch(1);
        final boolean[] asyncResult = new boolean[1];

        //加入到主线程Handler消息队列中, 去执行
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {

                //构建 Service 启动的 Intent
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mHostContext, serviceName));

                //
                boolean binding = mHostContext.bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        if (mLogger.isInfoEnabled()) {
                            mLogger.info("onServiceConnected connectCountDownLatch:" + mConnectCountDownLatch);
                        }

                        //设置为 false, 证明不是在连接中, 而是已经连接完成了
                        mServiceConnecting.set(false);

                        // service connect 后处理逻辑
                        onPluginServiceConnected(name, service);

                        //等待结束
                        mConnectCountDownLatch.get().countDown();

                        if (mLogger.isInfoEnabled()) {
                            mLogger.info("onServiceConnected countDown:" + mConnectCountDownLatch);
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        if (mLogger.isInfoEnabled()) {
                            mLogger.info("onServiceDisconnected");
                        }
                        mServiceConnecting.set(false);
                        onPluginServiceDisconnected(name);
                    }
                }, BIND_AUTO_CREATE);

                //复制到asyncResult
                asyncResult[0] = binding;
                //等待结束
                startBindingLatch.countDown();
            }
        });
        try {
            //等待bindService真正开始, 只等待 10
            startBindingLatch.await(10, TimeUnit.SECONDS);
            if (!asyncResult[0]) {
                throw new IllegalArgumentException("无法绑定PPS:" + serviceName);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public final void waitServiceConnected(int timeout, TimeUnit timeUnit) throws TimeoutException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("waitServiceConnected 不能在主线程中调用");
        }
        try {
            if (mLogger.isInfoEnabled()) {
                mLogger.info("waiting service connect connectCountDownLatch:" + mConnectCountDownLatch);
            }
            long s = System.currentTimeMillis();
            boolean isTimeout = !mConnectCountDownLatch.get().await(timeout, timeUnit);
            if (isTimeout) {
                throw new TimeoutException("连接Service超时 ,等待了：" + (System.currentTimeMillis() - s));
            }
            if (mLogger.isInfoEnabled()) {
                mLogger.info("service connected " + (System.currentTimeMillis() - s));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void onPluginServiceConnected(ComponentName name, IBinder service);

    protected abstract void onPluginServiceDisconnected(ComponentName name);

    /**
     * PluginManager对象创建的时候回调
     *
     * @param bundle 当PluginManager有更新时会回调老的PluginManager对象onSaveInstanceState存储数据，bundle不为null说明发生了更新
     *               为null说明是首次创建
     */
    public void onCreate(Bundle bundle) {
        if (mLogger.isInfoEnabled()) {
            mLogger.info("onCreate bundle:" + bundle);
        }
    }

    /**
     * 当PluginManager有更新时会先回调老的PluginManager对象 onSaveInstanceState存储数据
     *
     * @param bundle 要存储的数据
     */
    public void onSaveInstanceState(Bundle bundle) {
        if (mLogger.isInfoEnabled()) {
            mLogger.info("onSaveInstanceState:" + bundle);
        }
    }

    /**
     * 当PluginManager有更新时先会销毁老的PluginManager对象，回调对应的onDestroy
     */
    public void onDestroy() {
        if (mLogger.isInfoEnabled()) {
            mLogger.info("onDestroy:");
        }
    }

    public InstalledApk getPlugin(String uuid, String partKey) throws FailedException, NotFoundException {
        try {
            InstalledPlugin.Part part;
            try {
                part = getPluginPartByPartKey(uuid, partKey);
            } catch (RuntimeException e) {
                throw new NotFoundException("uuid==" + uuid + "partKey==" + partKey + "的Plugin找不到");
            }
            String businessName = part instanceof InstalledPlugin.PluginPart ? ((InstalledPlugin.PluginPart) part).businessName : null;
            String[] dependsOn = part instanceof InstalledPlugin.PluginPart ? ((InstalledPlugin.PluginPart) part).dependsOn : null;
            String[] hostWhiteList = part instanceof InstalledPlugin.PluginPart ? ((InstalledPlugin.PluginPart) part).hostWhiteList : null;
            LoadParameters loadParameters
                    = new LoadParameters(businessName, partKey, dependsOn, hostWhiteList);

            Parcel parcelExtras = Parcel.obtain();
            loadParameters.writeToParcel(parcelExtras, 0);
            byte[] parcelBytes = parcelExtras.marshall();
            parcelExtras.recycle();

            return new InstalledApk(
                    part.pluginFile.getAbsolutePath(),
                    part.oDexDir == null ? null : part.oDexDir.getAbsolutePath(),
                    part.libraryDir == null ? null : part.libraryDir.getAbsolutePath(),
                    parcelBytes
            );
        } catch (RuntimeException e) {
            if (mLogger.isErrorEnabled()) {
                mLogger.error("getPlugin exception:", e);
            }
            throw new FailedException(e);
        }
    }

    private InstalledApk getInstalledPL(String uuid, int type) throws FailedException, NotFoundException {
        try {
            InstalledPlugin.Part part;
            try {
                part = getLoaderOrRunTimePart(uuid, type);
            } catch (RuntimeException e) {
                if (mLogger.isErrorEnabled()) {
                    mLogger.error("getInstalledPL exception:", e);
                }
                throw new NotFoundException("uuid==" + uuid + " type==" + type + "没找到。cause：" + e.getMessage());
            }
            return new InstalledApk(part.pluginFile.getAbsolutePath(),
                    part.oDexDir == null ? null : part.oDexDir.getAbsolutePath(),
                    part.libraryDir == null ? null : part.libraryDir.getAbsolutePath());
        } catch (RuntimeException e) {
            throw new FailedException(e);
        }
    }

    public InstalledApk getPluginLoader(String uuid) throws FailedException, NotFoundException {
        return getInstalledPL(uuid, InstalledType.TYPE_PLUGIN_LOADER);
    }

    public InstalledApk getRuntime(String uuid) throws FailedException, NotFoundException {
        return getInstalledPL(uuid, InstalledType.TYPE_PLUGIN_RUNTIME);
    }
}
