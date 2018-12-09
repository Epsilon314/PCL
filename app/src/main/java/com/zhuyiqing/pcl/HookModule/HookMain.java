/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;


import com.zhuyiqing.pcl.ApiHooks.NetHook;
import com.zhuyiqing.pcl.BuildConfig;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public ApiCallCtrl apiCallCtrl = new ApiCallCtrl();
    public ApiCallReturnValue apiCallReturnValue = new ApiCallReturnValue();


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("PCL init");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //XposedBridge.log("PCL handleLoadPackage" + lpparam.packageName);

        if (lpparam.packageName.startsWith("android")) {
            return;
        }

        if (lpparam.packageName.startsWith(BuildConfig.APPLICATION_ID)) {
            return;
        }

        hookUserApplication(lpparam);

    }

    private void hookUserApplication(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        NetHook.getInstance().hookAllNetworkApi(loadPackageParam, apiCallCtrl, apiCallReturnValue);

    }


}
