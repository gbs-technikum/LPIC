package lpictraineeteacher.project.local.lpic_trainee_teacher.AcivitysMain;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;

public class RubricActivity extends Activity {

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubrik);

        initComponents();
        initEvents();
    }

    private void initComponents() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
