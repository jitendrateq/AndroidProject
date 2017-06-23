package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserEditRequest extends AppCompatActivity {

    Button BtnProceed;
    Button btnVehicleDetails;
    Button btnExteriorCondition;
    Button btnInteriorCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_edit_request);

        MoveNextActivity();
    }

    private void MoveNextActivity()
    {
        final Context context = this;
        BtnProceed = (Button) findViewById(R.id.btn_submit);
        btnVehicleDetails = (Button) findViewById(R.id.btnVehicleDetails);
        btnExteriorCondition = (Button) findViewById(R.id.btnExteriorCondition);
        btnInteriorCondition = (Button) findViewById(R.id.btnInteriorCondition);

        BtnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewUserEditVehicleDetails.class);
                startActivity(intent);
            }
        });

        btnVehicleDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewUserEditVehicleDetails.class);
                startActivity(intent);
            }
        });

        btnExteriorCondition.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewUserEditVehicleExteriorCondition.class);
                startActivity(intent);
            }
        });

        btnInteriorCondition.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewUserEditVehicleDetails.class);
                startActivity(intent);
            }
        });
    }
}
