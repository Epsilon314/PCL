/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl;

import com.zhuyiqing.pcl.ApiHooks.NetHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public ApiCallLog apiCallLog = new ApiCallLog();
    public ApiCallCtrl apiCallCtrl = new ApiCallCtrl();
    public ForgedReturnValues forgedReturnValues = new ForgedReturnValues();

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("PCL init");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //XposedBridge.log("PCL handleLoadPackage" + lpparam.packageName);

        if (lpparam.packageName.equals("android")) {
            return;
        }

        if (lpparam.packageName.startsWith(BuildConfig.APPLICATION_ID)) {
            return;
        }

        hookUserApplication(lpparam);

    }

    private void hookUserApplication(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        NetHook.hookAllNetworkApi(loadPackageParam, apiCallLog, apiCallCtrl, forgedReturnValues);
    }


}
