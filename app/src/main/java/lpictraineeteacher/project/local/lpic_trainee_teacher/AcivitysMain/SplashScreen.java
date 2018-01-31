package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
                   chkdb(SplashScreen.this);

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

    public static void chkdb(Context context) {

        String path = "/data/data/lpictraineeteacher.project.local.lpic_trainee_teacher/databases/lpicapp.db";
        File file = new File(path);
//        file.delete();
        // CHECK IS EXISTS OR NOT
        if(!file.exists() ) {
            try {
                // COPY IF NOT EXISTS
                AssetManager am = context.getAssets();
                OutputStream outputStream = new FileOutputStream(path);
                byte[] b = new byte[100];
                int r;
                InputStream is = am.open("lpicapp.db");
                while ((r = is.read(b)) != -1) {
                    outputStream.write(b, 0, r);
                }
                is.close();
                outputStream.close();
            } catch (Exception e) {
                Log.d("error_sqliteservice", e.toString());
            }
        }
    }

}
