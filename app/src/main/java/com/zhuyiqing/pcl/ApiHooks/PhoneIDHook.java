package com.zhuyiqing.pcl.ApiHooks;

import android.content.ContentResolver;
import android.provider.Settings;


import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallCtrl.*;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * hook android id, IMEI
 * those ids can be used as an unique device identifier
 * this hook allow user to config them
 */

public class PhoneIDHook implements HookBase{

    public static PhoneIDHook getInstance() {
        return new PhoneIDHook();
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparm, ApiCallCtrl ctrl, ApiCallReturnValue returnValue) {


        final String packageName = lpparm.packageName;
        final String forgeAndroidID = (String) returnValue.getReturnValue(
                "android.provider.Settings.Secure.getString.ANDROID_ID",
                ApiCallReturnValue.ReturnModifyType.FORGE);

        final Boolean logAndroidID = ctrl.getInformPolicy(packageName,
                "android.provider.Settings.Secure.getString.ANDROID_ID", ContentResolver.class, String.class);
        switch (ctrl.getPolicy(packageName, "android.provider.Settings.Secure.getString.ANDROID_ID",
                ContentResolver.class, String.class)) {
            case ApiCallCtrlPolicy.ALLOW:
                XposedHelpers.findAndHookMethod("android.provider.Settings.Secure",
                        lpparm.classLoader, "getString", ContentResolver.class,
                        String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                            if (logAndroidID) {
                                XposedBridge.log(packageName +
                                        " android.provider.Settings.Secure.getString.ANDROID_ID Allowed");
                            }
                        }
                    }
                });
                break;

            case ApiCallCtrlPolicy.BLOCK:
            case ApiCallCtrlPolicy.FORGE:
                XposedHelpers.findAndHookMethod("android.provider.Settings.Secure",
                        lpparm.classLoader, "getString", ContentResolver.class,
                        String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                            if (logAndroidID) {
                                XposedBridge.log(packageName +
                                        " android.provider.Settings.Secure.getString.ANDROID_ID Forged");
                            }
                            param.setResult(forgeAndroidID);
                        }
                    }
                });
                break;
        }



        String forgeIMEI = (String) returnValue.getReturnValue(
                "android.telephony.TelephonyManager.getDeviceId",
                ApiCallReturnValue.ReturnModifyType.FORGE);
        switch (ctrl.getPolicy(packageName, "android.telephony.TelephonyManager.getDeviceId")) {
            case ApiCallCtrlPolicy.BLOCK:
            case ApiCallCtrlPolicy.FORGE:
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager",
                        lpparm.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(forgeIMEI));

        }

    }


}
