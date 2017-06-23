package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewUserSummaryDetails extends AppCompatActivity {

    Button BtnProceed;
    Button BtnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_summary_details);

        MoveNextActivity();
    }

    private void MoveNextActivity()
    {
        BtnProceed = (Button) findViewById(R.id.btn_proceed);
        BtnEdit = (Button) findViewById(R.id.btn_edit);
        final Context context = this;

        BtnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserUploadShareData.class);
                startActivity(intent);
            }
        });

        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                
                Intent intent = new Intent(context, NewUserEditRequest.class);
                startActivity(intent);
            }
        });
    }

}
