/**
 * Created by Yiqing Zhu
 * 2018/12
 * yiqing.zhu.314@gmail.com
 */




package com.zhuyiqing.pcl.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.zhuyiqing.pcl.HookModule.ApiCallCtrl;
import com.zhuyiqing.pcl.R;
import com.zhuyiqing.pcl.Utils.SettingHelper;

public class SettingActivity extends AppCompatActivity {

    private Spinner packageSpin;
    private Spinner apiSpin;
    private Spinner policySpin;
    private Spinner infoSpin;

    private Spinner globalPolicySpin;
    private Spinner globalInfoSpin;

    private String currentPackage = "";
    private String currentApi = "";
    private int currentPolicy = -1;
    private int currentInfo = -1;

    private int currentGlobalPolicy = -1;
    private int currentGlobalInfo = -1;

    private String[] policySet = {"Allow", "Block", "Forge"};
    private String[] infoSet = {"Low", "Middle", "High"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        packageSpin = findViewById(R.id.PackageSpin);
        apiSpin = findViewById(R.id.ApiSpin);
        policySpin = findViewById(R.id.PolicySpin);
        infoSpin = findViewById(R.id.InfoSpin);

        globalPolicySpin = findViewById(R.id.spPolicy);
        globalInfoSpin = findViewById(R.id.spInfo);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPolicyHelper();
            }
        });

        Button globalSaveButton = findViewById(R.id.buttonGlobal);
        globalSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGlobalPolicyHelper();
            }
        });

        generateSettingItem();
        generateGlobalSettingItems();

    }

    private void generateSettingItem() {

        final String[] packages = SettingHelper.readPackageName();

        ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, packages);
        packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packageSpin.setAdapter(packageAdapter);
        packageSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentPackage = packages[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> apiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ApiCallCtrl.CtrlApiList);
        apiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apiSpin.setAdapter(apiAdapter);
        apiSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentApi = ApiCallCtrl.CtrlApiList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> policyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, policySet);
        policyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        policySpin.setAdapter(policyAdapter);
        policySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentPolicy = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> infoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, infoSet);
        infoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        infoSpin.setAdapter(infoAdapter);
        infoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentInfo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void generateGlobalSettingItems() {

        final ArrayAdapter<String> globalPolicyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, policySet);
        globalPolicyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        globalPolicySpin.setAdapter(globalPolicyAdapter);
        globalPolicySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentGlobalPolicy = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> globalInfoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, infoSet);
        globalInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        globalInfoSpin.setAdapter(globalInfoAdapter);
        globalInfoSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentGlobalInfo = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setPolicyHelper() {
        if (currentPackage != "" && currentApi != "" && currentPolicy != -1 && currentInfo != -1) {

            String item = currentPackage + " " + currentApi + "," + currentPolicy + "," + currentInfo + "\n";
            SettingHelper.saveSettingFileAppend(item);

        }
    }

    private void setGlobalPolicyHelper() {
        if (currentGlobalInfo != -1 && currentGlobalPolicy != -1) {
            SettingHelper.writeGlobalPolicy(currentGlobalPolicy, currentGlobalInfo);
        }
    }


}
