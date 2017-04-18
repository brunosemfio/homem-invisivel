package br.com.brunosemfio.homeminvisivel;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MARGIN = 30;

    private static final int TOLERANCE = 30;

    private int hitsRemains = 50;

    private int totalHits = 0;

    private boolean found;

    private MaxSize maxSize;

    private double maxDistance;

    private double randomX, randomY;

    private TextView hits, tip, explanation;

    private ImageView statue;

    private int showStatueWhen;

    private SoundPool soundPool;

    private int soundPoolPop, soundPoolToasty;

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupViews();

        setupScreen();

        initSoundPool();

        setupAd();

        showStatueWhen = new Random().nextInt(7) + 3;
    }

    private void setupViews() {

        hits = (TextView) findViewById(R.id.main_hits);

        tip = (TextView) findViewById(R.id.main_tip);

        explanation = (TextView) findViewById(R.id.main_explanation);

        statue = (ImageView) findViewById(R.id.main_statue);
    }

    private void setupScreen() {

        maxSize = ScreenUtils.getMaxSize(this);

        maxDistance = Math.hypot(0 - maxSize.width, 0 - maxSize.height);

        randomX = MARGIN + (Math.random() * (maxSize.width - MARGIN));
        randomY = MARGIN + (Math.random() * (maxSize.height - MARGIN));
    }

    private void initSoundPool() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();

        } else {

            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        soundPoolPop = soundPool.load(this, R.raw.pop, 1);

        soundPoolToasty = soundPool.load(this, R.raw.toasty, 1);
    }

    private void setupAd() {

        final AdRequest request = new AdRequest.Builder().build();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1502214236956122/1274212290");
        interstitialAd.loadAd(request);
        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {

                Log.v(MainActivity.class.getSimpleName(), "Não foi possível carregar o anúncio");
            }

            @Override
            public void onAdClosed() {

                finishGame();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {

            makeSound();

            explanation.setVisibility(View.GONE);

            final double distance = Math.hypot(randomX - event.getX(), randomY - event.getY());

            final float proportion = (float) (distance / maxDistance);

            totalHits += 1;

            found = distance <= TOLERANCE;

            checkHit();

            changeBgColor(proportion);

            showMessages(proportion);
        }

        return super.onTouchEvent(event);
    }

    private void makeSound() {

        if (hitsRemains != showStatueWhen) {

            soundPool.play(soundPoolPop, 1, 1, 1, 0, 0);

        } else {

            soundPool.play(soundPoolToasty, 1, 1, 1, 0, 0);

            showStatue();
        }
    }

    private void showStatue() {

        statue.setVisibility(View.VISIBLE);
        statue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Dica do Giordano Bruno")
                        .setMessage(getTip())
                        .setPositiveButton("Vlw, Brunão", null)
                        .show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                statue.setVisibility(View.GONE);

            }
        }, 1000);
    }

    private String getTip() {

        final String s1 = randomY < maxSize.height / 2 ? "superior" : "inferior";

        final String s2 = randomX < maxSize.width / 2 ? "esquedo" : "direito";

        return getString(R.string.txt_main_tip, s1, s2);
    }

    private void checkHit() {

        if (hitsRemains > 1 && !found) {

            hitsRemains -= 1;

            return;
        }

        if (interstitialAd.isLoaded()) {

            interstitialAd.show();

        } else {

            finishGame();
        }

    }

    private void finishGame() {

        final Intent intent = new Intent(this, FinishedActivity.class);
        intent.putExtra(FinishedActivity.HITS, totalHits);
        intent.putExtra(FinishedActivity.FOUND, found);

        startActivity(intent);

        finish();
    }

    private void changeBgColor(float proportion) {

        getWindow().getDecorView().setBackgroundColor((int) new ArgbEvaluator().evaluate(
                proportion, ContextCompat.getColor(this, R.color.quente), ContextCompat.getColor(this, R.color.frio)));
    }

    private void showMessages(float proportion) {

        hits.setText(String.valueOf(hitsRemains));

        if (proportion < 0.15) {

            tip.setText(R.string.txt_main_too_hot);

        } else if (proportion >= 0.15 && proportion < 0.30) {

            tip.setText(R.string.txt_main_hot);

        } else if (proportion >= 0.30 && proportion < 0.60) {

            tip.setText(R.string.txt_main_cold);

        } else {

            tip.setText(R.string.txt_main_too_cold);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        GAUtils.setScreenName(this, "Principal");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        soundPool.release();
    }
}
