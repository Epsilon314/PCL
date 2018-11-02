/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.ApiHooks;

import android.net.NetworkInfo;

import com.zhuyiqing.pcl.ApiCallCtrl;
import com.zhuyiqing.pcl.ApiCallLog;
import com.zhuyiqing.pcl.ForgedReturnValues;
import com.zhuyiqing.pcl.ApiCallCtrl.ApiCallCtrlPolicy;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NetHook {

    public static void hookAllNetworkApi (XC_LoadPackage.LoadPackageParam loadPackageParam,
                                          ApiCallLog apiCallLog, ApiCallCtrl apiCallCtrl,
                                          ForgedReturnValues forgedReturnValues) throws Throwable{

        //XposedBridge.log("Network");
        hookAndroidNetNetworkInfo(loadPackageParam, apiCallLog, apiCallCtrl, forgedReturnValues);
        apiCallLog.printAllToXposedLog();
        apiCallLog.clearAll();
    }

    private static void hookAndroidNetNetworkInfo (final XC_LoadPackage.LoadPackageParam loadPackageParam,
                                                   final ApiCallLog apiCallLog,
                                                   final ApiCallCtrl apiCallCtrl,
                                                   final ForgedReturnValues forgedReturnValues) throws Throwable {

        final Class<?> clazz = XposedHelpers.findClass("android.net.NetworkInfo", loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(clazz, "getDetailedState", new XC_MethodHook() {

            @Override
            protected void  beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                switch (apiCallCtrl.getPolicy(loadPackageParam.packageName, "getDetailedState")) {
                    case ApiCallCtrlPolicy.BLOCK:
                        //Todo check why setResult causes error

                        //param.setResult(null);
                        apiCallLog.addRecord(loadPackageParam.packageName,
                                clazz.getName()+".getDetailedState", "Blocked");
                        break;
                    case ApiCallCtrlPolicy.ALLOW:
                        apiCallLog.addRecord(loadPackageParam.packageName,
                                clazz.getName()+".getDetailedState", "Allowed");
                        break;
                    case ApiCallCtrlPolicy.FORGE:
                        Object result = forgedReturnValues.getForgedValue(clazz.getName(), "getDetailedState");
                        //if (result!=null) param.setResult(result);
                        String resultStr = (null==result) ? "null" : result.toString();
                        apiCallLog.addRecord(loadPackageParam.packageName, clazz.getName()+".getDetailedState", "Forged to " + resultStr);
                        break;
                }

                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod(clazz, "getExtraInfo", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                switch (apiCallCtrl.getPolicy(loadPackageParam.packageName, "getExtraInfo")) {
                    case ApiCallCtrlPolicy.BLOCK:
                        //param.setResult(null);
                        apiCallLog.addRecord(loadPackageParam.packageName,
                                clazz.getName()+".getExtraInfo", "Blocked");
                        break;
                    case ApiCallCtrlPolicy.ALLOW:
                        apiCallLog.addRecord(loadPackageParam.packageName,
                                clazz.getName()+".getExtraInfo", "Allowed");
                        break;
                    case ApiCallCtrlPolicy.FORGE:
                        break;

                }

                super.afterHookedMethod(param);
            }
        });



    }
}
