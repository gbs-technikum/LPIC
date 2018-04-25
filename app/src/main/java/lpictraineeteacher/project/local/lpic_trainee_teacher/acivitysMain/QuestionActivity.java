package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements Constants {
    private Button btnPrev;
    private Button btnNext;
    private Button btnCheck;
    private Button btnResult;
    private ImageButton btnInfo;
    private ImageButton btnGlossary;
    private ProgressBar progressBar;
    private SqliteService sqliteService;
    private LinearLayout llAnswers;
    private TextView tvExplaination;
    private TextView tvQuestion;
    private TextView tvTimer;
    private TextView tvQuestionNr;
    private String rubricid;
    private String categoryid;
    private ArrayList<Question> questions;
    private Question question;
    private ArrayList<Answer> answers;
    private int index;
    private String listtype;
    private int anzahlRichtige;
    private boolean isCheckResultAndShowCalled;
    private CountDownTimer countDownTimer;
    private long timeInMilliSec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initComponents();
        initEvents();
        checkForRequest();
        createQuestionList();
        displayQuestion(0);
        anzahlRichtige = 0;
        isCheckResultAndShowCalled = false;
        if (questions.size() > 0) {
            timeInMilliSec = questions.size() * 60 * 1000;
            initTimer(timeInMilliSec);
        }
        btnPrev.setEnabled(false);
    }

    private void checkForRequest() {
        listtype = getIntent().getStringExtra(LISTTYPE);
        if (listtype.equals(LISTRUBRIC)) {
            rubricid = getIntent().getExtras().getString(RUBRICID);
            this.setTitle(getIntent().getExtras().getString(RUBRIC));
            btnCheck.setVisibility(View.VISIBLE);
            tvTimer.setVisibility(View.GONE);
        } else if (listtype.equals(LISTCATEGORY)) {
            categoryid = getIntent().getExtras().getString(CATEGORYID);
            btnCheck.setVisibility(View.GONE);
            tvTimer.setVisibility(View.VISIBLE);
            this.setTitle(R.string.activitytestheadline);

        }
        
    }

    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNr = findViewById(R.id.tvQuestionNr);
        tvTimer = findViewById(R.id.tvTimer);
        tvExplaination = findViewById(R.id.tvExplaination);
        llAnswers = findViewById(R.id.llAnswers);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnCheck = findViewById(R.id.btnCheck);
        btnResult = findViewById(R.id.btnResult);
        btnInfo = findViewById(R.id.btnInfo);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initEvents() {
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResultAndShow();

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
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initTimer(long timeInMilliSec) {

        countDownTimer = new CountDownTimer(timeInMilliSec + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                Integer secs = (int) millisUntilFinished / 1000;
                Integer minutes = secs / 60;
                Integer seconds = secs % 60;
                tvTimer.setText("noch " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + " min");
            }

            public void onFinish() {
                tvTimer.setText("noch 0:00 min");
                checkResultAndShow();
            }
        }.start();


    }

    private void checkResultAndShow() {
        if (questions.size() > 0) {
            if (!isCheckResultAndShowCalled) {
                for (int i = 0; i < questions.size(); i++) {
                    displayQuestion(i);
                    checkQuestion();
                    if (question.getAnswerIsRight().equals(ISTRUE)) {
                        anzahlRichtige++;
                    }
                }
                index = 0;
                displayQuestion(index);
                isCheckResultAndShowCalled = true;
            }
            countDownTimer.cancel();
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            intent.putExtra(COUNT_ALL, questions.size());
            intent.putExtra(COUNT_RIGHT, anzahlRichtige);
            startActivityForResult(intent,101);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Bundle intent = data.getExtras();
            int ret =  intent.getInt("QUIT");
            if (ret == 1) {
                finish();
            }

        }
    }

    private void createQuestionList() {
        if (listtype.equals(LISTRUBRIC)) {
            questions = sqliteService.getAllQuestionRecords(rubricid);
        } else if (listtype.equals(LISTCATEGORY)) {

            ArrayList<Question> allQuestions = sqliteService.getAllCategoryQuestionRecords(categoryid);
            questions = new ArrayList<>();
            int allQuestionsSize = allQuestions.size();
            int questionsSize = 20;
            if (allQuestionsSize < questionsSize) {
                questionsSize = allQuestionsSize;
            }
            Random random = new Random();
            for (int i = 0; i < questionsSize; i++) {
                int n = random.nextInt(allQuestions.size());
                questions.add(allQuestions.get(n));
                allQuestions.remove(n);
            }
            allQuestions = null;
        }
        index = -1;
        progressBar.setMax(questions.size());
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
        if (questions.size() > 0 && index == questions.size() - 1) {
            btnNext.setEnabled(false);
        }
        btnPrev.setEnabled(true);
    }

    private void prevQuestion() {
        if (questions.size() > 0 && index > 0) {
            index--;
            displayQuestion(index);
        }
        if (questions.size() > 0 && index == 0) {
            btnPrev.setEnabled(false);
        }
        btnNext.setEnabled(true);
    }

    private void checkQuestion() {
        if (questions.size() > 0) {
            // Erklärung anzeigen
            tvExplaination.setVisibility(View.VISIBLE);
            // Textantwortfrage
            if (question.getArt().equals(TYPETEXT)) {
                // tvRightAnswer ist nur vorhanden bei question.art == TYPETEXT
                TextView tvRightAnswer = findViewById(R.id.tvRightAnswer);
                if (tvRightAnswer != null) {
                    EditText etAnswer = findViewById(R.id.etAnswer);
                    if (!etAnswer.getText().toString().equals(tvRightAnswer.getText().toString())) {
                        etAnswer.setTextColor(Color.RED);
                        tvRightAnswer.setVisibility(View.VISIBLE);
                        question.setAnswerIsRight(ISFALSE);
                    } else {
                        etAnswer.setTextColor(Color.GREEN);
                        question.setAnswerIsRight(ISTRUE);
                    }
                    etAnswer.setEnabled(false);
                    answers.get(0).setResponse(etAnswer.getText().toString());
                }
                // Checkboxfrage
            } else if (question.getArt().equals(TYPECHECK)) {
                question.setAnswerIsRight(ISTRUE);
                for (int i = 0; i < answers.size(); i++) {
                    CheckBox cb = findViewById(i);
                    int color = checkboxColor(answers.get(i), cb.getCurrentTextColor());
                    cb.setTextColor(color);
                    if (color == Color.RED) {
                        question.setAnswerIsRight(ISFALSE);
                    }
                    cb.setEnabled(false);
                }
            }
            question.setVerified(ISTRUE);
            btnCheck.setEnabled(false);
            tastaturAusblenden();
        }
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
        btnCheck.setEnabled(true);
        progressBar.setProgress(index + 1);
        if (questions.size() > 0) {
            question = questions.get(index);
            tvQuestionNr.setText("Frage " + String.valueOf(index + 1) + " von " + String.valueOf(questions.size()));
            tvQuestion.setText(question.getFrage());
            tvExplaination.setText(question.getHinweis());
            answers = question.getAnswers();
            if (answers.size() > 0) {
                // Checkboxfrage
                if (question.getArt().equals(TYPECHECK)) {
                    for (int x = 0; x < answers.size(); x++) {
                        final Answer answer = answers.get(x);
                        final CheckBox ckAnswer = new CheckBox(llAnswers.getContext());
                        ckAnswer.setId(x);
                        ckAnswer.setText(answer.getAnswer());
                        ckAnswer.setTextSize(18);
                        ckAnswer.setPadding(0,0,0,4);
                        if (answer.getResponse().equals(ISTRUE)) {
                            ckAnswer.setChecked(true);
                        } else {
                            answer.setResponse(ISFALSE);
                        }
                        if (question.getVerified().equals(ISTRUE)) {
                            ckAnswer.setTextColor(checkboxColor(answer, ckAnswer.getCurrentTextColor()));
                            ckAnswer.setEnabled(false);
                            tvExplaination.setVisibility(View.VISIBLE);
                            btnCheck.setEnabled(false);
                        }

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
                    // Textantwortfrage
                } else {
                    for (int x = 0; x < answers.size(); x++) {
                        final Answer answer = answers.get(x);
                        View view = LayoutInflater.from(this).inflate(R.layout.question_answer_text, null);
                        final EditText etAnswer = view.findViewById(R.id.etAnswer);
                        etAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {

                                } else {
                                    answer.setResponse(etAnswer.getText().toString());
                                }
                            }
                        });
                        TextView tvRightAnswer = view.findViewById(R.id.tvRightAnswer);
                        tvRightAnswer.setText(answer.getAnswer());
                        etAnswer.setText(answer.getResponse());
                        if (question.getVerified().equals(ISTRUE)) {
                            if (!etAnswer.getText().toString().equals(tvRightAnswer.getText().toString())) {
                                etAnswer.setTextColor(Color.RED);
                                tvRightAnswer.setVisibility(View.VISIBLE);
                            } else {
                                etAnswer.setTextColor(Color.GREEN);
                            }
                            etAnswer.setEnabled(false);
                            btnCheck.setEnabled(false);
                            tvExplaination.setVisibility(View.VISIBLE);
                        } else {
                            tvRightAnswer.setVisibility(View.GONE);
                        }
                        llAnswers.addView(view);
                    }
                }
            }
        }
    }

    private int checkboxColor(Answer answer, int color) {
        if (!answer.getTruefalse().equals(answer.getResponse())) {
            return Color.RED;
        }
        if (answer.getTruefalse().equals(ISTRUE)) {
            return Color.GREEN;
        }
        return color;
    }
}


