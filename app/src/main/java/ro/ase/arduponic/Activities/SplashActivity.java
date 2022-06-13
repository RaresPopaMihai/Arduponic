package ro.ase.arduponic.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import ro.ase.arduponic.R;

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView splashAnimation;
    private TextView titleTextView;
    private TextView mottoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        titleTextView = findViewById(R.id.titleTextView);
        mottoTextView = findViewById(R.id.mottoTextView);
        splashAnimation = findViewById(R.id.splashAnimation);
        splashAnimation.setMinAndMaxProgress(0,0.9f);
        Animation animation1 = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        mottoTextView.startAnimation(animation1);
        splashAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Animation fadeAnimation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
                titleTextView.startAnimation(fadeAnimation);
                mottoTextView.startAnimation(fadeAnimation);
                titleTextView.setVisibility(View.VISIBLE);
                mottoTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SplashActivity.this, MasterActivity.class);
                startActivity(intent);
                Animatoo.animateFade(SplashActivity.this);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}