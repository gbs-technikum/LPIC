package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.sql.SQLClientInfoException;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splashscreen = new Thread() {
            @Override
            public void run() {
                try{
                    SqliteService.chkdb(SplashScreen.this);

                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                    startActivity(intent);
                    finish();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } // END Try-Catch-Block
            } // END public void run()
        }; // END NEW THREAD
        splashscreen.start();


    }
}
