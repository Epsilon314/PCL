/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;

import java.io.OutputStream;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Hook Http
 * log only
 * Todo: add more ctrl to http data
 */
public class HttpHook implements HookBase{


    public static HttpHook getInstance() {
        return new HttpHook();
    }

    /**
     *
     * @param lpparm load package parameter
     * @param ctrl control class
     * @param returnValue forged value
     */
    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparm, ApiCallCtrl ctrl, ApiCallReturnValue returnValue) {

        final String packageName = lpparm.packageName;

        final Class<?> httpUrlConn = XposedHelpers.findClass("java.net.HttpURLConnection",
                lpparm.classLoader);

        final boolean logOn = ctrl.getInformPolicy(packageName, "java.net.HttpURLConnection");


        XposedBridge.hookAllConstructors(httpUrlConn, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                if (param.args.length != 1 || param.args[0].getClass() != java.net.URL.class) {
                    return;
                }
                if (logOn) {
                    XposedBridge.log(packageName + " HttpUrlConnection:" + param.args[0] + "\n");
                }

            }

        });

        XposedHelpers.findAndHookMethod("java.io.OutputStream", lpparm.classLoader, "write", byte[].class,int.class,int.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                OutputStream os = (OutputStream)param.thisObject;
                if(!os.toString().contains("internal.http")) {
                    return;
                }
                String data = new String((byte[]) param.args[0]);
                if (logOn) {
                    XposedBridge.log("Data:" + data + "\n");
                }
            }
        });


        XposedHelpers.findAndHookMethod("java.io.OutputStream", lpparm.classLoader, "write", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream)param.thisObject;
                if(!os.toString().contains("internal.http")) {
                    return;
                }
                String data = new String((byte[]) param.args[0]);
                if (logOn) {
                    XposedBridge.log("Data: " + data);
                }

            }
        });

    }
}
