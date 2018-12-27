/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * hook message
 */
public class SMSHook implements HookBase{

    public static SMSHook getInstance() {
        return new SMSHook();
    }

    public void startHook (final XC_LoadPackage.LoadPackageParam lpparm,
                           ApiCallCtrl ctrl,
                           ApiCallReturnValue returnValue) throws Throwable{

        final String packageName = lpparm.packageName;


        Class<?> pendingIntentClass = XposedHelpers.findClass("android.app.PendingIntent", lpparm.classLoader);

        final Boolean logOnTextMessage = ctrl.getInformPolicy(packageName,
                "android.telephony.SmsManager.sendTextMessage", String.class, String.class,
                String.class, pendingIntentClass, pendingIntentClass);

        switch (ctrl.getPolicy(packageName, "android.telephony.SmsManager.sendTextMessage",
                String.class, String.class, String.class, pendingIntentClass, pendingIntentClass)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("android.telephony.SmsManager",
                        lpparm.classLoader, "sendTextMessage",
                        String.class, String.class, String.class, pendingIntentClass, pendingIntentClass, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        /**
                         * get destination address, source address, and text to be sent from parameters
                         */
                        String destAddress = (String) param.args[0];
                        String sourceAddress = (String) param.args[1];
                        String textToBeSent = (String) param.args[2];

                        if (logOnTextMessage) {
                            XposedBridge.log(packageName + " android.telephony.SmsManager.sendTextMessage \n" +
                                    "Destination Address:" + destAddress + "\nSource Address:" +
                                    sourceAddress + "\n Content:\n" + textToBeSent + "\n" + "Allowed");


                        }
                    }
                });
                break;

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookMethod("android.telephony.SmsManager",
                        lpparm.classLoader, "sendTextMessage",
                        String.class, String.class, String.class, pendingIntentClass, pendingIntentClass, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String destAddress = (String) param.args[0];
                        String sourceAddress = (String) param.args[1];
                        String textToBeSent = (String) param.args[2];

                        if (logOnTextMessage) {
                            XposedBridge.log(packageName + " android.telephony.SmsManager.sendTextMessage \n" +
                                "Destination Address:" + destAddress + "\nSource Address:" +
                                sourceAddress + "\n Content:\n" + textToBeSent +"\n" + "Blocked");

                        }

                        /** stop the original method from being called */

                        param.setResult(null);

                    }
                });
                break;

        }



        final Boolean logOnDataMessage = ctrl.getInformPolicy(packageName,
                "android.telephony.SmsManager.sendDataMessage", String.class, String.class,
                short.class, byte[].class, pendingIntentClass, pendingIntentClass);

        switch (ctrl.getPolicy(packageName, "android.telephony.SmsManager.sendDataMessage",
                String.class, String.class, short.class, byte[].class, pendingIntentClass, pendingIntentClass)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("android.telephony.SmsManager",
                        lpparm.classLoader, "sendDataMessage",
                        String.class, String.class, short.class, byte[].class, pendingIntentClass, pendingIntentClass, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        String destAddress = (String) param.args[0];
                        String sourceAddress = (String) param.args[1];
                        String textToBeSent = (String) param.args[2];

                        if (logOnDataMessage) {
                            XposedBridge.log(packageName + " android.telephony.SmsManager.sendDataMessage \n" +
                                "Destination Address:" + destAddress + "\nSource Address:" +
                                sourceAddress + "\n Content:\n" + textToBeSent +"\n" + "Allowed");
                        }
                    }
                });
                break;

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:
                XposedHelpers.findAndHookMethod("android.telephony.SmsManager",
                        lpparm.classLoader, "sendDataMessage",
                        String.class, String.class, short.class, byte[].class, pendingIntentClass, pendingIntentClass, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String destAddress = (String) param.args[0];
                        String sourceAddress = (String) param.args[1];
                        short destPort = (short) param.args[2];
                        byte[] text = (byte[]) param.args[3];
                        String destPortString = String.valueOf((int) destPort);

                        /**
                         * Just log the hashcode of the content since we don't know the
                         *  particular encode standard used by the app created it
                         */

                        if (logOnDataMessage) {
                            XposedBridge.log(packageName + " android.telephony.SmsManager.sendDataMessage \n" +
                                    "Destination Address:" + destAddress + "\nDestination Port:" +
                                    destPortString + "\nSource Address:" + sourceAddress + "\n Content:\n" +
                                    text.toString() + "\n" + "Blocked");
                        }
                        param.setResult(null);
                    }
                });
                break;

        }

    }

}
