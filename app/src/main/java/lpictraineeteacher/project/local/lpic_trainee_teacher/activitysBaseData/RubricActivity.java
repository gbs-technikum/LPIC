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
import java.util.HashMap;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class RubricActivity extends Activity implements Constants {

    private String kategorieid;
    private String kategorie;
    private ImageButton btnAddNewRecord;

    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;

    private String rubrikID;
    private ArrayList<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdrubric);
        kategorieid = getIntent().getStringExtra(CATEGORYID);
        kategorie = getIntent().getStringExtra(CATEGORY);
        initComponents();
        initEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayAllRecords();
    }

    private void initComponents() {
        this.setTitle(getString(R.string.rubric) + " - " + kategorie);
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
        Intent intent = new Intent(this, RubricNewActivity.class);
        intent.putExtra(CATEGORYID, kategorieid);
        intent.putExtra(DML_TYPE, INSERT);
        startActivity(intent);
    }

    private void onUpdateRecord(String rubricid, String rubrik) {
        Intent intent = new Intent(this, RubricNewActivity.class);
        intent.putExtra(CATEGORYID, kategorieid);
        intent.putExtra(RUBRICID, rubricid);
        intent.putExtra(RUBRIC, rubrik);
        intent.putExtra(DML_TYPE, UPDATE);
        startActivity(intent);
    }

    private void onEditNewQuestion(String rubrikID, String rubrik) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(RUBRICID, rubrikID);
        intent.putExtra(RUBRIC, rubrik);
        startActivity(intent);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        ArrayList<Rubric> rubrics = sqliteService.getAllRubricRecords(kategorieid);
        if (rubrics.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            for (int i = 0; i < rubrics.size(); i++) {
                final Rubric rubric = rubrics.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.bd_rubric_record, null);
                String rubricID = rubric.getId();
                TextView tvRubrik = view.findViewById(R.id.tvRubric);
                tvRubrik.setText(rubric.getRubrik());
                ImageButton iBtnDelete = view.findViewById(R.id.iBtnDelete);
                ImageButton iBtnEdit = view.findViewById(R.id.iBtnEdit);
                ImageButton iBtnQuestion = view.findViewById(R.id.iBtnQuestion);
                iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(rubric.getId(), rubric.getRubrik());
                    }
                });
                iBtnQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewQuestion(rubric.getId(), rubric.getRubrik());
                    }
                });
                iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(RubricActivity.this);
                        deleteDialogOk.setTitle(R.string.rubrikloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteRubricRecord(rubric.getId())) {
                                            Toast.makeText(RubricActivity.this, R.string.rubrictoast, Toast.LENGTH_LONG).show();
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
