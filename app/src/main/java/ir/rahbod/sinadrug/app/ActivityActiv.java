package ir.rahbod.sinadrug.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityActiv extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CardView cardView;
    LinearLayout linearTxt;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activ);

        relativeLayout = findViewById(R.id.relativeLayout);
        cardView = findViewById(R.id.cardView);
        linearTxt = findViewById(R.id.linearTxt);
        txtLogin = findViewById(R.id.txtLogin);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Animation animationToDown = AnimationUtils.loadAnimation(ActivityActiv.this, R.anim.to_down_);
        relativeLayout.setAnimation(animationToDown);

        final Animation animatorFade = AnimationUtils.loadAnimation(ActivityActiv.this, R.anim.fade_anim);
        animatorFade.setStartOffset(200);
        animatorFade.setDuration(500);
        cardView.setAnimation(animatorFade);
        linearTxt.setAnimation(animatorFade);


    }
}
