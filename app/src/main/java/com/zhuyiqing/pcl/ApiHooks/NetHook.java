/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.ApiHooks;


import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import java.net.InetAddress;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NetHook {

    public static NetHook getInstance() {
        return new NetHook();
    }


    public void hookAllNetworkApi (XC_LoadPackage.LoadPackageParam loadPackageParam,
                                   ApiCallCtrl apiCallCtrl,
                                   ApiCallReturnValue apiCallReturnValue) throws Throwable{

        //XposedBridge.log("Network");

        final String packageName = loadPackageParam.packageName;


        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.NetworkInfo", loadPackageParam.classLoader,
                "getDetailedState",  null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.NetworkInfo", loadPackageParam.classLoader,
                "getExtraInfo", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.NetworkInfo", loadPackageParam.classLoader,
                "isConnected",  null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "getActiveNetworkInfo",  null);

        Class networkClass = XposedHelpers.findClass("android.net.Network",
                loadPackageParam.classLoader);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "getNetworkInfo", networkClass);


        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "getDefaultProxy",  null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "getRestrictBackgroundStatus",  null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "getRestrictBackgroundStatus",  null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.ConnectivityManager", loadPackageParam.classLoader,
                "isActiveNetworkMetered",  null);


        Class wifiConfigurationClass = XposedHelpers.findClass(
                "android.net.wifi.WifiConfiguration", loadPackageParam.classLoader);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "addNetwork", wifiConfigurationClass);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "disconnect", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "getConfiguredNetworks", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "getWifiState", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "isWifiEnabled", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "reconnect", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "removeNetwork", int.class);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "setWifiEnabled", boolean.class);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiManager", loadPackageParam.classLoader,
                "updateNetwork", wifiConfigurationClass);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiInfo", loadPackageParam.classLoader,
                "getBSSID", null);

        Class supplicantStateClass = XposedHelpers.findClass(
                "android.net.wifi.SupplicantState", loadPackageParam.classLoader);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiInfo", loadPackageParam.classLoader,
                "getDetailedStateOf", supplicantStateClass);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiInfo", loadPackageParam.classLoader,
                "getIpAddress", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiInfo", loadPackageParam.classLoader,
                "getMacAddress", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "android.net.wifi.WifiInfo", loadPackageParam.classLoader,
                "getSSID", null);

        HookMethod.startHook(apiCallCtrl, apiCallReturnValue, packageName,
                "java.net.InetAddress", loadPackageParam.classLoader,
                "getAllByName", String.class);



        // getAllByName is a static method which returns an InetAddress instance and
        // class InetAddress's constructor(s) and all fields are private and i don't konw their name
        // Todo: reflect to find them
        // now we simply change its parameter to null so that the method will return an instance
        // representing the loopback address which stop its original intent
        // Todo: check related AOSP code to find more delicate hook ways to do this

        switch (apiCallCtrl.getPolicy(packageName, "java.net.InetAddress.getAllByName",
                String.class)) {

            case ApiCallCtrl.ApiCallCtrlPolicy.ALLOW:

                XposedBridge.log(packageName + "java.net.InetAddress.getAllByName Allowed");
                break;

            case ApiCallCtrl.ApiCallCtrlPolicy.FORGE:

                // same treat as block

            case ApiCallCtrl.ApiCallCtrlPolicy.BLOCK:


                XposedHelpers.findAndHookMethod("java.net.InetAddress", loadPackageParam.classLoader,
                        "getAllByName", String.class, new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                        XposedBridge.log(packageName + "java.net.InetAddress.getAllByName Blocked");

                        param.args[0] = null;
                    }
                });

                break;
        }




    }





}
