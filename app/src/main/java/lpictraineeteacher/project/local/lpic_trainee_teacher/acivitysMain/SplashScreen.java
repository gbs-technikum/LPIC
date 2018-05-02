package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;


public class SplashScreen extends Activity {

    private Button btnGerman;
    private Button btnEnglish;
    private ImageButton ibDBCopy;
    private int clickcounter;
    private String destinationFile;
    private File file;
    private Context context;
    private String dbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dbName = "lpicapp.db";
        file = new File("/data/data/lpictraineeteacher.project.local.lpic_trainee_teacher/databases/");
        destinationFile = file + dbName;
        context = getApplicationContext();
        clickcounter = 0;
        copyDB();
        initComponents();
        initEvents();
    }

    private void initComponents() {
        btnEnglish = findViewById(R.id.btnEnglish);
        btnGerman = findViewById(R.id.btnGerman);
        ibDBCopy = findViewById(R.id.ibDBCopy);
    }

    private void initEvents() {
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("en");
            }
        });
        btnGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage("de");
            }
        });
        ibDBCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickcounter++;
                if (clickcounter == 5) {
                    overwriteDB();
                    Toast.makeText(context, "DB überschrieben", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setLanguage(String language) {
        String languageToLoad = "en";
        if (language.equals("de")) {
            languageToLoad = "de";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void copyDB() {
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!new File(destinationFile).exists()) {
            try {
                CopyFromAssetsToStorage(context, "lpicapp.db", destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void overwriteDB() {
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            CopyFromAssetsToStorage(context, dbName, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
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
