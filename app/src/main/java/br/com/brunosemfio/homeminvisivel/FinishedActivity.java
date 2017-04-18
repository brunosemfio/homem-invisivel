package br.com.brunosemfio.homeminvisivel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishedActivity extends AppCompatActivity {

    public static final String FOUND = "FOUND";

    public static final String HITS = "HITS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finished);

        boolean found = getIntent().getBooleanExtra(FOUND, false);

        final int totalHits = getIntent().getIntExtra(HITS, 0);

        ((TextView) findViewById(R.id.finished_message)).setText(found ? getString(R.string.txt_finished_found) : getString(R.string.txt_finished_not_found));
        ((TextView) findViewById(R.id.finished_hits)).setText(found ? getString(R.string.txt_finished_hits, totalHits) : getString(R.string.txt_finished_none_hits));

        Button btnShare = (Button) findViewById(R.id.finished_btn_share);
        btnShare.setVisibility(found ? View.VISIBLE : View.GONE);
        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Achei o Menino do Acre em " + totalHits + " toque(s). Baixe o app: https://play.google.com/store/apps/details?id=br.com.brunosemfio.homeminvisivel&hl=pt-BR");
                intent.setType("text/plain");

                startActivity(Intent.createChooser(intent, null));
            }
        });

        findViewById(R.id.finished_btn_play).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(FinishedActivity.this, MainActivity.class));

                finish();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        GAUtils.setScreenName(this, "Final");
    }
}