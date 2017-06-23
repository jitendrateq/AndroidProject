package com.example.teqforce.veritalabinsurance;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VideoInstructionsGuidelines extends AppCompatActivity {

    TextView _tvCheckPhoto;
    TextView _tvCheckPhotoEngine;
    TextView _tvCheckBlurryPhoto;
    Button BtnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_instructions_guidelines);

        _tvCheckPhoto = (TextView) findViewById( R.id.textViewCheckPhoto );
        _tvCheckPhotoEngine = (TextView) findViewById( R.id.textViewCheckEnginePhoto );
        _tvCheckBlurryPhoto = (TextView) findViewById( R.id.textViewCheckBlurryPhoto );

        String clickText = _tvCheckPhoto.getText().toString();
        //String clickText="Ensure that entire car is within video frame at all times (click to check photo).";
        SpannableString ss = new SpannableString(clickText);
        StyleSpan boldSpan = new StyleSpan( Typeface.BOLD );
        ss.setSpan(new VideoInstructionsGuidelines.MyClickableSpan(), clickText.indexOf("click to check photo"), clickText.indexOf("click to check photo") + String.valueOf("click to check photo").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//0 to 7 Android is clickable
        ss.setSpan(boldSpan, clickText.indexOf("click to check photo"), clickText.indexOf("click to check photo") + String.valueOf("click to check photo").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tvCheckPhoto.setText(ss);
        _tvCheckPhoto.setMovementMethod(LinkMovementMethod.getInstance());

        String clickTextEngine = _tvCheckPhotoEngine.getText().toString();
        //String clickTextEngine="Engine/chassis no should be clearly visible and readable by human eye (click to check photo).";
        SpannableString ssEngine = new SpannableString(clickTextEngine);
        StyleSpan boldSpanEngine = new StyleSpan( Typeface.BOLD );
        ssEngine.setSpan(new VideoInstructionsGuidelines.MyClickableSpan(), 70,91, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//0 to 7 Android is clickable
        ssEngine.setSpan(boldSpanEngine, 70,91, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tvCheckPhotoEngine.setText(ssEngine);
        _tvCheckPhotoEngine.setMovementMethod(LinkMovementMethod.getInstance());

        String clickTextBlurry = _tvCheckBlurryPhoto.getText().toString();
        //String clickTextBlurry="Move slowly to avoid blurry videos (click to check photo).";
        SpannableString ssBlurry = new SpannableString(clickTextBlurry);
        StyleSpan boldSpanBlurry = new StyleSpan( Typeface.BOLD );
        ssBlurry.setSpan(new VideoInstructionsGuidelines.MyClickableSpan(), 36,56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//0 to 7 Android is clickable
        ssBlurry.setSpan(boldSpanBlurry, 38,56, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tvCheckBlurryPhoto.setText(ssBlurry);
        _tvCheckBlurryPhoto.setMovementMethod(LinkMovementMethod.getInstance());

        OnButtonClick();
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {
            //do something
            Toast.makeText(VideoInstructionsGuidelines.this, "Clicked",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            //ds.setColor(Color.BLUE);//set text color
            ds.setUnderlineText(false); // set to false to remove underline
        }
    }

    public void OnButtonClick()
    {
        BtnProceed=(Button) findViewById(R.id.btn_proceed);
        BtnProceed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View args0){
                Intent intent = new Intent(VideoInstructionsGuidelines.this, NewUserPhonePosition.class);
                startActivity(intent);
            }
        });
    }
}
