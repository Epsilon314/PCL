/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */



package com.zhuyiqing.pcl.Utils;

import java.io.DataOutputStream;


// will be called from app thread, so don't use anything from Xposed here

public class GetRootPermission {

    public static boolean getRoot(String dir) {
        Process process = null;
        DataOutputStream outputStream = null;
        try {
            String cmd = "chmod 777 " + dir + "\nexit\n";
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(cmd);
            outputStream.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
        }
        return true;
    }
}
