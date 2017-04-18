package br.com.brunosemfio.homeminvisivel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        findViewById(R.id.intro_btn_start).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        GAUtils.setScreenName(this, "Intro");
    }
}