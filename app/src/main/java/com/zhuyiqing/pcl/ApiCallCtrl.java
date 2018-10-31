package com.zhuyiqing.pcl;

public class ApiCallCtrl {

    public enum apiCallCtrlPolicy {
        ALLOW, BLOCK, FORGE;
    }

    private static apiCallCtrlPolicy defaultPolicy = apiCallCtrlPolicy.ALLOW;

    public apiCallCtrlPolicy getPolicy(String appName, String apiName) {
        return defaultPolicy;
    }


}
