/**
 * Created by Yiqing Zhu
 * 2018/11
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhuyiqing.pcl.R;
import com.zhuyiqing.pcl.Utils.LogHelper;
import com.zhuyiqing.pcl.Utils.SettingHelper;

public class DisplayLogActivity extends AppCompatActivity {

    private static String logString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_log);

        //freshLogString();
        logString = SettingHelper.readSettingFile();
        //logString = LogHelper.readXposedLogFile();

        TextView logTextView = findViewById(R.id.logText);

        if (null != logString) {
            logTextView.setText(logString);
        }

    }

    private static void freshLogString() {
        String newLogString = LogHelper.readLogFile();
        if (null != newLogString && !LogHelper.READ_FILE_ERROR.equals(newLogString)) {
            logString = newLogString;
        }
    }


}


