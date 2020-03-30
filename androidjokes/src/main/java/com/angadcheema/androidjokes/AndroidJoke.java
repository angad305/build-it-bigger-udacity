package com.angadcheema.androidjokes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AndroidJoke extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_android_joke);
    TextView textView = findViewById(R.id.textView);
    Intent intent = getIntent();
    String JokeRecieved = intent.getStringExtra("JOKE");

    if (JokeRecieved != null) {
      textView.setText(JokeRecieved);
    } else {
      textView.setText("No Jokes Available");
    }


  }
}
