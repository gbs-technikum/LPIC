package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.ConstantsBD;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements ConstantsBD {
    private Button btnBack;
    private LinearLayout llAnswers;
    private SqliteService sqliteService;
    private TextView tvHeadline;
    private TextView tvExplaination;
    private TextView tvFrage;
    private String rubricid;
    private String rubric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
 //       checkForRequest();
        displayAllRecords();
    }

    private void displayAllRecords() {

    }

//    private void checkForRequest() {
//        llAnswers.removeAllViews();
//        llAnswers.addView(tvHeadline);
//        tvHeadline.setText(rubric);
//
//        ArrayList<Question> questions = sqliteService.getAllQuestionRecords(rubricid);
//        for (int i = 0; i < questions.size(); i++) {
//            Question question = questions.get(i);
//        }
//
//        tvFrage.setText(question.getFrage());
//        tvExplaination.setText(question.getHinweis());
//
//        final View view = LayoutInflater.from(this).inflate(R.layout.question_answer, null);
//        view.setTag(question.getId());
//
//
//        llAnswers.addView(view);
//    }


    private void initComponents() {
        llAnswers = findViewById(R.id.llParentLayout);
        tvHeadline = findViewById(R.id.tvHeadline);
        btnBack = findViewById(R.id.btnBack);
        tvFrage = findViewById(R.id.tvFrage);
        tvExplaination = findViewById(R.id.tvExplaination);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
