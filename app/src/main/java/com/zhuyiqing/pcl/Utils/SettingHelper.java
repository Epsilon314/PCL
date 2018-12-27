/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.Utils;


import java.util.Vector;

/**
 * save custom settings to file
 * those settings will be activated after reboot
 */
public class SettingHelper extends FileHelper{

    private static final String fileName = "/setting";
    private static final String packageNameFileName = "/packages";
    private static final String packageTempFileName = "/packagesTemp";
    private static final String globalPolicyFileName = "/globalSetting";

    public static void saveSettingFile(String item) {
        writeFileReplace(item, fileName);
    }

    public static void saveSettingFileAppend(String item) {
        writeFileAppend(item, fileName);
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

            packageNames = "*,"+packageNames;
            Vector<String> ret = new Vector<>();
            for (String s : packageNames.split(",")) {

                Boolean dupFlag = false;
                for (int i = 0; i < ret.size(); i++) {
                    if (s==ret.get(i)) {
                        dupFlag = true;
                        break;
                    }
                }
                if (!dupFlag) ret.add(s);
            }
            String[] res = new String[ret.size()];
            for (int i = 0; i < ret.size(); i++) {
                res[i] = ret.get(i);
            }
            return res;
        }
        else  return null;
    }

    public static void writeGlobalPolicy(int globalPolicy, int globalInfoLevel) {
        writeFileReplace(globalPolicy + "," + globalInfoLevel, globalPolicyFileName);
    }

    public static int[] getGlobalPolicy() throws Exception{
        String res = readFile(globalPolicyFileName);
        if (null != res) {
            String[] policyString = res.split(",");
            if (null != policyString && policyString.length == 2) {
                int[] policyInt = new int[2];
                policyInt[0] =  Integer.parseInt(policyString[0]);
                policyInt[1] = Integer.parseInt(policyString[1]);
                return policyInt;
            }
        }
        throw new Exception();
    }

    public static void clearPackageNames() {
        writeFileReplace("", packageNameFileName);
    }

}
