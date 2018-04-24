package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;

public class ResultActivity extends Activity implements Constants {

    private ProgressBar progressBar;
    private TextView tvAnzahlFragenIst;
    private TextView tvAnzahlRichtigIst;


    private int anzahl;
    private int anzahlRichtige;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents();
        anzahl = getIntent().getExtras().getInt(COUNT_ALL);
        anzahlRichtige = getIntent().getExtras().getInt(COUNT_RIGHT);
        tvAnzahlFragenIst.setText(String.valueOf(anzahl));
        tvAnzahlRichtigIst.setText(String.valueOf(anzahlRichtige));
        progressBar.setMax(anzahl);
        progressBar.setProgress(anzahlRichtige);
    }

    private void initComponents() {

        tvAnzahlFragenIst = findViewById(R.id.tvAnzahlFragenIst);
        tvAnzahlRichtigIst = findViewById(R.id.tvAnzahlRichtigIst);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(20);

    }
}
