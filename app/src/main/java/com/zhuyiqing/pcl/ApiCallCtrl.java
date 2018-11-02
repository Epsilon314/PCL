/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl;


public class ApiCallCtrl {

    public static class ApiCallCtrlPolicy {
        public static final int ALLOW = 0;
        public static final int BLOCK = 1;
        public static final int FORGE = 2;
    }

    private int defaultPolicy = ApiCallCtrlPolicy.FORGE;

    public int getPolicy(String appName, String apiName) {

        return defaultPolicy;
    }

}
