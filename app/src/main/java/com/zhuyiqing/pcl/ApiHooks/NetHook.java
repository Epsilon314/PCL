package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.ApiCallCtrl;
import com.zhuyiqing.pcl.ApiCallLog;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NetHook {


    public static void hookAllNetworkApi (XC_LoadPackage.LoadPackageParam loadPackageParam, ApiCallLog apiCallLog) throws Throwable{
        hookAndroidNetNetworkInfo(loadPackageParam, apiCallLog);
    }

    private static void hookAndroidNetNetworkInfo (final XC_LoadPackage.LoadPackageParam loadPackageParam, final ApiCallLog apiCallLog) throws Throwable {

        Class<?> clazz = XposedHelpers.findClass("android.net.NetworkInfo", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "getDetailedState", new XC_MethodHook() {

            @Override
            protected void  beforeHookedMethod(MethodHookParam param) throws Throwable {

                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                int id = apiCallLog.addRecord(loadPackageParam.packageName, "getDetailedState", ApiCallCtrl.apiCallCtrlPolicy.ALLOW.toString());
                apiCallLog.printToXposedLog(id);
                super.afterHookedMethod(param);
            }
        });
    }
}
