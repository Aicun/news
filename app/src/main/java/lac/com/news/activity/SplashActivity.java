package lac.com.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import lac.com.news.R;
import lac.com.news.utils.CacheUtils;

public class SplashActivity extends AppCompatActivity {

    public static final String MAIN_VISITED = "main_visited";
    private RelativeLayout rl_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);

        AlphaAnimation aa = new AlphaAnimation(0,1);
        //aa.setDuration(500);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        //sa.setDuration(500);
        sa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_PARENT,0.5f);
        //ra.setDuration(500);
        ra.setFillAfter(true);

        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);
        as.addAnimation(sa);
        as.addAnimation(ra);
        as.setDuration(2000);

        rl_splash.setAnimation(as);

        as.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //if MainActivity was visited, then visit MainActivity, otherwise visit Indicator
            boolean isMainVisitted = CacheUtils.getBoolean(SplashActivity.this, MAIN_VISITED);
            if(isMainVisitted) {

            }else {
                Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
