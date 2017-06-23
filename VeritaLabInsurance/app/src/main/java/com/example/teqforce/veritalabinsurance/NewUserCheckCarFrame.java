package com.example.teqforce.veritalabinsurance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewUserCheckCarFrame extends AppCompatActivity {

    Button BtnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_check_car_frame);

        OnProceedButtonClick();
    }

    public void OnProceedButtonClick()
    {
        BtnProceed=(Button) findViewById(R.id.btn_proceed);
        BtnProceed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View args0){
                Intent intent = new Intent(NewUserCheckCarFrame.this, NewUserSampleEngineImage.class);
                startActivity(intent);
            }
        });
    }
}
