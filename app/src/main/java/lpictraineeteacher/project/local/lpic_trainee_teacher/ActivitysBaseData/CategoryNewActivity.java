package lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class CategoryNewActivity extends Activity implements Constants {

    private EditText etKategorie;
    private Button btnBack;
    private Button btnDML;
    private String categoryID;
    String requestCode;
    private SqliteService sqliteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcategory_new);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        requestCode = getIntent().getStringExtra(DML_TYPE);
        if (requestCode.equals(UPDATE)) {
            btnDML.setText(R.string.update);
            categoryID = getIntent().getStringExtra(CATEGORYID);
            etKategorie.setText(getIntent().getStringExtra(CATEGORY));
        } else {
            btnDML.setText(R.string.insert);
        }
    }

    private void initEvents() {
        btnDML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnDMLClick();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponents() {
        etKategorie = findViewById(R.id.etKategorie);
        btnDML = findViewById(R.id.btnDML);
        btnBack = findViewById(R.id.btnBack);
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
            setResult(RESULT_OK);
            finish();
        }
    }
}
