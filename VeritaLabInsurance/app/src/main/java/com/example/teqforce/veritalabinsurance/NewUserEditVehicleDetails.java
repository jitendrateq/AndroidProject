package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class NewUserEditVehicleDetails extends AppCompatActivity {

    Button BtnProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_edit_vehicle_details);

        MoveNextActivity();
    }

    private void MoveNextActivity()
    {
        BtnProceed = (Button) findViewById(R.id.btn_submit);
        final Context context = this;
        Intent intent = new Intent(context, NewUserEditRequest.class);
        startActivity(intent);

    }
}
