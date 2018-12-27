/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;

import android.util.ArrayMap;

import com.zhuyiqing.pcl.Utils.SettingHelper;


/**
 *  manage access policies and inform policies
 */
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

    /**
     * store all policies in the form of key-value in an array-map
     * the number of policies are estimated to be like tens to hundreds and the type of key is String,
     * so array-map shall be the fittest
     */
    private ArrayMap<String, PolicyPack> currentPolicy = new ArrayMap<>();

    /**
     * policy used when no explicit settings are available, configurable
     */
    private int defaultPolicy = ApiCallCtrlPolicy.ALLOW;


    /**
     * policy used when no explicit settings are available
     */
    private int defaultInformLevel = LogInformLevel.MIDDLE;


    /**
     * minimal inform level an event shall have for it to be logged, configurable
     */
    private int currentInformLevel = LogInformLevel.MIDDLE;


    /**
     * get policy
     * more specific rules overwrite less ones
     * in detail, specific rules overwrite rules with wildcards overwrite global ones
     * @param appName
     * @param apiName
     * @param parameterType
     * @return
     */
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

    /**
     *  get policy
     *  more specific rules overwrite less ones
     *  in detail, specific rules overwrite rules with wildcards overwrite global ones
     * @param appName
     * @param apiName
     * @param parameterType
     * @return
     */
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


    /**
     * load saved settings after default ones to overwrite conflicts
     * save after load removes duplication in the settings file
     */
    private void loadPolicy() {
        loadSavedGlobalSetting();
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

    private void loadSavedGlobalSetting() {
        try{
            int[] settings = SettingHelper.getGlobalPolicy();
            defaultPolicy = settings[0];
            currentInformLevel = settings[1];
        }catch (Exception e) {

        }
    }


    private void loadDefaultPolicy() {

        loadHelper("* android.net.NetworkInfo.getDetailedState", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.net.NetworkInfo.getExtraInfo", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.NetworkInfo.isConnected", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.ConnectivityManager.getActiveNetworkInfo", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.ConnectivityManager.getNetworkInfo", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.ConnectivityManager.getDefaultProxy", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.net.ConnectivityManager.getRestrictBackgroundStatus", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.ConnectivityManager.isActiveNetworkMetered", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiManager.addNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiManager.disconnect", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiManager.getConfiguredNetworks", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiManager.getWifiState", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiManager.isWifiEnabled", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiManager.reconnect", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiManager.removeNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiManager.setWifiEnabled", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiManager.updateNetwork", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* java.net.InetAddress.getAllByName", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* java.net.NetworkInterface.getNetworkInterfaces", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.net.wifi.WifiInfo.getBSSID", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getSSID", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getDetailedStateOf", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getIpAddress", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.net.wifi.WifiInfo.getMacAddress", ApiCallCtrlPolicy.ALLOW, LogInformLevel.LOW);
        loadHelper("* android.telephony.SmsManager.sendTextMessage", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.CALL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.NEW_OUTGOING_CALL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* android.content.ContextWrapper.startActivityandroid.intent.action.DIAL", ApiCallCtrlPolicy.BLOCK, LogInformLevel.HIGH);
        loadHelper("* java.net.HttpURLConnection", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.telephony.TelephonyManager.listen", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.location.Location.getLatitude", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.location.Location.getLongitude", ApiCallCtrlPolicy.ALLOW, LogInformLevel.HIGH);
        loadHelper("* android.provider.Settings.Secure.getString.ANDROID_ID", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
        loadHelper("* android.telephony.TelephonyManager.getDeviceId", ApiCallCtrlPolicy.ALLOW, LogInformLevel.MIDDLE);
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
