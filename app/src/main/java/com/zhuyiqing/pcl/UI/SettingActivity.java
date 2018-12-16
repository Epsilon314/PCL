package com.zhuyiqing.pcl.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuyiqing.pcl.R;
import com.zhuyiqing.pcl.Utils.SettingHelper;

public class SettingActivity extends AppCompatActivity {

    LinearLayout settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        settings = findViewById(R.id.settingsLayout);
        generateSettingItem();

    }

    private void generateSettingItem() {

        String[] packages = SettingHelper.readPackageName();

        if (null != packages) {
            for (String packetName : packages)  {
                TextView packetNameView = new TextView(this);
                packetNameView.setText(packetName);
                LinearLayout item = new LinearLayout(this);
                item.addView(packetNameView);
                settings.addView(item);
            }
        }


    }


}
