package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.ConstantsBD;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class RubricActivity extends Activity implements ConstantsBD{

    private Button btnBack;
    private LinearLayout parentLayout;
    private SqliteService sqliteService;
    private TextView tvHeadline;
    private String categoryid;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubrik);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        checkForRequest();
        displayAllRecords();
    }

    private void checkForRequest() {
        categoryid = getIntent().getExtras().getString(CATEGORYID);
        category = getIntent().getExtras().getString(CATEGORY );
    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        displayAllRecords();
    //    }

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
    }

    private void initComponents() {
        parentLayout = findViewById(R.id.llParentLayout);
        tvHeadline = findViewById(R.id.tvHeadline);
        btnBack = findViewById(R.id.btnBack);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        parentLayout.addView(tvHeadline);
        tvHeadline.setText(category);

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
            parentLayout.addView(view);
        }
    }

    private void onShowQuestion(String rubrikid, String rubrik) {
        Intent intent = new Intent(this, RubricActivity.class);
        intent.putExtra(RUBRIC, rubrik);
        intent.putExtra(RUBRICID, rubrikid);
        startActivity(intent);
    }
}
