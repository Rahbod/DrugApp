package ir.rahbod.sinadrug.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ActivityLogin extends AppCompatActivity {

    CardView cardView;
    Button btnActivation, btnLogin;
    Boolean fadeLogo = false;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cardView = findViewById(R.id.cardView);
        btnActivation = findViewById(R.id.btnActivation);
        btnLogin = findViewById(R.id.btnLogin);
        relativeLayout = findViewById(R.id.relativeLayout);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityLog.class);
                startActivity(intent);
            }
        });

        if (!fadeLogo) {
            final Animation animatorFade = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
            cardView.setAnimation(animatorFade);
        }
        fadeLogo = true;

        btnActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    shared_element();
                else{
                    Intent intent = new Intent(ActivityLogin.this, ActivityActiv.class);
                    startActivity(intent);
                }

            }
        });

        animBtnActivation();
        animBtnLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        animBtnActivation();
        animBtnLogin();
    }

    private void animBtnLogin() {
        Button btnLogin = findViewById(R.id.btnLogin);
        final Animation animatorToUp700 = AnimationUtils.loadAnimation(this, R.anim.to_up_900);
        btnLogin.setAnimation(animatorToUp700);

    }

    private void animBtnActivation() {
        Button btnActivation = findViewById(R.id.btnActivation);
        final Animation animatorToUp1000 = AnimationUtils.loadAnimation(this, R.anim.to_up_600);
        btnActivation.setAnimation(animatorToUp1000);
    }

    public void shared_element() {
        Intent intent = new Intent(this, ActivityActiv.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = Pair.create(cardView, "logoTrans");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            startActivity(intent, options.toBundle());
        }
    }
}
