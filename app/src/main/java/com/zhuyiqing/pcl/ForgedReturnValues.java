/**
 * Created by Yiqing Zhu
 * 2018/11
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl;

import android.net.NetworkInfo;

public class ForgedReturnValues {

    private class ClassNameChain {
        private String className;
        private MethodNameChain methodNameChain;
        private ClassNameChain next = null;
    }

    private class MethodNameChain {
        private String methodName;
        private Object forgedValue;
        private  MethodNameChain next = null;
    }

    private ClassNameChain head;

    public ForgedReturnValues() {
        setDefaultValue();
    }

    public Object getForgedValue(String className, String methodName) {

        return NetworkInfo.DetailedState.DISCONNECTED;
        /**
        ClassNameChain searchClassName = head;
        while (searchClassName.className != className) {
           searchClassName = searchClassName.next;
           if (null == searchClassName) return null;
        }
        MethodNameChain searchMethodName = searchClassName.methodNameChain;
        if (null == searchMethodName) return null;
        while (searchMethodName.methodName != methodName) {
            searchMethodName = searchMethodName.next;
            if (null == searchMethodName) return null;
        }
        return searchMethodName.forgedValue;
         **/
    }

    public void setDefaultValue() {

        ClassNameChain editClassNameChain;
        MethodNameChain editMethodNameChain;

        head = new ClassNameChain();
        head.className = "android.net.NetworkInfo";
        head.methodNameChain = new MethodNameChain();
        //head.next = new ClassNameChain();

        editClassNameChain = head.next;
        editMethodNameChain = head.methodNameChain;

        editMethodNameChain.methodName ="getDetailedState";
        editMethodNameChain.forgedValue = NetworkInfo.DetailedState.DISCONNECTED;
        //editMethodNameChain.next = new MethodNameChain();
        //editMethodNameChain = editMethodNameChain.next;

    }



}
