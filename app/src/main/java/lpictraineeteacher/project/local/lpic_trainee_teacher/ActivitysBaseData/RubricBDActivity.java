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
import java.util.UUID;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class RubricBDActivity extends Activity implements ConstantsBD {

    private String kategorieid;
    private String kategorie;

    private Button btnAddNewRecord;
    private Button btnBack;
    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;

    private String rubrikID;
    private ArrayList<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdrubric);

        kategorieid = getIntent().getExtras().getString(CategoryBDActivity.CATEGORYID);
        kategorie = getIntent().getExtras().getString(CategoryBDActivity.CATEGORY);

        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            String strRubrik = intent.getStringExtra(RUBRIC);
            Rubric rubric = new Rubric();
            rubric.setRubrik(strRubrik);
            rubric.setKategorieID(kategorieid);

            if (requestCode == DML_ADD_RECORD) {
                rubric.setId(UUID.randomUUID().toString());
                sqliteService.insertRubricRecord(rubric);
            } else if (requestCode == DML_UPDATE_RECORD) {
                rubric.setId(rubrikID);
                sqliteService.updateRubricRecord(rubric);
            }
            displayAllRecords();
        }
    }

    private void initComponents() {
        btnAddNewRecord = findViewById(R.id.btnAddNewRecord);
        btnBack = findViewById(R.id.btnBack);
        parentLayout = findViewById(R.id.parentLayout);
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
        Intent intent = new Intent(this, RubricNewBDActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void onUpdateRecord(String rubricid, String rubrik) {
        Intent intent = new Intent(this, RubricNewBDActivity.class);
        intent.putExtra(RUBRICID, rubricid);
        intent.putExtra(RUBRIC, rubrik);
        intent.putExtra(DML_TYPE, UPDATE);
        startActivityForResult(intent, DML_UPDATE_RECORD);
    }

    private void onEditNewQuestion(String rubrikID, String rubrik) {
        Intent intent = new Intent(this, QuestionBDActivity.class);
        intent.putExtra(RUBRICID, rubrikID);
        intent.putExtra(RUBRIC, rubrik);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void displayAllRecords() {

        parentLayout.removeAllViews();

        ArrayList<Rubric> rubrics = sqliteService.getAllRubricRecords(kategorieid);

        if (rubrics.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            Rubric rubric;
            for (int i = 0; i < rubrics.size(); i++) {
                rubric = rubrics.get(i);
                final MRow mRow = new MRow();
                final View view = LayoutInflater.from(this).inflate(R.layout.bd_rubric_record, null);
                view.setTag(rubric.getId());
                mRow.tvRubrik = view.findViewById(R.id.tvRubric);
                mRow.tvRubrik.setText(rubric.getRubrik());
                mRow.iBtnDelete = view.findViewById(R.id.iBtnDelete);
                mRow.iBtnEdit = view.findViewById(R.id.iBtnEdit);
                mRow.iBtnQuestion = view.findViewById(R.id.iBtnQuestion);

                mRow.iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rubrikID = view.getTag().toString();
                        onUpdateRecord(view.getTag().toString(), mRow.tvRubrik.getText().toString());
                    }
                });

                mRow.iBtnQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewQuestion(view.getTag().toString(), mRow.tvRubrik.getText().toString());
                    }
                });


                mRow.iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(RubricBDActivity.this);
                        deleteDialogOk.setTitle(R.string.rubrikloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteRubricRecord(view.getTag().toString())) {
                                            Toast.makeText(RubricBDActivity.this, "Der Datensatz kann nicht gelöscht werden, bitte erst die korrespondierenden Fragen löschen.", Toast.LENGTH_LONG).show();
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
        TextView tvRubrik;
        ImageButton iBtnDelete;
        ImageButton iBtnEdit;
        ImageButton iBtnQuestion;
    }
}
