package in.vmc.mconnecttab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import in.vmc.mconnecttab.R;
import in.vmc.mconnecttab.utils.Utils;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welocme);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isLogin(Welcome.this)) {
                    Intent intent = new Intent(new Intent(Welcome.this, Home.class));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
                    //  Log.d("LOG", "User is Logged in");
                } else {
                    Log.d("LOG", "User is not Logged in");

                    Intent intent = new Intent(new Intent(Welcome.this, Login.class));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.entryanim, R.anim.exitanim);
                    //  overridePendingTransition(0, 0);

                }

            }
        }, 800);


    }

}
