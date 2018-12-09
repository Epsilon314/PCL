/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;

import android.net.wifi.WifiInfo;

import java.util.HashMap;

public class ApiCallReturnValue {

    public static class ReturnModifyType {
        public static final int BLOCK = 0;
        public static final int FORGE = 1;
        public static final int EARLY_BLOCK = 2;
        public static final int EARLY_FORGE = 3;
    }

    private HashMap<String, Object> currentReturnValueSetting = new HashMap<>();

    public ApiCallReturnValue() {
        loadSetting();
    }

    public Object getReturnValue(String classAndMethod, int mode) {
        if (mode == ReturnModifyType.BLOCK) {
            if (currentReturnValueSetting.containsKey(classAndMethod + " b")) {
                return currentReturnValueSetting.get(classAndMethod + " b");
            }
            return null;
        }
        else if (mode == ReturnModifyType.FORGE) {
            if (currentReturnValueSetting.containsKey(classAndMethod + " f")) {
                return currentReturnValueSetting.get(classAndMethod + " f");
            }
            return null;
        }
        else if (mode == ReturnModifyType.EARLY_BLOCK) {
            if (currentReturnValueSetting.containsKey(classAndMethod + " eb")) {
                return currentReturnValueSetting.get(classAndMethod + " eb");
            }
            return null;
        }
        else if (mode == ReturnModifyType.EARLY_FORGE) {
            if (currentReturnValueSetting.containsKey(classAndMethod + " ef")) {
                return currentReturnValueSetting.get(classAndMethod + " ef");
            }
            return null;
        }
        return null;
    }

    private void loadSetting() {
        loadDefaultSetting();
        //Todo: customer setting
    }

    private void loadDefaultSetting() {
        currentReturnValueSetting.put("android.net.NetworkInfo.getDetailedState b", null);
        currentReturnValueSetting.put("android.net.NetworkInfo.getDetailedState f", null);
        currentReturnValueSetting.put("android.net.NetworkInfo.getExtraInfo b", "");
        currentReturnValueSetting.put("android.net.NetworkInfo.getExtraInfo f", "MOBILE");
        currentReturnValueSetting.put("android.net.NetworkInfo.isConnected b", false);
        currentReturnValueSetting.put("android.net.NetworkInfo.isConnected f", true);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getActiveNetworkInfo b", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getActiveNetworkInfo f", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getNetworkInfo b", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getNetworkInfo f", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getDefaultProxy b", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getDefaultProxy f", null);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getRestrictBackgroundStatus b", 3);
        currentReturnValueSetting.put("android.net.ConnectivityManager.getRestrictBackgroundStatus f", 3);
        currentReturnValueSetting.put("android.net.ConnectivityManager.isActiveNetworkMetered b", true);
        currentReturnValueSetting.put("android.net.ConnectivityManager.isActiveNetworkMetered f", true);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.addNetwork eb", -1);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.disconnect eb", false);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.disconnect ef", true);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.getWifiState b", 1);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.isWifiEnabled b", false);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.reconnect eb", false);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.reconnect ef", true);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.removeNetwork eb", false);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.removeNetwork ef", true);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.setWifiEnabled eb", false);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.setWifiEnabled ef", true);
        currentReturnValueSetting.put("android.net.wifi.WifiManager.updateNetwork eb", -1);
        currentReturnValueSetting.put("android.net.wifi.WifiInfo.getBSSID f", "06:06:06:06:06:06");
        currentReturnValueSetting.put("android.net.wifi.WifiInfo.getSSID f", "makeupsomething");

    }
}
