package lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements Constants {
    private String rubrikid;
    private String rubrik;
    private Button btnAddNewRecord;
    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdquestion);
        rubrikid = getIntent().getExtras().getString(RUBRICID);
        rubrik = getIntent().getExtras().getString(RUBRIC);
        initComponents();
        initEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayAllRecords();
    }

    private void initComponents() {
        this.setTitle(getString(R.string.frage) + " - " + rubrik);
        sqliteService = SqliteService.getInstance(this);
        btnAddNewRecord = findViewById(R.id.btnAddNewRecord);
        parentLayout = findViewById(R.id.llParentLayout);
        tvNoRecordsFound = findViewById(R.id.tvNoRecordsFound);
    }

    private void initEvents() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRecord();
            }
        });
    }

    private void onAddRecord() {
        Intent intent = new Intent(this, QuestionNewActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        intent.putExtra(RUBRICID, rubrikid);
        startActivity(intent);
    }

    private void onUpdateRecord(String questID) {
        Intent intent = new Intent(this, QuestionNewActivity.class);
        intent.putExtra(DML_TYPE, UPDATE);
        intent.putExtra(QUESTIONID, questID);
        startActivityForResult(intent, DML_UPDATE_RECORD);
    }

    private void onEditNewAnswer(String questID) {
        Intent intent = new Intent(this, AnswerActivity.class);
        intent.putExtra(QUESTIONID, questID);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        ArrayList<Question> questions = sqliteService.getAllQuestionRecords(rubrikid);

        if (questions.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            for (int i = 0; i < questions.size(); i++) {
                final Question question = questions.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.bd_question_record, null);
                TextView tvFrage = view.findViewById(R.id.tvQuestion);
                tvFrage.setText(question.getFrage());
                ImageButton iBtnDelete = view.findViewById(R.id.iBtnDelete);
                ImageButton iBtnEdit = view.findViewById(R.id.iBtnEdit);
                ImageButton iBtnAnswer = view.findViewById(R.id.iBtnQuestion);

                iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(question.getId());
                    }
                });
                iBtnAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewAnswer(question.getId());
                    }
                });
                iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(QuestionActivity.this);
                        deleteDialogOk.setTitle(R.string.frageloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteQuestionRecord(question.getId())) {
                                            Toast.makeText(QuestionActivity.this, R.string.questiontoast, Toast.LENGTH_LONG).show();
                                        }
                                        displayAllRecords();
                                    }
                                }
                        );
                        deleteDialogOk.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        deleteDialogOk.show();
                    }
                });
                parentLayout.addView(view);
            }
        } else {
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }
}
