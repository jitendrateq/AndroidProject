package com.example.teqforce.veritalabinsurance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.teqforce.veritalabinsurance.app.Config;
import com.example.teqforce.veritalabinsurance.app.MyApplication;
import com.example.teqforce.veritalabinsurance.helper.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewUser extends AppCompatActivity {
    private Toolbar toolbar;
    private PrefManager pref;
    private TextView name, email, mobile;
    private static String TAG = NewUser.class.getSimpleName();
    @InjectView(R.id.input_name) EditText _input_name;
    @InjectView(R.id.input_phone) EditText _input_phone;
    @InjectView(R.id.btn_next) Button _btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name =  (TextView) findViewById(R.id.input_name);
        mobile = (TextView) findViewById(R.id.input_phone);
        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pref = new PrefManager(getApplicationContext());

        _btn_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        if (!pref.isLoggedIn()) {
           // logout();
        }

        _btn_next.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(NewUser.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        final Context context = this;
        String name = _input_name.getText().toString();
        String phone = _input_phone.getText().toString();

        requestForSMS(name, "abc@gmail.com", phone);

        // TODO: Implement your own authentication logic here.
        _btn_next.setEnabled(true);
        progressDialog.dismiss();
        //Intent intent = new Intent(context, VerifyNewUserOTP.class);
        //startActivity(intent);
      /*  new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    private void logout() {
        pref.clearSession();
/*
        Intent intent = new Intent( NewUser.this, VerifyNewUserOTP.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
*/
        finish();
    }

    public void onLoginSuccess() {
        _btn_next.setEnabled(true);
        finish();
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Enter Correct Details", Toast.LENGTH_LONG).show();

        _btn_next.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _input_name.getText().toString();
        String phone = _input_phone.getText().toString();

        if (name.isEmpty()) {
            _input_name.setError("please enter name");
            valid = false;
        } else {
            _input_name.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10 || phone.length() > 10) {
            _input_phone.setError("enter a correct phone no");
            valid = false;
        } else {
            _input_phone.setError(null);
        }

        return valid;
    }

    private void requestForSMS(final String name, final String email, final String mobile) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config.URL_REQUEST_SMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        // boolean flag saying device is waiting for sms
                        pref.setIsWaitingForSms(true);
                        Intent intent = new Intent(NewUser.this, VerifyNewUserOTP.class);
                        startActivity(intent);


                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    // progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    // progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //  progressBar.setVisibility(View.GONE);
            }
        }) {

            /**
             * Passing user parameters to our server
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);

                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

}
