package com.example.teqforce.veritalabinsurance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.lang.String;

public class selectInspectionId extends AppCompatActivity {

    Button backbtn;
    Button proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inspection_id);
        Spinner mySpinner = (Spinner)findViewById(R.id.inspid_spinner);

        addListenerOnButton();

        // call method to fill spinner and uncomment below code

      /*  int selectedItemOfMySpinner = mySpinner.getSelectedItemPosition();
        String actualPositionOfMySpinner = (String) mySpinner.getItemAtPosition(selectedItemOfMySpinner);

        if (actualPositionOfMySpinner.isEmpty()) {
            String errmsg="field can't be empty";
            setSpinnerError(mySpinner,errmsg);
        }*/

    }

    public void addListenerOnButton() {

        final Context context = this;
        backbtn = (Button) findViewById(R.id.btnNewuser);
        proceedBtn = (Button) findViewById(R.id.btnExistingUser);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ExistingUserLogin.class);
                startActivity(intent);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }
}
