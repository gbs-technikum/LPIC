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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class QuestionActivity extends Activity implements Constants {
    private String rubrikid;
    private String rubrik;
    private Button btnAddNewRecord;
    private Button btnBack;
    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;

    private ArrayList<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdquestion);

        rubrikid = getIntent().getExtras().getString(RUBRICID);
        rubrik = getIntent().getExtras().getString(RUBRICID);

        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        displayAllRecords();
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
        Intent intent = new Intent(this, QuestionNewActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        intent.putExtra(RUBRICID, rubrikid);
        startActivityForResult(intent, DML_ADD_RECORD);
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
            Question question;
            for (int i = 0; i < questions.size(); i++) {

                question = questions.get(i);

                final MRow mRow = new MRow();
                final View view = LayoutInflater.from(this).inflate(R.layout.bd_question_record, null);
                view.setTag(question.getId());

                mRow.tvFrage = view.findViewById(R.id.tvQuestion);
                mRow.tvFrage.setText(question.getFrage());
                mRow.iBtnDelete = view.findViewById(R.id.iBtnDelete);
                mRow.iBtnEdit = view.findViewById(R.id.iBtnEdit);
                mRow.iBtnAnswer = view.findViewById(R.id.iBtnQuestion);

                mRow.iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(view.getTag().toString());
                    }
                });
                mRow.iBtnAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewAnswer(view.getTag().toString());
                    }
                });
                mRow.iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(QuestionActivity.this);
                        deleteDialogOk.setTitle(R.string.frageloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteQuestionRecord(view.getTag().toString())) {
                                            Toast.makeText(QuestionActivity.this, "Der Datensatz kann nicht gelöscht werden, bitte erst die korrespondierenden Fragen löschen.", Toast.LENGTH_LONG).show();
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

    private class MRow {
        TextView tvFrage;
        ImageButton iBtnDelete;
        ImageButton iBtnEdit;
        ImageButton iBtnAnswer;
    }
}
