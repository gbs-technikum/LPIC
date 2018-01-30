package lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class AnswerNewBDActivity extends Activity implements ConstantsBD {

    private Answer answer;
    private Question question;
    private TextView tvFrage;
    private EditText etAntwort;
    private RadioGroup rgRichtigFalsch;
    private RadioButton rbRichtig;
    private RadioButton rbFalsch;
    private Button btnDML;
    private Button btnBack;
    private String request;
    private SqliteService sqliteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdanswer_new);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        request = getIntent().getStringExtra(DML_TYPE);
        if (request.equals(UPDATE)) {
            readAnswer(getIntent().getStringExtra(ANSWERID));
            readQuestion(answer.getFid());
            showData();
        } else {
            answer = new Answer();
            readQuestion(getIntent().getStringExtra(QUESTIONID));
            showData();
        }
    }
    private void readQuestion(String questionID) {
        question = sqliteService.getQuestionRecord(questionID);
    }

    private void readAnswer(String answerID) {
        answer = sqliteService.getAnswerRecord(answerID);
    }

    private void writeData() {
        int rbRichtigFalsch = rgRichtigFalsch.getCheckedRadioButtonId();
        String isToF = ISTRUE;
        if (rbRichtigFalsch == R.id.rbFalsch) {
            isToF = ISFALSE;
        }
        answer.setTruefalse(isToF);
        answer.setAnswer(etAntwort.getText().toString());
        if (request.equals(INSERT)) {
            answer.setId(UUID.randomUUID().toString());
            answer.setFid(question.getId());
            sqliteService.insertAnswerRecord(answer);
        } else {
            sqliteService.updateAnswerRecord(answer);
        }
    }

    private void showData() {
        String isToF = answer.getTruefalse();
        tvFrage.setText(question.getFrage());
        etAntwort.setText(answer.getAnswer());
        if (question.getArt().equals(TYPETEXT)) {
            rgRichtigFalsch.setVisibility(View.INVISIBLE);
            isToF = ISTRUE;
        }
        if (isToF.equals(ISTRUE)) {
            rbRichtig.setChecked(true);
        } else {
            rbFalsch.setChecked(true);
        }
    }

    private void initEvents() {
        btnDML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponents() {
        etAntwort = findViewById(R.id.etAntwort);
        tvFrage = findViewById(R.id.tvFrage);
        btnDML = findViewById(R.id.btnDML);
        btnBack = findViewById(R.id.btnBack);
        rgRichtigFalsch = findViewById(R.id.rgRichtigFalsch);
        rbFalsch = findViewById(R.id.rbFalsch);
        rbRichtig = findViewById(R.id.rbRichtig);
    }

    private void onButtonClick() {
        if (etAntwort.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.emptyfield, Toast.LENGTH_LONG).show();
        } else {
            writeData();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
