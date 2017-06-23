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

public class VideoInstructionsSteps extends AppCompatActivity {

    Button BtnProceed;
    TextView _tvClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_instructions_steps);
        _tvClick = (TextView) findViewById( R.id.textViewClickPhoto );
        String clickText="Place phone in landscape position(check photo)";
        SpannableString ss = new SpannableString(clickText);
        StyleSpan boldSpan = new StyleSpan( Typeface.BOLD );
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ss.setSpan(new MyClickableSpan(), clickText.indexOf("check photo"), clickText.indexOf("check photo") + String.valueOf("check photo").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//0 to 7 Android is clickable
        ss.setSpan(boldSpan, clickText.indexOf("check photo"), clickText.indexOf("check photo") + String.valueOf("check photo").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        _tvClick.setText(ss);
        _tvClick.setMovementMethod(LinkMovementMethod.getInstance());
        OnButtonClick();
    }

    public void OnButtonClick()
    {
        BtnProceed=(Button) findViewById(R.id.btn_proceed);
        BtnProceed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View args0){
                Intent intent = new Intent(VideoInstructionsSteps.this, VideoInstructionsGuidelines.class);
                startActivity(intent);
            }
        });
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(View textView) {
            //do something
            Toast.makeText(VideoInstructionsSteps.this, "Clicked",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            //ds.setColor(Color.BLUE);//set text color
            ds.setUnderlineText(false); // set to false to remove underline
        }
    }
}
