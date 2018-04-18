package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Glossary;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class CategoryActivity extends Activity {

    public static final String CATEGORYID = "CATEGORYID";
    public static final String CATEGORY = "CATEGORY";

    private LinearLayout llParentLayout;
    private SqliteService sqliteService;
    private ImageButton btnBD;
    private ImageButton btnInfo;
    private ImageButton btnGlossary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAllRecords();
    }

    private void initEvents() {
        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData.CategoryActivity.class);
                startActivity(intent);
            }
        });


        btnGlossary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, GlossaryActivity.class);
                startActivity(intent);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initComponents() {
        llParentLayout = findViewById(R.id.llParentLayout);
        btnBD = findViewById(R.id.btnBD);
        btnInfo = findViewById(R.id.btnInfo);
        btnGlossary = findViewById(R.id.btnGlossary);
    }

    private void displayAllRecords() {
        llParentLayout.removeAllViews();
        ArrayList<Category> categories = sqliteService.getAllCategoryRecords();

        for (int i = 0; i < categories.size(); i++) {
            final Category category = categories.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.select_record, null);
            view.setTag(category.getId());
            Button btnSelect = view.findViewById(R.id.btnSelect);
            btnSelect.setText(category.getCategory());
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShowRubric(category.getId(), category.getCategory());
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
