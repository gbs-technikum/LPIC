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
import lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData.CategoryBDActivity;

public class CategoryActivity extends Activity {

    public static final String KATEGORIEID = "KategorieID";
    public static final String KATEGORIE = "Kategorie";

    private LinearLayout parentLayout;
    private SqliteService sqliteService;
    private Button sd;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        displayAllRecords();
    }

    private void initEvents() {
        sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, CategoryBDActivity.class);
                startActivityForResult(intent,1);
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
        parentLayout = findViewById(R.id.parentLayout);
        sd = new Button(this);
        sd = findViewById(R.id.button);
        btnExit = findViewById(R.id.btnExit);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        ArrayList<Category> categories = sqliteService.getAllCategoryRecords();

        for (int i = 0; i < categories.size(); i++) {

            Category category = categories.get(i);

            final View view = LayoutInflater.from(this).inflate(R.layout.category_record, null);
            view.setTag(category.getId());

            final Button btnKategorie = view.findViewById(R.id.btnKategorie);
            btnKategorie.setText(category.getCategory());
            btnKategorie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShowRubric(view.getTag().toString(), "");
                }
            });
            parentLayout.addView(view);
        }
    }

    private void onShowRubric(String categoryID, String categorie) {
        Intent intent = new Intent(this, RubricActivity.class);
        intent.putExtra(KATEGORIE, categorie);
        intent.putExtra(KATEGORIEID, categoryID);
        startActivity(intent);
    }

    private class MRow {
        Button btnKategorie;
    }

}
