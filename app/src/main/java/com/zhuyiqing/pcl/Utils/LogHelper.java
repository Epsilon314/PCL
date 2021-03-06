/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.Utils;


import java.text.SimpleDateFormat;
import java.util.Date;




// will be called from app thread, so don't use anything from Xposed here

public class LogHelper extends FileHelper{

    private static final String fileName = "/log1";
    private static final String backupFileName = "/log2";
    private static final String XposedLogFilePath = "/data/data/de.robv.android.xposed.installer/log/error.log";

    public static void writeToLogFile(String item) {
        writeFileAppend(item,fileName);
    }

    public static String readLogFile() {
        return readFile(fileName);
    }

    public static String readXposedLogFile() {
        return readFileByPath(XposedLogFilePath);
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

    public static void cleanLog() {
        String temp = readFile(fileName);
        writeFileReplace("", fileName);
        writeFileReplace(temp, backupFileName);
    }

}
