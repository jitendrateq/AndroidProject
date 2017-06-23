package com.example.teqforce.veritalabinsurance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserPhonePosition extends AppCompatActivity {

    Button BtnProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_phone_position);
        OnButtonClick();
    }

    public void OnButtonClick()
    {
        BtnProceed=(Button) findViewById(R.id.btn_proceed);
        BtnProceed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View args0){
                Intent intent = new Intent(NewUserPhonePosition.this, NewUserCheckCarFrame.class);
                startActivity(intent);
            }
        });
    }
}
