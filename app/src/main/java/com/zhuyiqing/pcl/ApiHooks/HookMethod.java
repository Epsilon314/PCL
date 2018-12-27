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

/**
 * simple hook pattern method
 */
public class HookMethod {



    /**
     *
     * @param className class to be hooked
     * @param classLoader
     * @param methodName method to be hooked
     * @param parameterType one parameter, null is does't has one
     * @param returnValue forged return value used after calling the original method
     * @param earlyReturnValue forged return value used when you don't what to call the original method
     * @param packageName package who call the hooked method
     * @param logResult extra information to be logged
     * @param logEnable if log is on
     */
    private static void hookMethod(final String  className, ClassLoader classLoader,
                                   final String  methodName, Class parameterType,
                                   final Object returnValue, final Object earlyReturnValue,
                                   final String packageName, final String logResult,
                                   final boolean logEnable) {
        if (null == parameterType) {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, new XC_MethodHook() {

                @Override
                protected void  beforeHookedMethod(MethodHookParam param) throws Throwable {

                    if (logEnable) {
                        XposedBridge.log(packageName + " " + className + "." + methodName + " " +
                                logResult);
                    }

                    /**
                     * setting a result in beforeHookedMethod will set the early return flag and the
                     * original method will not be called
                     */
                    if (null != earlyReturnValue) {
                        param.setResult(earlyReturnValue);
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        if (null != returnValue) {
                            param.setResult(returnValue);
                        }
                    } catch (Throwable t) {
                        param.setThrowable(t);
                    }
                }
            });
        }
        else {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterType, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    if (logEnable) {
                        XposedBridge.log(packageName + " " + className + "." + methodName + " " +
                                logResult);
                    }

                    if (null != earlyReturnValue) {
                        param.setResult(earlyReturnValue);
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    try {
                        if (null != returnValue) {
                            param.setResult(returnValue);
                        }
                    } catch (Throwable t) {
                        param.setThrowable(t);
                    }
                }
            });
        }
    }

    /**
     *
     * @param apiCallCtrl
     * @param apiCallReturnValue
     * @param packageName
     * @param className
     * @param classLoader
     * @param methodName
     * @param parameterType
     */
    public static void startHook(ApiCallCtrl apiCallCtrl, ApiCallReturnValue apiCallReturnValue,
                                 String packageName, String className, ClassLoader classLoader,
                                 String methodName, Class parameterType) {

        Boolean logEnable = apiCallCtrl.getInformPolicy(packageName,
                className + "." + methodName, parameterType);

        switch (apiCallCtrl.getPolicy(packageName, className + "." + methodName,
                parameterType)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:
                Object returnValueBlock = apiCallReturnValue.getReturnValue(
                        className + "." + methodName,
                        ApiCallReturnValue.ReturnModifyType.BLOCK);

                Object earlyReturnValueBlock = apiCallReturnValue.getReturnValue(
                        className + "." + methodName,
                        ApiCallReturnValue.ReturnModifyType.EARLY_BLOCK);

                HookMethod.hookMethod(className,classLoader, methodName, parameterType,
                        returnValueBlock, earlyReturnValueBlock, packageName, "Blocked", logEnable);

                break;

            case ApiCallCtrl.ApiCallCtrlPolicy.ALLOW:

                HookMethod.hookMethod(className,classLoader, methodName, parameterType,
                        null, null, packageName, "Allowed", logEnable);


                break;

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:

                Object returnValueForge = apiCallReturnValue.getReturnValue(
                        className + "." + methodName,
                        ApiCallReturnValue.ReturnModifyType.FORGE);

                Object earlyReturnValueForge = apiCallReturnValue.getReturnValue(
                        className + "." + methodName,
                        ApiCallReturnValue.ReturnModifyType.EARLY_FORGE);

                HookMethod.hookMethod(className,classLoader, methodName, parameterType,
                        returnValueForge, earlyReturnValueForge, packageName, "Forged", logEnable);


                break;

        }
    }
}
