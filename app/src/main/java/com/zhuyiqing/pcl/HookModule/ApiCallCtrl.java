/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;


import java.util.HashMap;

public class ApiCallCtrl {

    public static class ApiCallCtrlPolicy {
        public static final int ALLOW = 0;
        public static final int BLOCK = 1;
        public static final int FORGE = 2;
    }

    public ApiCallCtrl() {
        loadPolicy();
    }

    private HashMap<String, Integer> currentPolicy = new HashMap<>();

    private int defaultPolicy = ApiCallCtrlPolicy.ALLOW;

    public int getPolicy(String appName, String apiName, Object parameterType) {
        int policy;
        if (currentPolicy.containsKey(appName + " " + apiName)) {
            policy = currentPolicy.get(appName + " " + apiName);
        }
        else if (currentPolicy.containsKey("* " + apiName)) {
            policy = currentPolicy.get("* " + apiName);
        }
        else {
            policy = defaultPolicy;
        }
        return policy;
    }

    private void loadPolicy() {
        loadDefaultPolicy();
        //Todo load customer policy
    }

    private void loadDefaultPolicy() {
        currentPolicy.put("* android.net.NetworkInfo.getDetailedState", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.NetworkInfo.getExtraInfo", ApiCallCtrlPolicy.BLOCK);
        currentPolicy.put("* android.net.NetworkInfo.isConnected", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.ConnectivityManager.getActiveNetworkInfo", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.ConnectivityManager.getNetworkInfo", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.ConnectivityManager.getDefaultProxy", ApiCallCtrlPolicy.BLOCK);
        currentPolicy.put("* android.net.ConnectivityManager.getRestrictBackgroundStatus", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.ConnectivityManager.isActiveNetworkMetered", ApiCallCtrlPolicy.ALLOW);
        currentPolicy.put("* android.net.wifi.WifiManager.addNetwork", ApiCallCtrlPolicy.BLOCK);
        currentPolicy.put("* java.net.InetAddress.getAllByName", ApiCallCtrlPolicy.BLOCK);
    }



}
