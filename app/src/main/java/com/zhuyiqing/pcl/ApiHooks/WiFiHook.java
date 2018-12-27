/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * hook things related to wifi
 * not working on simulator for free visions does not support wifi, sad
 * Todo: test on real devices later
 */
public class WiFiHook implements HookBase{

    public static WiFiHook getInstance() {
        return new WiFiHook();
    }

    public void startHook(final XC_LoadPackage.LoadPackageParam lpparm,
                          ApiCallCtrl ctrl,
                          ApiCallReturnValue returnValue) {

        final String packageName = lpparm.packageName;

        Class<?> wifiConfigurationClass = XposedHelpers.findClass(
                "android.net.wifi.WifiConfiguration", lpparm.classLoader);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "addNetwork", wifiConfigurationClass);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "disconnect", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "getConfiguredNetworks", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "getWifiState", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "isWifiEnabled", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "reconnect", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "removeNetwork", int.class);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "setWifiEnabled", boolean.class);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiManager", lpparm.classLoader,
                "updateNetwork", wifiConfigurationClass);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiInfo", lpparm.classLoader,
                "getBSSID", null);

        Class<?> supplicantStateClass = XposedHelpers.findClass(
                "android.net.wifi.SupplicantState", lpparm.classLoader);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiInfo", lpparm.classLoader,
                "getDetailedStateOf", supplicantStateClass);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiInfo", lpparm.classLoader,
                "getIpAddress", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiInfo", lpparm.classLoader,
                "getMacAddress", null);

        HookMethod.startHook(ctrl, returnValue, packageName,
                "android.net.wifi.WifiInfo", lpparm.classLoader,
                "getSSID", null);
    }

}
