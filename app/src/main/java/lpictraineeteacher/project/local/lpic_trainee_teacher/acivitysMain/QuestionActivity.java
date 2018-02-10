package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements Constants {
    private Button btnBack;
    private Button btnPrev;
    private Button btnNext;
    private Button btnCheck;
    private Button btnResult;
    private Button btnGlossary;
    private ProgressBar progressBar;
    private SqliteService sqliteService;
    private LinearLayout llAnswers;
    private TextView tvExplaination;
    private TextView tvQuestion;
    private TextView tvQuestionNr;
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
            this.setTitle(getIntent().getExtras().getString(RUBRIC));
        } else if (listtype.equals(LISTCATEGORY)) {
            categoryid = getIntent().getExtras().getString(CATEGORYID);
            btnCheck.setVisibility(View.INVISIBLE);
            this.setTitle(R.string.activitytestheadline);
        }
    }

    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestionNr = findViewById(R.id.tvQuestionNr);
        tvExplaination = findViewById(R.id.tvExplaination);
        llAnswers = findViewById(R.id.llAnswers);
        btnBack = findViewById(R.id.btnBack);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnCheck = findViewById(R.id.btnCheck);
        btnResult = findViewById(R.id.btnResult);
        btnGlossary = findViewById(R.id.btnGlossary);
        progressBar = findViewById(R.id.progressBar);
    }

    private void initEvents() {
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "fehlt noch", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        btnGlossary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "fehlt noch", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
        } else if (listtype.equals(LISTCATEGORY)) {

            ArrayList<Question> allQuestions = sqliteService.getAllCategoryQuestionRecords(categoryid);
            questions = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
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
    }

    private void prevQuestion() {
        if (questions.size() > 0 && index > 0) {
            index--;
            displayQuestion(index);
        }
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
                    } else {
                        etAnswer.setTextColor(Color.GREEN);
                    }
                    etAnswer.setEnabled(false);
                    answers.get(0).setResponse(etAnswer.getText().toString());
                }
                // Checkboxfrage
            } else if (question.getArt().equals(TYPECHECK)) {
                for (int i = 0; i < answers.size(); i++) {
                    CheckBox cb = findViewById(i);
                    cb.setTextColor(checkboxColor(answers.get(i), cb.getCurrentTextColor()));
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
        progressBar.setProgress(index+1);
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


