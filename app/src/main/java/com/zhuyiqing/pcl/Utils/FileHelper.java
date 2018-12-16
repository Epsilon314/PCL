package com.zhuyiqing.pcl.Utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class FileHelper {

    protected static final String filePath = "/data/system/xposed_pcl";
    public static final String READ_FILE_ERROR = "Read File Error!";

    protected static void writeFileAppend(String item, String fileName) {

        String fileFullName = filePath + fileName;

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

    protected static void writeFileReplace(String item, String fileName) {

        String fileFullName = filePath + fileName;

        try {

            File logFile = new File(fileFullName);

            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            if (logFile.exists()) {
                logFile.delete();
                logFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(logFile);
            byte [] bytes = item.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static String readFile(String fileName) {

        String fileFullName = filePath + fileName;

        if (!getRoot(filePath)) {
            return "permission denied\n";
        }
        if (!getRoot(fileFullName)) {
            return "permission denied\n";
        }

        String res = "";

        try {

            File logFile = new File(fileFullName);
            FileInputStream fileInputStream = new FileInputStream(logFile);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            res = new String(bytes, "UTF-8");
            fileInputStream.close();

        } catch (Exception e) {
            /*e.printStackTrace();
            res = e.getMessage() + "\n";
            for (StackTraceElement m : e.getStackTrace()) {
                res += m.toString() + "\n";
            }*/
        }

        return res;
    }

    private static boolean getRoot(String dir) {
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
