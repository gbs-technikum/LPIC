package lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData;

import android.app.Activity;
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
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionNewActivity extends Activity implements Constants {

    private EditText etFrage;
    private EditText etErklaerung;
    private TextView tvArt;
    private Button btnDML;
    private Button btnBack;
    private RadioGroup rgArt;
    private RadioButton rbCheckoption;
    private RadioButton rbTextoption;
    private String questionID;
    private String rubricID;
    private String request;
    private SqliteService sqliteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdquestion_new);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        request = getIntent().getStringExtra(DML_TYPE);
        if (request.equals(UPDATE)) {
            questionID = getIntent().getStringExtra(QUESTIONID);
            btnDML.setText(R.string.update);
            rgArt.setVisibility(View.GONE);
            readQuestion(questionID);

        } else {
            tvArt.setVisibility(View.GONE);
            btnDML.setText(R.string.insert);
            rubricID = getIntent().getStringExtra(RUBRICID);
            questionID = UUID.randomUUID().toString();
        }
    }

    private void readQuestion(String questionID) {
        Question question = sqliteService.getQuestionRecord(questionID);
        String art = question.getArt();
        if (art.equals(TYPETEXT)) {
            tvArt.setText(R.string.textantwortfrage);
            rbTextoption.setChecked(true);
        } else {
            tvArt.setText(R.string.checkboxantwortfrage);
            rbCheckoption.setChecked(true);
        }
        etFrage.setText(question.getFrage());
        etErklaerung.setText(question.getHinweis());
        rubricID = question.getRid();
    }

    private void initEvents() {
        btnDML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnDMLClick();
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
        sqliteService = SqliteService.getInstance(this);
        etFrage = findViewById(R.id.etFrage);
        etErklaerung = findViewById(R.id.etErklaerung);
        tvArt = findViewById(R.id.tvArt);
        btnDML = findViewById(R.id.btnDML);
        btnBack = findViewById(R.id.btnBack);
        rgArt = findViewById(R.id.rgArt);
        rbCheckoption = findViewById(R.id.rbCheckoption);
        rbTextoption = findViewById(R.id.rbTextoption);
    }

    private void onBtnDMLClick() {
        if (etFrage.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.emptyfield, Toast.LENGTH_LONG).show();
        } else {

            Question question = new Question();
            question.setId(questionID);
            question.setRid(rubricID);
            question.setFrage(etFrage.getText().toString());
            question.setHinweis(etErklaerung.getText().toString());

            int checkedRadioButtonId = rgArt.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.rbTextoption) {
                question.setArt(TYPETEXT);
            } else if (checkedRadioButtonId == R.id.rbCheckoption) {
                question.setArt(TYPECHECK);
            }
            if (request.equals(INSERT)) {
                sqliteService.insertQuestionRecord(question);
            } else if (request.equals(UPDATE)) {
                sqliteService.updateQuestionRecord(question);
            }
            finish();
        }
    }
}
