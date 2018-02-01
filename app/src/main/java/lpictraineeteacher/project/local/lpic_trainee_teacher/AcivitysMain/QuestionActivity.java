package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.ConstantsBD;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements ConstantsBD {
    private Button btnBack;
    private SqliteService sqliteService;
    private LinearLayout llAnswers;
    private TextView tvHeadline;
    private TextView tvExplaination;
    private TextView tvQuestion;
    private String rubricid;
    private String rubric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        checkForRequest();
        displayAllRecords();
    }

    private void initComponents() {
        tvHeadline = findViewById(R.id.tvHeadline);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvExplaination = findViewById(R.id.tvExplaination);
        llAnswers = findViewById(R.id.llAnswers);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkForRequest() {
        rubricid = getIntent().getExtras().getString(RUBRICID);
        rubric = getIntent().getExtras().getString(RUBRIC);
    }

    private void displayAllRecords() {
        tvHeadline.setText(rubric);
        ArrayList<Question> questions = sqliteService.getAllQuestionRecords(rubricid);
        for (int i = 0; i < 1; i++) {
            Question question = questions.get(i);
            tvQuestion.setText(question.getFrage());
            tvExplaination.setText(question.getHinweis());

            ArrayList<Answer> answers = sqliteService.getAllAnswerRecords(question.getId());
            for (int x = 0; x < answers.size(); x++) {
                Answer answer = answers.get(x);
                final View view = LayoutInflater.from(this).inflate(R.layout.question_answer_checkbox, null);
                final CheckBox chkAnswer = findViewById(R.id.chkAnswer);
                //chkAnswer.setText(answer.getAnswer());
                //chkAnswer.setTag(answer.getId());
                llAnswers.addView(view);
            }
        }
    }
}
