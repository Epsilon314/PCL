/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;


import com.zhuyiqing.pcl.ApiHooks.HttpHook;
import com.zhuyiqing.pcl.ApiHooks.LocationHook;
import com.zhuyiqing.pcl.ApiHooks.NetHook;
import com.zhuyiqing.pcl.ApiHooks.PhoneIDHook;
import com.zhuyiqing.pcl.ApiHooks.SMSHook;
import com.zhuyiqing.pcl.ApiHooks.TelephonyHook;
import com.zhuyiqing.pcl.ApiHooks.WiFiHook;
import com.zhuyiqing.pcl.BuildConfig;
import com.zhuyiqing.pcl.Utils.SettingHelper;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookZygoteInit, IXposedHookLoadPackage{

    public ApiCallCtrl ctrl = new ApiCallCtrl();
    public ApiCallReturnValue returnValue = new ApiCallReturnValue();
    public ApiCallLog log = new ApiCallLog();


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XposedBridge.log("PCL init");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        //XposedBridge.log("PCL handleLoadPackage" + lpparam.packageName);


        /**
         * bypass android system and self
         */
        if (lpparam.packageName.startsWith("android")) {
            return;
        }

        if (lpparam.packageName.startsWith(BuildConfig.APPLICATION_ID)) {
            return;
        }

        SettingHelper.savePackageNames(lpparam.packageName);

        hookUserApplication(lpparam);

    }

    private void hookUserApplication(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        NetHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        WiFiHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        HttpHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        SMSHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        TelephonyHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        LocationHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);
        PhoneIDHook.getInstance().startHook(loadPackageParam, ctrl, returnValue);

    }


}
