/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.Utils;



public class SettingHelper extends FileHelper{

    private static final String fileName = "/setting";
    private static final String packageNameFileName = "/packages";
    private static final String packageTempFileName = "/packagesTemp";

    public static void saveSettingFile(String item) {
        writeFileReplace(item, fileName);
    }

    public static String readSettingFile() {
        return readFile(fileName);
    }

    public static void savePackageNames(String name) {
        writeFileAppend(name + ",", packageNameFileName);
    }

    public static String[] readPackageName() {
        String packageNames = readFile(packageNameFileName);
        if (packageNames == null || packageNames.length() <= 1) {
            packageNames = readFile(packageTempFileName);
        }
        else {
            writeFileReplace(packageNames, packageTempFileName);
            writeFileReplace("", packageNameFileName);
        }
        if (null != packageNames) {
            return packageNames.split(",");
        }
        else  return null;
    }

    public static void clearPackageNames() {
        writeFileReplace("", packageNameFileName);
    }

}
