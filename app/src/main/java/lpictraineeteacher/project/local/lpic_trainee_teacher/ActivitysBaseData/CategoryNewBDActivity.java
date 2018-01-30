package lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;

public class CategoryNewBDActivity extends Activity  implements ConstantsBD {

    private EditText etKategorie;
    private Button btnBack;
    private Button btnDML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcategory_new);
        initComponents();
        initEvents();
        checkForRequest();
    }

    private void checkForRequest() {
        String request = getIntent().getExtras().get(DML_TYPE).toString();
        if (request.equals(UPDATE)) {
            btnDML.setText(R.string.update);
            etKategorie.setText(getIntent().getExtras().get(CategoryBDActivity.CATEGORY).toString());
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
            Toast.makeText(getApplicationContext(),  R.string.emptyfield, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(CATEGORY,etKategorie.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
