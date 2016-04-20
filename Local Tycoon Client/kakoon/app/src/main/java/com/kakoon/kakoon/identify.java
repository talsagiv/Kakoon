package com.kakoon.kakoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;


public class identify extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    TextView textViewCustom = (TextView) findViewById(R.id.textView);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/Blox.ttf");
        textViewCustom.setTypeface(myCustomFont);



        button=(Button)findViewById(R.id.btnSignUp);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),SignUp.class);
                startActivity(i);
            }
        });
    }
}
