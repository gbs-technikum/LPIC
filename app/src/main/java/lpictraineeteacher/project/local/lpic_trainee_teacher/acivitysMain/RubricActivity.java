package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class RubricActivity extends Activity implements Constants {

    private Button btnBack;
    private Button btnTest;
    private ImageButton btnInfo;
    private LinearLayout llParentLayout;
    private SqliteService sqliteService;
    private String categoryid;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubrik);
        sqliteService = SqliteService.getInstance(this);
        checkForRequest();
        initComponents();
        initEvents();
        displayAllRecords();
    }

    private void checkForRequest() {
        categoryid = getIntent().getExtras().getString(CATEGORYID);
        category = getIntent().getExtras().getString(CATEGORY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAllRecords();
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowTest();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "fehlt noch", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void initComponents() {
        this.setTitle(getString(R.string.activityrubricheadline) + " - " + category);
        llParentLayout = findViewById(R.id.llParentLayout);
        btnBack = findViewById(R.id.btnBack);
        btnTest = findViewById(R.id.btnTest);
        btnInfo = findViewById(R.id.btnInfo);
    }

    private void onShowTest() {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(CATEGORYID, categoryid);
        intent.putExtra(LISTTYPE, LISTCATEGORY);
        startActivity(intent);
    }

    private void onShowQuestion(String rubricid, String rubric) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(RUBRIC, rubric);
        intent.putExtra(RUBRICID, rubricid);
        intent.putExtra(CATEGORYID, categoryid);
        intent.putExtra(LISTTYPE, LISTRUBRIC);
        startActivity(intent);
    }

    private void displayAllRecords() {
        llParentLayout.removeAllViews();
        ArrayList<Rubric> rubrics = sqliteService.getAllRubricRecords(categoryid);

        for (int i = 0; i < rubrics.size(); i++) {

            Rubric rubric = rubrics.get(i);

            final View view = LayoutInflater.from(this).inflate(R.layout.select_record, null);
            view.setTag(rubric.getId());
            final Button btnSelect = view.findViewById(R.id.btnSelect);
            btnSelect.setText(rubric.getRubrik());
            final String frubric = rubric.getRubrik();
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShowQuestion(view.getTag().toString(), frubric);
                }
            });
            llParentLayout.addView(view);
        }
    }
}
