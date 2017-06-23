package com.example.teqforce.veritalabinsurance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDetails extends AppCompatActivity {

    private Spinner spin_profile;
    private Button btnNewInspection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_user_account_details);
        spin_profile = (Spinner) findViewById(R.id.profile_spinner);
        /*spin_profile.setOnItemSelectedListener(new OnItemSelectedListener() {

        });*/
        addProfile();

        btnNewInspection=(Button)findViewById(R.id.btnNewIns);
    }
    public void addProfile()
    {
        spin_profile = (Spinner) findViewById(R.id.profile_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Customer");
        list.add("Agent");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin_profile.setAdapter(dataAdapter);
    }
}
