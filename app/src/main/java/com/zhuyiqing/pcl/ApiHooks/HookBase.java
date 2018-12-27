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
 * Interface for simple hook pattern
 */
public interface HookBase {

    void startHook(final XC_LoadPackage.LoadPackageParam lpparm,
                   ApiCallCtrl ctrl,
                   ApiCallReturnValue returnValue) throws Throwable;

}
