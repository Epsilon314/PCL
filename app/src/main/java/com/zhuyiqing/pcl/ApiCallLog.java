package com.zhuyiqing.pcl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

public class ApiCallLog {

    public ApiCallLog() {

    }

    static int logCount = 0;

    private int setId() {
        return logCount++;
    }

    private class callRecord {

        private int recordId;
        private String callTime;
        private String caller;
        private String callApi;
        private String handleResult;

        public callRecord(int mRecordId, String mCaller, String mCallApi, String mHandleResult) {

            this.recordId = mRecordId;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date(System.currentTimeMillis());
            this.callTime = simpleDateFormat.format(now);

            this.caller = mCaller;
            this.callApi = mCallApi;
            this.handleResult = mHandleResult;
        }

        public String toString() {
            return callTime + ":" + caller + " " + callApi + " " + handleResult + "\n";
        }


        public void printToXposedLog() {
            XposedBridge.log(this.toString());
        }

    }

    private static final int ESTIMATED_LOG_SIZE = 500;

    private ArrayList<callRecord> logs = new ArrayList<>(ESTIMATED_LOG_SIZE);

    public int addRecord(String caller, String callApi, String handleResult) {
        int newId = setId();
        callRecord newRecord = new callRecord(newId, caller, callApi, handleResult);
        logs.add(newId,newRecord);
        return newId;
    }


    public void clearAll() {
        logs.clear();
        logCount = 0;
    }

    public void printAllToXposedLog() {
        for (callRecord log : logs) {
            if (null != log) {
                log.printToXposedLog();
            }
        }
    }

    public void printToXposedLog(int id) {
        if (null != logs.get(id))
            logs.get(id).printToXposedLog();
    }


}
