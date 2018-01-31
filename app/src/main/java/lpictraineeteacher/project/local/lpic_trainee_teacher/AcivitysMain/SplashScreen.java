package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                try {
                    copyDB();
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } // END Try-Catch-Block
            } // END public void run()
        }; // END NEW THREAD
        splashscreen.start();


    }

    private void copyDB() {
        Context Context = getApplicationContext();
        String destinationFile = "/data/data/lpictraineeteacher.project.local.lpic_trainee_teacher/databases/lpicapp.db";
        File file = new File("/data/data/lpictraineeteacher.project.local.lpic_trainee_teacher/databases/");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!new File(destinationFile).exists()) {
            try {
                CopyFromAssetsToStorage(Context, "lpicapp.db", destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//java.io.FileNotFoundException
    private void CopyFromAssetsToStorage(Context Context, String SourceFile, String DestinationFile) throws IOException {
        InputStream IS = Context.getAssets().open(SourceFile);
        OutputStream OS = new FileOutputStream(DestinationFile);
        CopyStream(IS, OS);
        OS.flush();
        OS.close();
        IS.close();
    }

    private void CopyStream(InputStream Input, OutputStream Output) throws IOException {
        byte[] buffer = new byte[5120];
        int length = Input.read(buffer);
        while (length > 0) {
            Output.write(buffer, 0, length);
            length = Input.read(buffer);
        }
    }
}
