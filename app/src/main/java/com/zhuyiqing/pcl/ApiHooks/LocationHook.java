/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;


import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Hook location
 * Todo: hook more location provider
 */
public class LocationHook implements HookBase{

    public static LocationHook getInstance() {
        return new LocationHook();
    }

    @Override
    public void startHook(XC_LoadPackage.LoadPackageParam lpparm, ApiCallCtrl ctrl, ApiCallReturnValue returnValue) {


        final String packageName = lpparm.packageName;

        HookMethod.startHook(ctrl, returnValue, packageName, "android.location.Location",
                lpparm.classLoader, "getLatitude", null);

        HookMethod.startHook(ctrl, returnValue, packageName, "android.location.Location",
                lpparm.classLoader, "getLongitude", null);


    }
}
