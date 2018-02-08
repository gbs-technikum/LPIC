package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements Constants {
    private Button btnBack;
    private Button btnPrev;
    private Button btnNext;
    private Button btnCheck;
    private SqliteService sqliteService;
    private LinearLayout llAnswers;
    private TextView tvHeadline;
    private TextView tvExplaination;
    private TextView tvQuestion;
    private String rubricid;
    private String categoryid;
    private ArrayList<Question> questions;
    private Question question;
    private ArrayList<Answer> answers;
    private int index;
    private String listtype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initComponents();
        initEvents();
        checkForRequest();
        createQuestionList();
        displayQuestion(0);
    }

    private void checkForRequest() {
        listtype = getIntent().getStringExtra(LISTTYPE);
        if (listtype.equals(LISTRUBRIC)) {
            rubricid = getIntent().getExtras().getString(RUBRICID);
            tvHeadline.setText(getIntent().getExtras().getString(RUBRIC));
        } else {
            categoryid = getIntent().getExtras().getString(CATEGORYID);
            tvHeadline.setText("");
        }
    }


    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        tvHeadline = findViewById(R.id.tvHeadline);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvExplaination = findViewById(R.id.tvExplaination);
        llAnswers = findViewById(R.id.llAnswers);
        btnBack = findViewById(R.id.btnBack);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnCheck = findViewById(R.id.btnCheck);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkQuestion();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });
    }

    private void createQuestionList() {
        if (listtype.equals(LISTRUBRIC)) {
            questions = sqliteService.getAllQuestionRecords(rubricid);
        } else {
            //ToDo TestList
        }
        index = -1;
        if (questions.size() > 0) {
            index = 0;
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                answers = sqliteService.getAllAnswerRecords(question.getId());
                question.setAnswers(answers);
            }
        }
    }

    private void nextQuestion() {
        if (index > -1 && index < questions.size() - 1) {
            index++;
            displayQuestion(index);
        }
    }

    private void prevQuestion() {
        if (questions.size() > 0 && index > 0) {
            index--;
            displayQuestion(index);
        }
        tastaturAusblenden();
    }

    private void checkQuestion() {
        // Erklärung anzeigen
        tvExplaination.setVisibility(View.VISIBLE);
        if (question.getArt().equals(TYPETEXT)) {
            // tvRightAnswer ist nur vorhanden bei question.art == TYPETEXT
            TextView tvRightAnswer = findViewById(R.id.tvRightAnswer);
            if (tvRightAnswer != null) {
                EditText etAnswer = findViewById(R.id.etAnswer);
                if (!etAnswer.getText().toString().equals(tvRightAnswer.getText().toString())) {
                    etAnswer.setTextColor(Color.RED);
                    tvRightAnswer.setVisibility(View.VISIBLE);
                } else {
                    etAnswer.setTextColor(Color.GREEN);
                }
            }
        } else if (question.getArt().equals(TYPECHECK)) {
            for (Answer answer : question.getAnswers()) {
                if (!answer.getTruefalse().equals(answer.getResponse())) {

                }
            }
            for (int i = 0; i < llAnswers.getChildCount(); i++) {
                View v = llAnswers.getChildAt(i);
                if (v instanceof CheckBox) {
                    ((CheckBox) v).setTextColor(Color.RED);
                }
            }
        }
        tastaturAusblenden();
    }


    private void tastaturAusblenden() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void displayQuestion(int index) {

        tvExplaination.setVisibility(View.GONE);
        llAnswers.removeAllViews();
        if (questions.size() > 0) {
            question = questions.get(index);
            tvQuestion.setText(question.getFrage());
            tvExplaination.setText(question.getHinweis());

            answers = question.getAnswers();
            if (answers.size() > 0) {
                if (question.getArt().equals(TYPECHECK)) {
                    for (int x = 0; x < answers.size(); x++) {
                        final Answer answer = answers.get(x);
                        final CheckBox ckAnswer = new CheckBox(llAnswers.getContext());
                        ckAnswer.setId(x);
                        ckAnswer.setText(answer.getAnswer());
                        ckAnswer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ckAnswer.isChecked()) {
                                    answer.setResponse(ISTRUE);
                                } else {
                                    answer.setResponse(ISFALSE);
                                }
                            }
                        });
                        llAnswers.addView(ckAnswer);
                    }
                } else {
                    for (int x = 0; x < answers.size(); x++) {
                        Answer answer = answers.get(x);
                        final EditText etAnswer;
                        final TextView tvRightAnswer;
                        final View view = LayoutInflater.from(this).inflate(R.layout.question_answer_text, null);
                        etAnswer = view.findViewById(R.id.etAnswer);
                        tvRightAnswer = view.findViewById(R.id.tvRightAnswer);
                        tvRightAnswer.setVisibility(View.GONE);
                        tvRightAnswer.setText(answer.getAnswer());
                        etAnswer.setText("");
                        llAnswers.addView(view);
                    }
                }
            }
        }
    }

//
//    private void displayQuestion(int index) {
//
//        tvExplaination.setVisibility(View.GONE);
//        llAnswers.removeAllViews();
//        if (questions.size() > 0) {
//            question = questions.get(index);
//            tvQuestion.setText(question.getFrage());
//            tvExplaination.setText(question.getHinweis());
//
//            answers = question.getAnswers();
//            if (answers.size() > 0) {
//                if (question.getArt().equals(TYPECHECK)) {
//                    for (int x = 0; x < answers.size(); x++) {
//                        final Answer answer = answers.get(x);
//                        final CheckBox ckAnswer;
//                        final View view = LayoutInflater.from(this).inflate(R.layout.question_answer_checkbox, null);
//                        ckAnswer = view.findViewById(R.id.ckAnswer);
//                        ckAnswer.setText(answer.getAnswer());
//                        ckAnswer.setId(x);
//                        ckAnswer.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (ckAnswer.isChecked()) {
//                                    answer.setResponse(ISTRUE);
//                                } else {
//                                    answer.setResponse(ISFALSE);
//                                }
//                            }
//                        });
//                        llAnswers.addView(view);
//                    }
//                } else {
//                    for (int x = 0; x < answers.size(); x++) {
//                        Answer answer = answers.get(x);
//                        final EditText etAnswer;
//                        final TextView tvRightAnswer;
//                        final View view = LayoutInflater.from(this).inflate(R.layout.question_answer_text, null);
//                        etAnswer = view.findViewById(R.id.etAnswer);
//                        tvRightAnswer = view.findViewById(R.id.tvRightAnswer);
//                        tvRightAnswer.setVisibility(View.GONE);
//                        tvRightAnswer.setText(answer.getAnswer());
//                        etAnswer.setText("");
//                        llAnswers.addView(view);
//                    }
//                }
//            }
//        }
//    }
//
}

