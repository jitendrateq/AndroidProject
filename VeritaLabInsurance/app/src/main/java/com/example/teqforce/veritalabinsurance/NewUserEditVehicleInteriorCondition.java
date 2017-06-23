package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserEditVehicleInteriorCondition extends AppCompatActivity {

    Button btn_Submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_edit_vehicle_interior_condition);

        OnButtonListener();
    }

    private void OnButtonListener() {

        final Context context = this;
        btn_Submit=(Button) findViewById(R.id.btn_Submit);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, NewUserUploadShareData.class);
                startActivity(intent);
            }
        });
    }
}
