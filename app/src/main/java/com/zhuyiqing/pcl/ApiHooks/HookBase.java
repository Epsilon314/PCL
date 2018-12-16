package com.zhuyiqing.pcl.ApiHooks;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.HookModule.ApiCallReturnValue;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public interface HookBase {

    void startHook(final XC_LoadPackage.LoadPackageParam loadPackageParam,
                          ApiCallCtrl apiCallCtrl,
                          ApiCallReturnValue apiCallReturnValue) throws Throwable;

}
