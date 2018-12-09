/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.Utils;

import com.zhuyiqing.pcl.BuildConfig;

import de.robv.android.xposed.XSharedPreferences;

public class PreferenceUtils {

    public static XSharedPreferences instance = null;

    public static XSharedPreferences getInstance() {
        if(null==instance) {
            instance = new XSharedPreferences(BuildConfig.APPLICATION_ID, "PCL");
            instance.makeWorldReadable();
        }
        else {
            instance.reload();
        }
        return instance;
    }

}
