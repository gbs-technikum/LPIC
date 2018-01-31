package lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData;

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
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class AnswerBDActivity extends Activity implements ConstantsBD {

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
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            displayAllRecords();
        }
    }


    private void initComponents() {
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
        Intent intent = new Intent(this, AnswerNewBDActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        intent.putExtra(QUESTIONID, questionid);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void onUpdateRecord(String answID) {
        Intent intent = new Intent(this, AnswerNewBDActivity.class);
        intent.putExtra(ANSWERID, answID);
        intent.putExtra(DML_TYPE, UPDATE);
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
            Answer answer;
            for (int i = 0; i < answers.size(); i++) {
                answer = answers.get(i);
                final MRow mRow = new MRow();
                final View view = LayoutInflater.from(this).inflate(R.layout.bd_answer_record, null);
                view.setTag(answer.getId());
                mRow.tvAntwort = view.findViewById(R.id.tvAnswer);
                mRow.tvAntwort.setText(answer.getAnswer());
                mRow.iBtnDelete = view.findViewById(R.id.iBtnDelete);
                mRow.iBtnEdit = view.findViewById(R.id.iBtnEdit);
                mRow.tvRichtigFalsch = view.findViewById(R.id.tvRichtigFalsch);
                mRow.tvRichtigFalsch.setText(answer.getTruefalse());
                mRow.iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(view.getTag().toString());
                    }
                });

                mRow.iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(AnswerBDActivity.this);
                        deleteDialogOk.setTitle(R.string.antwortloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sqliteService.deleteAnswerRecord(view.getTag().toString());
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

    private class MRow {
        TextView tvAntwort;
        TextView tvRichtigFalsch;
        ImageButton iBtnDelete;
        ImageButton iBtnEdit;
    }
}


