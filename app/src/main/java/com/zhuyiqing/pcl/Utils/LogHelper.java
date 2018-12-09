/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.Utils;

import java.io.FileInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;


// will be called from app thread, so don't use anything from Xposed here

public class LogHelper {

    private static final String filePath = "/data/system/xposed_pcl";
    private static final String fileName = "/log";
    private static final String fileFullName = filePath + fileName;
    public static final String READ_FILE_ERROR = "Read File Error!";


    public static void writeToLogFile(String item) {
        //GetRootPermission.getRoot(fileFullName);

        try {

            File logFile = new File(fileFullName);

            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "rw");
            randomAccessFile.seek(logFile.length());
            byte [] bytes = item.getBytes();
            randomAccessFile.write(bytes);
            randomAccessFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String readLogFile() {

        if (!GetRootPermission.getRoot(fileFullName)) {
            return "permission denied\n";
        }

        String res;

        try {

            File logFile = new File(fileFullName);
            FileInputStream fileInputStream = new FileInputStream(logFile);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            res = new String(bytes, "UTF-8");
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage() + "\n";
            for (StackTraceElement m : e.getStackTrace()) {
                res += m.toString() + "\n";
            }
        }

        return res;
    }

    public static String generateLog(String caller, String callApi, String handleResult) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
        String timeStamp = simpleDateFormat.format(now);
        return timeStamp + ":" + caller + "  " + callApi + "  " + handleResult + "\n";
    }

    public static void generateAndWriteLog(String caller, String callApi, String handleResult) {
        writeToLogFile(generateLog(caller,callApi,handleResult));
    }

    public static void generateAndPrintToXposed(String caller, String callApi, String handleResult) {
        XposedBridge.log(caller + "  " + callApi + "  " + handleResult + "\n");
    }

}
