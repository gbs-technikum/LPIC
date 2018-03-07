package lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

/**
 * Created by mkoenig on 20.01.2018.
 */

public class RubricNewActivity extends Activity implements Constants {

    private EditText etDaten;
    private Button btnCancel;
    private Button btnDML;
    private String rubricID;
    private String kategorieID;
    String requestCode;
    private SqliteService sqliteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdrubric_new);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        requestCode = getIntent().getStringExtra(DML_TYPE);
        kategorieID = getIntent().getStringExtra(CATEGORYID);
        if (requestCode.equals(UPDATE)) {
            btnDML.setText(R.string.update);
            etDaten.setText(getIntent().getStringExtra(RUBRIC));
            rubricID = getIntent().getStringExtra(RUBRICID);
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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponents() {
        sqliteService = SqliteService.getInstance(this);
        etDaten = findViewById(R.id.etDaten);
        btnDML = findViewById(R.id.btnDML);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void onBtnDMLClick() {
        if (etDaten.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.emptyfield, Toast.LENGTH_LONG).show();
        } else {
            Rubric rubric = new Rubric();
            rubric.setKategorieID(kategorieID);
            rubric.setRubrik(etDaten.getText().toString());
            if (requestCode.equals(INSERT)) {
                rubric.setId(UUID.randomUUID().toString());
                sqliteService.insertRubricRecord(rubric);
            } else {
                rubric.setId(rubricID);
                sqliteService.updateRubricRecord(rubric);
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
