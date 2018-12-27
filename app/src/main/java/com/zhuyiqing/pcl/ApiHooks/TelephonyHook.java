/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;


import android.content.Intent;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * hook telephony
 * not working in simulator for it does not actually dial... Todo: test on real devices later
 */

public class TelephonyHook implements HookBase{

    public static TelephonyHook getInstance() {
        return new TelephonyHook();
    }

    public void startHook(final XC_LoadPackage.LoadPackageParam lpparm,
                          ApiCallCtrl ctrl,
                          ApiCallReturnValue returnValue) throws Throwable{

        final String packageName = lpparm.packageName;

        String[] targetIntentAction = {Intent.ACTION_CALL, Intent.ACTION_DIAL, Intent.ACTION_NEW_OUTGOING_CALL};

        for (String intentAction : targetIntentAction) {
            if (ctrl.getPolicy(packageName,
                    "android.content.ContextWrapper.startActivity" + intentAction,
                    Intent.class)!=ApiCallCtrl.ApiCallCtrlPolicy.ALLOW) {
                StartActivityHook.hookStartActivity(lpparm.classLoader, Intent.ACTION_DIAL, packageName, true, true);
            }
            StartActivityHook.hookStartActivity(lpparm.classLoader, Intent.ACTION_DIAL, packageName,true,false);
        }


        Class<?> phoneStateListenerClass = XposedHelpers.findClass("android.telephony.PhoneStateListener",
                lpparm.classLoader);

        final Boolean logOn = ctrl.getInformPolicy(packageName, "android.telephony.TelephonyManager.listen",
                phoneStateListenerClass, int.class);

        switch(ctrl.getPolicy(packageName, "android.telephony.TelephonyManager.listen",
                phoneStateListenerClass, int.class)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.ALLOW:
                if (logOn) {
                    XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager",
                            lpparm.classLoader, "listen", phoneStateListenerClass, int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(packageName + " android.telephony.TelephonyManager.listen Allowed\n");
                        }
                    });
                }
                break;
            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager",
                        lpparm.classLoader, "listen", phoneStateListenerClass, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        /**
                         * 0 represents LISTEN_NONE
                         */
                        param.args[1] = 0;

                        if (logOn) {
                            XposedBridge.log(packageName + " android.telephony.TelephonyManager.listen Blocked\n");
                        }
                    }
                });
                break;
        }




    }


    /**
     * Currently deprecated, leave here for possibly reuse
     *
     * @param loadPackageParam
     * @param apiCallCtrl
     * @param apiCallReturnValue
     */
    public void intentHook(final XC_LoadPackage.LoadPackageParam loadPackageParam,
                           ApiCallCtrl apiCallCtrl,
                           ApiCallReturnValue apiCallReturnValue) {

        final String packageName = loadPackageParam.packageName;

        /**
         * manage call through Intent
         * there are two ways to obtain a call-Intent
         * 1. construct Intent() 2. construct a Intent and setAction()
         * so we need to hook them all
         * related intent actions can be CALL, DIAL, NEW_OUTGOING_CALL
         */

        /** hook the Intent(String) constructor and replace it with an empty intent */

        switch (apiCallCtrl.getPolicy(packageName, "android.content.Intent.Intent.ACTION_CALL", String.class)) {
            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:

                /**
                 * for a constructor, MethodHookParam.getResult will always return null
                 * so we should use MethodHookParam.thisObject instead
                 *
                 * it seems that afterHookedMethod has no use for constructors
                 */

                XposedHelpers.findAndHookConstructor("android.content.Intent",
                        loadPackageParam.classLoader, String.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                /** discover which intent the target app used to start the call */

                                //XposedBridge.log(packageName + " android.content.Intent.Intent " + param.args[0] + "\n");

                                if (param.args[0] == Intent.ACTION_CALL) {

                                    XposedBridge.log(packageName + " android.content.Intent.Intent "
                                            + param.args[0] + " Blocked\n");

                                    /**
                                     * will cause exception for the block app
                                     * and the consequences varies from the way they handle exceptions
                                     */

                                    param.args[0] = "";

                                    //Todo parse intent.data to get extra information

                                }
                            }

                        });

                break;

        }


        switch (apiCallCtrl.getPolicy(packageName, "android.content.Intent.Intent.ACTION_NEW_OUTGOING_CALL", String.class)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookConstructor("android.content.Intent",
                        loadPackageParam.classLoader, String.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                if (param.args[0] == Intent.ACTION_NEW_OUTGOING_CALL) {

                                    XposedBridge.log(packageName + " android.content.Intent.Intent "
                                            + param.args[0] + " Blocked\n");

                                    /**
                                     * will cause exception for the block app
                                     * and the consequences varies from the way they handle exceptions
                                     */

                                    param.args[0] = "";

                                }
                            }
                        });
                break;

        }

        switch (apiCallCtrl.getPolicy(packageName, "android.content.Intent.Intent.ACTION_DIAL", String.class)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:
            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookConstructor("android.content.Intent",
                        loadPackageParam.classLoader, String.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                if (param.args[0] == "android.intent.action.DIAL") {

                                    XposedBridge.log(packageName + " android.content.Intent.Intent "
                                            + param.args[0] + " Blocked\n");

                                    /**
                                     * will cause exception for the block app
                                     * and the consequences varies from the way they handle exceptions
                                     */

                                    param.args[0] = "";


                                }
                            }
                        });
                break;

        }
    }
}
