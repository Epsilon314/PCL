/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;

import android.content.Intent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


/**
 * startActivity is an abstract method which can not be hooked, but we can find a proper implement to hook,
 * saying android.content.ContextWrapper.startActivity
 *
 * It is a frequently called method so do be careful when changing it, for it will effect almost all apps
 */
public class StartActivityHook {

    public static void hookStartActivity(ClassLoader classLoader, final String intentAction,
                                         final String packetName, final boolean logOn, final boolean block) {

        final String className = "android.content.ContextWrapper";
        final String methodName = "startActivity";

        XposedHelpers.findAndHookMethod(className, classLoader, methodName, Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                Intent startIntent = (Intent) param.args[0];

                if (null != startIntent && startIntent.getAction() == intentAction) {
                    if (logOn) {
                        XposedBridge.log(packetName + " " + className + "." + methodName + " " + intentAction);
                    }
                    if (block) {
                        XposedBridge.log(" Blocked");
                        param.setResult(null);
                    }
                    XposedBridge.log("\n");
                }
            }
        });

    }




}
