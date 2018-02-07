package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class CategoryActivity extends Activity {

    public static final String CATEGORYID = "CATEGORYID";
    public static final String CATEGORY = "CATEGORY";

    private LinearLayout llParentLayout;
    private SqliteService sqliteService;
    private Button btnBD;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
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
        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.CategoryActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponents() {
        llParentLayout = findViewById(R.id.llParentLayout);
        btnBD = findViewById(R.id.btnBD);
        btnExit = findViewById(R.id.btnExit);
    }

    private void displayAllRecords() {
        llParentLayout.removeAllViews();
        ArrayList<Category> categories = sqliteService.getAllCategoryRecords();

        for (int i = 0; i < categories.size(); i++) {

            Category category = categories.get(i);

            final View view = LayoutInflater.from(this).inflate(R.layout.select_record, null);
            view.setTag(category.getId());
            final String cat = category.getCategory();

            final Button btnSelect = view.findViewById(R.id.btnSelect);
            btnSelect.setText(category.getCategory());
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShowRubric(view.getTag().toString(),cat );
                }
            });
            llParentLayout.addView(view);
        }
    }

    private void onShowRubric(String categoryID, String categorie) {
        Intent intent = new Intent(this, RubricActivity.class);
        intent.putExtra(CATEGORY, categorie);
        intent.putExtra(CATEGORYID, categoryID);
        startActivity(intent);
    }

}
