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

import java.util.ArrayList;
import java.util.HashMap;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class AnswerActivity extends Activity implements Constants {

    private String questionid;

    private Button btnAddNewRecord;
    private Button btnBack;
    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;
    private ArrayList<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdanswer);
        questionid = getIntent().getExtras().getString(QUESTIONID);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayAllRecords();
    }

    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        btnAddNewRecord = findViewById(R.id.btnAddNewRecord);
        btnBack = findViewById(R.id.btnBack);
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onAddRecord() {
        Intent intent = new Intent(this, AnswerNewActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        intent.putExtra(QUESTIONID, questionid);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void onUpdateRecord(String answID) {
        Intent intent = new Intent(this, AnswerNewActivity.class);
        intent.putExtra(DML_TYPE, UPDATE);
        intent.putExtra(ANSWERID, answID);
        startActivityForResult(intent, DML_UPDATE_RECORD);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        Question question = sqliteService.getQuestionRecord(questionid);
        ArrayList<Answer> answers = sqliteService.getAllAnswerRecords(questionid);

        if (answers.size() > 0) {
            if (question.getArt().equals(TYPETEXT)) {
                btnAddNewRecord.setVisibility(View.INVISIBLE);
            }
            tvNoRecordsFound.setVisibility(View.GONE);
            for (int i = 0; i < answers.size(); i++) {
                final Answer answer = answers.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.bd_answer_record, null);
                TextView tvAntwort = view.findViewById(R.id.tvAnswer);
                tvAntwort.setText(answer.getAnswer());
                ImageButton iBtnDelete = view.findViewById(R.id.iBtnDelete);
                ImageButton iBtnEdit = view.findViewById(R.id.iBtnEdit);
                TextView tvRichtigFalsch = view.findViewById(R.id.tvRichtigFalsch);
                tvRichtigFalsch.setText(answer.getTruefalse());
                iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(answer.getId());
                    }
                });

                iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(AnswerActivity.this);
                        deleteDialogOk.setTitle(R.string.antwortloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sqliteService.deleteAnswerRecord(answer.getId());
                                        btnAddNewRecord.setVisibility(View.VISIBLE);
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


