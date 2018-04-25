package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;

public class ResultActivity extends Activity implements Constants {

    private ProgressBar progressBar;
    private TextView tvAnzahlFragenIst;
    private TextView tvAnzahlRichtigIst;
    private TextView tvProzent;
    private Button btnBack;
    private Button btnQuit;


    private int anzahl;
    private int anzahlRichtige;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents();
        initEvents();
        anzahl = getIntent().getExtras().getInt(COUNT_ALL);
        anzahlRichtige = getIntent().getExtras().getInt(COUNT_RIGHT);
        tvAnzahlFragenIst.setText(String.valueOf(anzahl));
        tvAnzahlRichtigIst.setText(String.valueOf(anzahlRichtige));

        double erg = 0;
        if (anzahlRichtige > 0) {
            erg = (double) anzahlRichtige / ((double) anzahl / 100);
        }
        tvProzent.setText(String.format("%4.2f", erg) + " %");

        progressBar.setMax(anzahl);
        progressBar.setProgress(anzahlRichtige);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
                intent.putExtra("QUIT", 0);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
                intent.putExtra("QUIT", 1);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initComponents() {

        tvAnzahlFragenIst = findViewById(R.id.tvAnzahlFragenIst);
        tvAnzahlRichtigIst = findViewById(R.id.tvAnzahlRichtigIst);
        tvProzent = findViewById(R.id.tvProzent);
        progressBar = findViewById(R.id.progressBar);
        btnBack = findViewById(R.id.btnBack);
        btnQuit = findViewById(R.id.btnQuit);
    }
}
