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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

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
    private Button btnInfo;
    private Button btnGlossary;
    private ImageButton btnGerman;
    private ImageButton btnEnglish;

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
                Toast toast = Toast.makeText(getApplicationContext(), "fehlt noch", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageToEnglish();
                Toast toast = Toast.makeText(getApplicationContext(), "Locale is set to English", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        btnGerman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageToGerman();
                Toast toast = Toast.makeText(getApplicationContext(), "Sprache wurde auf Deutsch umgestellt", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void initComponents() {
        llParentLayout = findViewById(R.id.llParentLayout);
        btnBD = findViewById(R.id.btnBD);
        btnInfo = findViewById(R.id.btnInfo);
        btnGlossary = findViewById(R.id.btnGlossary);
        btnGerman = findViewById(R.id.btnLanguageGerman);
        btnEnglish = findViewById(R.id.btnLanguageEnglish);
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

    private void changeLanguageToEnglish() {
        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void changeLanguageToGerman() {
        String languageToLoad = "de";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}
