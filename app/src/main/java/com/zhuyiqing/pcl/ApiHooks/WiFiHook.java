package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class WiFiHook implements HookBase{

    public static WiFiHook getInstance() {
        return new WiFiHook();
    }

    public void startHook(final XC_LoadPackage.LoadPackageParam loadPackageParam,
                                   ApiCallCtrl apiCallCtrl,
                                   ApiCallReturnValue apiCallReturnValue) {

        final String packageName = loadPackageParam.packageName;

        Class<?> wifiConfigurationClass = XposedHelpers.findClass(
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

        Class<?> supplicantStateClass = XposedHelpers.findClass(
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
    }

}
