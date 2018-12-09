/**
 * Created by Yiqing Zhu
 * 2018/10
 * yiqing.zhu.314@gmail.com
 */


package com.zhuyiqing.pcl.HookModule;


import com.zhuyiqing.pcl.Utils.LogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import de.robv.android.xposed.XposedBridge;

public class ApiCallLog {


    public ApiCallLog() {

    }

    private static int logCount = 0;

    private int setId() {
        return logCount++;
    }

    public static class SevereLevel {
        public static final int LOW = 0;
        public static final int MIDDLE = 1;
        public static final int HIGH = 2;
    }

    private static final int DEFAULT_INFORM_LEVEL = SevereLevel.MIDDLE;
    private static final int DEFAULT_SEVERE_LEVEL = SevereLevel.HIGH;

    private int informLevel = DEFAULT_INFORM_LEVEL;

    private static final int ESTIMATED_LOG_SIZE = 500;

    private ArrayList<callRecord> logs = new ArrayList<>(ESTIMATED_LOG_SIZE);


    private class callRecord {

        private int recordId;
        private String callTime;
        private String caller;
        private String callApi;
        private String handleResult;
        private int severeLevel;

        public callRecord(int mRecordId, String mCaller, String mCallApi, String mHandleResult) {

            this.recordId = mRecordId;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date(System.currentTimeMillis());
            this.callTime = simpleDateFormat.format(now);

            this.caller = mCaller;
            this.callApi = mCallApi;
            this.handleResult = mHandleResult;
            this.severeLevel = DEFAULT_SEVERE_LEVEL;
        }

        public String toString() {
            return callTime + ":" + caller + " " + callApi + " " + handleResult + "\n";
        }

        public String toStringWithoutTimestamp() {
            return caller + " " + callApi + " " + handleResult + "\n";
        }


        public void printToXposedLog() {
            XposedBridge.log(this.toStringWithoutTimestamp());
        }

        public void printToPclLog() {
            LogHelper.writeToLogFile(this.toString());
        }

        public int getRecordId() {
            return recordId;
        }

        public void setSevereLevel(int severeLevel) {
            this.severeLevel = severeLevel;
        }
    }

    public int addRecord(String caller, String callApi, String handleResult) {
        int newId = setId();
        callRecord newRecord = new callRecord(newId, caller, callApi, handleResult);
        logs.add(newRecord);
        return newId;
    }


    public void clearAll() {
        logs.clear();
        logCount = 0;
    }

    public void printAllToXposedLog() {
        for (int i = 0; i < logs.size(); i++) {
            callRecord log = logs.get(i);
            if (null != log) {
                log.printToXposedLog();
            }
        }
    }

    public void printAllToPclLog() {
        for (int i = 0; i < logs.size(); i++) {
            callRecord log = logs.get(i);
            if (null != log) {
                log.printToPclLog();
            }
        }
    }

    public void setInformLevel(int severeLevel) {
        informLevel = severeLevel;
    }

    public callRecord getCallRecord(int id) {
        callRecord cr = logs.get(id);
        if (null != cr && cr.getRecordId()==id) {
            return cr;
        }
        for (callRecord record : logs) {
            if (record.getRecordId() == id) return record;
        }
        return null;
     }

}
