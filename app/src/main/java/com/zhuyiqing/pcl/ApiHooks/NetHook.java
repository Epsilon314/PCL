/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.ApiHooks;



import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallCtrl.*;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Vector;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * hook network related methods
 */

public class NetHook implements HookBase{

    public static NetHook getInstance() {
        return new NetHook();
    }


    public void startHook (final XC_LoadPackage.LoadPackageParam lpparm,
                           ApiCallCtrl ctrl,
                           ApiCallReturnValue returnValue) throws Throwable{



        final String packageName = lpparm.packageName;

        /**
         * ConnectivityManager can be achieved through a system service, and NetworkInfo can be achieved
         * through ConnectivityManager. Hooking them shall affect app using ConnectivityManager to get access
         * to network.
         */

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.NetworkInfo", lpparm.classLoader,
                "getDetailedState",  null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.NetworkInfo", lpparm.classLoader,
                "getExtraInfo", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.NetworkInfo", lpparm.classLoader,
                "isConnected",  null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.ConnectivityManager", lpparm.classLoader,
                "getActiveNetworkInfo",  null);

        Class<?> networkClass = XposedHelpers.findClass("android.net.Network",
                lpparm.classLoader);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.ConnectivityManager", lpparm.classLoader,
                "getNetworkInfo", networkClass);


        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.ConnectivityManager", lpparm.classLoader,
                "getDefaultProxy",  null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.ConnectivityManager", lpparm.classLoader,
                "getRestrictBackgroundStatus",  null);


        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.ConnectivityManager", lpparm.classLoader,
                "isActiveNetworkMetered",  null);


        /**
         * getAllByName is a static method which returns an InetAddress instance and
         * class InetAddress's constructor(s) and all fields are private and i don't konw their name
         * Todo: reflect to find them
         * now we simply change its parameter to null so that the method will return an instance
         * representing the loopback address which stop its original intent
         * Todo: check related AOSP code to find more delicate hook ways to do this
         */
        final Boolean logOn1 = ctrl.getInformPolicy(packageName, "java.net.InetAddress.getAllByName"
                , String.class);

        switch (ctrl.getPolicy(packageName, "java.net.InetAddress.getAllByName",
                String.class)) {

            case ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("java.net.InetAddress", lpparm.classLoader,
                        "getAllByName", String.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                if (logOn1) {
                                    XposedBridge.log(packageName + " java.net.InetAddress.getAllByName Allowed");
                                }
                            }
                        });

                break;

            case ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrlPolicy.BLOCK:

                /**
                 * will affect apps using host name to connect to their server
                 */
                XposedHelpers.findAndHookMethod("java.net.InetAddress", lpparm.classLoader,
                        "getAllByName", String.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        if (logOn1) {
                            XposedBridge.log(packageName + " java.net.InetAddress.getAllByName Blocked");
                        }
                        param.args[0] = null;
                    }
                });

                break;
        }

        final Boolean logOn2 = ctrl.getInformPolicy(packageName, "java.net.InetAddress.getByName"
                , String.class);

        switch (ctrl.getPolicy(packageName, "java.net.InetAddress.getByName",
                String.class)) {

            case ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("java.net.InetAddress", lpparm.classLoader,
                        "getByName", String.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        if (logOn2) {
                            XposedBridge.log(packageName + " java.net.InetAddress.getByName Allowed");
                        }
                    }
                });

                break;

            case ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrlPolicy.BLOCK:


                XposedHelpers.findAndHookMethod("java.net.InetAddress", lpparm.classLoader,
                        "getByName", String.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (logOn2) {
                            XposedBridge.log(packageName + " java.net.InetAddress.getByName Blocked");
                            }
                            param.args[0] = null;
                        }
                });

                break;
        }


        final Boolean logOn3 = ctrl.getInformPolicy(packageName, "java.net.NetworkInterface.getByInetAddress"
                , String.class);
        Class<?> inetAddressClass = XposedHelpers.findClass("java.net.InetAddress", lpparm.classLoader);

        switch (ctrl.getPolicy(packageName, "java.net.NetworkInterface.getByInetAddress",
                inetAddressClass)) {

            case ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getByInetAddress", inetAddressClass, new XC_MethodHook() {

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (logOn3) {
                                    XposedBridge.log(packageName + " java.net.NetworkInterface.getByInetAddress Allowed");
                                }
                            }
                        });

                break;

            case ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getByInetAddress", inetAddressClass, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (logOn3) {
                                XposedBridge.log(packageName + " java.net.NetworkInterface.getByInetAddress Blocked");
                            }
                                param.args[0] = null;
                            }
                        });

                break;
        }

        final Boolean logOn4 = ctrl.getInformPolicy(packageName, "java.net.NetworkInterface.getByName"
                , String.class);
        switch (ctrl.getPolicy(packageName, "java.net.NetworkInterface.getByName",
                String.class)) {

            case ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getByName", String.class, new XC_MethodHook() {

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (logOn4) {
                                    XposedBridge.log(packageName + " java.net.NetworkInterface.getByName Allowed");
                                }
                            }
                        });

                break;

            case ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getByName", String.class, new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (logOn4) {
                                    XposedBridge.log(packageName + " java.net.NetworkInterface.getByName Blocked");
                                }
                                param.args[0] = null;
                            }
                        });

                break;
        }


        final Boolean logOn5 = ctrl.getInformPolicy(packageName, "java.net.NetworkInterface.getNetworkInterfaces"
                , String.class);
        switch (ctrl.getPolicy(packageName, "java.net.NetworkInterface.getNetworkInterfaces",
                inetAddressClass)) {

            case ApiCallCtrlPolicy.ALLOW:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getNetworkInterfaces", new XC_MethodHook() {

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (logOn5) {
                                    XposedBridge.log(packageName + " java.net.NetworkInterface.getNetworkInterfaces Allowed");
                                }
                            }
                        });

                break;

            case ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrlPolicy.BLOCK:

                XposedHelpers.findAndHookMethod("java.net.NetworkInterface", lpparm.classLoader,
                        "getNetworkInterfaces", new XC_MethodHook() {

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                Class networkInterfaceClass = XposedHelpers.findClass("java.net.NetworkInterface",
                                        lpparm.classLoader);

                                Class inetAddressClass = XposedHelpers.findClass("java.net.InetAddress", lpparm.classLoader);

                                InetAddress loopbackAddress =(InetAddress) XposedHelpers.callStaticMethod(inetAddressClass,
                                        "getByName", null);

                                NetworkInterface loopbackInterface = (NetworkInterface) XposedHelpers.callStaticMethod(
                                        networkInterfaceClass, "getByInetAddress", loopbackAddress);
                                Vector<NetworkInterface> loopback = null;
                                loopback.add(loopbackInterface);
                                Enumeration<NetworkInterface> res = loopback.elements();
                                param.setResult(res);

                                if (logOn5) {
                                    XposedBridge.log(packageName + " java.net.NetworkInterface.getNetworkInterfaces Blocked");
                                }

                            }
                        });

                break;
        }
    }

}
