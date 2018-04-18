package lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.UUID;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class CategoryNewActivity extends Activity implements Constants {

    private EditText etKategorie;
    private ImageButton btnCancel;
    private ImageButton btnDML;
    private String categoryID;
    String requestCode;
    private SqliteService sqliteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcategory_new);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        requestCode = getIntent().getStringExtra(DML_TYPE);
        categoryID = getIntent().getStringExtra(CATEGORYID);
        etKategorie.setText(getIntent().getStringExtra(CATEGORY));
    }

    private void initEvents() {
        btnDML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnDMLClick();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        etKategorie = findViewById(R.id.etKategorie);
        btnDML = findViewById(R.id.btnDML);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void onBtnDMLClick() {
        if (etKategorie.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.emptyfield, Toast.LENGTH_LONG).show();
        } else {
            Category category = new Category();
            category.setCategory(etKategorie.getText().toString());
            if (requestCode.equals(INSERT)) {
                category.setId(UUID.randomUUID().toString());
                sqliteService.insertCategoryRecord(category);
            } else {
                category.setId(categoryID);
                sqliteService.updateCategoryRecord(category);
            }
            finish();
        }
    }
}
