/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;

import android.util.ArrayMap;

import com.zhuyiqing.pcl.Utils.SettingHelper;


public class ApiCallCtrl {

    public static class ApiCallCtrlPolicy {
        public static final int ALLOW = 0;
        public static final int BLOCK = 1;
        public static final int FORGE = 2;
    }

    public static class LogInformLevel {
        public static final int LOW = 0;
        public static final int MIDDLE = 1;
        public static final int HIGH = 2;
    }

    public class PolicyPack {
        public int ctrlPolicy;
        public int informLevel;
    }

    public ApiCallCtrl() {
        loadPolicy();
    }

    private ArrayMap<String, PolicyPack> currentPolicy = new ArrayMap<>();

    private int defaultPolicy = ApiCallCtrlPolicy.ALLOW;

    private int defaultInformLevel = LogInformLevel.MIDDLE;

    private int currentInformLevel = LogInformLevel.HIGH;

    public int getPolicy(String appName, String apiName, Object... parameterType) {

        int policy = defaultPolicy;

        /** global rules */

        if (currentPolicy.containsKey("* " + apiName)) {
            policy = currentPolicy.get("* " + apiName).ctrlPolicy;
        }

        /** specific rules overwrites global ones */

        if (currentPolicy.containsKey(appName + " " + apiName)) {
            policy = currentPolicy.get(appName + " " + apiName).ctrlPolicy;
        }

        /** if no rules are defined, adopt default ones */

        return policy;
    }

    public boolean getInformPolicy(String appName, String apiName, Object... parameterType) {

        int level = defaultInformLevel;

        /** global rules */

        if (currentPolicy.containsKey("* " + apiName)) {
            level = currentPolicy.get("* " + apiName).informLevel;
        }

        /** specific rules overwrites global ones */

        if (currentPolicy.containsKey(appName + " " + apiName)) {
            level = currentPolicy.get(appName + " " + apiName).informLevel;
        }

        /** if no rules are defined, adopt default ones */

        if (level >= currentInformLevel) return true;
        else return false;
    }

    public void setCurrentInformLevel(int currentInformLevel) {
        this.currentInformLevel = currentInformLevel;
    }

    private void loadPolicy() {
        loadDefaultPolicy();
        loadSavedSetting();
        saveSetting();
    }

    private void loadHelper(String key, int ctrlPolicy, int logPolicy) {
        PolicyPack policyPack = new PolicyPack();
        policyPack.ctrlPolicy = ctrlPolicy;
        policyPack.informLevel = logPolicy;
        currentPolicy.put(key, policyPack);
    }

    private void saveSetting() {
        String saveData = "";
        for (String key : currentPolicy.keySet()) {
            saveData = saveData + key + "," + currentPolicy.get(key).ctrlPolicy + "," + currentPolicy.get(key).informLevel + "\n";
        }
        SettingHelper.saveSettingFile(saveData);
    }

    private void loadSavedSetting() {
        String savedSetting = SettingHelper.readSettingFile();
        String[] settings = savedSetting.split("\n");
        if (null != settings) {
            for (String setting : settings) {
                String[] settingSeg = setting.split(",");
                if (settingSeg.length>=3) {
                    loadHelper(settingSeg[0], Integer.parseInt(settingSeg[1]), Integer.parseInt(settingSeg[2]));
                }
            }
        }
    }


    private void loadDefaultPolicy() {

        loadHelper("* android.net.NetworkInfo.getDetailedState", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.NetworkInfo.getExtraInfo", ApiCallCtrlPolicy.BLOCK, LogInformLevel.MIDDLE);
        loadHelper("* android.net.NetworkInfo.isConnected", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.net.ConnectivityManager.getActiveNetworkInfo", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.ConnectivityManager.getNetworkInfo", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.net.ConnectivityManager.getDefaultProxy", ApiCallCtrlPolicy.BLOCK, LogInformLevel.MIDDLE);
        loadHelper("* android.net.ConnectivityManager.getRestrictBackgroundStatus", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.ConnectivityManager.isActiveNetworkMetered", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiManager.addNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.disconnect", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.getConfiguredNetworks", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.getWifiState", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.isWifiEnabled", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.reconnect", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.removeNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.setWifiEnabled", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiManager.updateNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* java.net.InetAddress.getAllByName", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* java.net.NetworkInterface.getNetworkInterfaces", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.net.wifi.WifiInfo.getBSSID", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getSSID", ApiCallCtrlPolicy.BLOCK, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getDetailedStateOf", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getIpAddress", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getMacAddress", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.telephony.SmsManager.sendTextMessage", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.CALL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.NEW_OUTGOING_CALL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.DIAL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* java.net.HttpURLConnection", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.telephony.TelephonyManager.listen", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.location.Location.getLatitude", ApiCallCtrlPolicy.FORGE, LogInformLevel.HIGH);
        loadHelper("* android.location.Location.getLongitude", ApiCallCtrlPolicy.FORGE, LogInformLevel.HIGH);
        loadHelper("* android.provider.Settings.Secure.getString.ANDROID_ID", ApiCallCtrlPolicy.FORGE, LogInformLevel.HIGH);
        loadHelper("* android.telephony.TelephonyManager.getDeviceId", ApiCallCtrlPolicy.FORGE, LogInformLevel.HIGH);
    }

    public static String[] CtrlApiList = {
            "android.net.NetworkInfo.getDetailedState",
            "android.net.NetworkInfo.getExtraInfo",
            "android.net.NetworkInfo.isConnected",
            "android.net.ConnectivityManager.getActiveNetworkInfo",
            "android.net.ConnectivityManager.getNetworkInfo",
            "android.net.ConnectivityManager.getDefaultProxy",
            "android.net.ConnectivityManager.getRestrictBackgroundStatus",
            "android.net.ConnectivityManager.isActiveNetworkMetered",
            "android.net.wifi.WifiManager.addNetwork",
            "android.net.wifi.WifiManager.disconnect",
            "android.net.wifi.WifiManager.getConfiguredNetworks",
            "android.net.wifi.WifiManager.getWifiState",
            "android.net.wifi.WifiManager.isWifiEnabled",
            "android.net.wifi.WifiManager.reconnect",
            "android.net.wifi.WifiManager.removeNetwork",
            "android.net.wifi.WifiManager.setWifiEnabled",
            "android.net.wifi.WifiManager.updateNetwork",
            "java.net.InetAddress.getAllByName",
            "java.net.NetworkInterface.getNetworkInterfaces",
            "android.net.wifi.WifiInfo.getBSSID",
            "android.net.wifi.WifiInfo.getSSID",
            "android.net.wifi.WifiInfo.getDetailedStateOf",
            "android.net.wifi.WifiInfo.getIpAddress",
            "android.net.wifi.WifiInfo.getMacAddress",
            "android.telephony.SmsManager.sendTextMessage",
            "android.content.ContextWrapper.startActivityandroid.intent.action.CALL",
            "android.content.ContextWrapper.startActivityandroid.intent.action.NEW_OUTGOING_CALL",
            "android.content.ContextWrapper.startActivityandroid.intent.action.DIAL",
            "java.net.HttpURLConnection",
            "android.telephony.TelephonyManager.listen",
            "android.location.Location.getLatitude",
            "android.location.Location.getLongitude",
            "android.provider.Settings.Secure.getString.ANDROID_ID",
            "android.telephony.TelephonyManager.getDeviceId",
    };


}
