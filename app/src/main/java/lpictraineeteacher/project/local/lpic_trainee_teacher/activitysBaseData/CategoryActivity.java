package lpictraineeteacher.project.local.lpic_trainee_teacher.activitysBaseData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.R;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Constants;
import lpictraineeteacher.project.local.lpic_trainee_teacher.persistent.SqliteService;

public class CategoryActivity extends Activity implements Constants {

    private Button btnAddNewRecord;
    private Button btnBack;
    private SqliteService sqliteService;
    private LinearLayout parentLayout;
    private TextView tvNoRecordsFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcategory);
        initComponents();
        initEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayAllRecords();
    }

    private void initComponents() {
        this.setTitle(R.string.category);
        sqliteService = SqliteService.getInstance(this);
        btnAddNewRecord = findViewById(R.id.btnAddNewRecord);
        btnBack = findViewById(R.id.btnBack);
        parentLayout = findViewById(R.id.llParentLayout);
        tvNoRecordsFound = findViewById(R.id.tvNoRecordsFound);
    }

    private void initEvents() {
        btnAddNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddRecord();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onAddRecord() {
        Intent intent = new Intent(this, CategoryNewActivity.class);
        intent.putExtra(DML_TYPE, INSERT);
        startActivity(intent);
    }

    private void onUpdateRecord(String categoryID, String category) {
        Intent intent = new Intent(this, CategoryNewActivity.class);
        intent.putExtra(CATEGORYID, categoryID);
        intent.putExtra(CATEGORY, category);
        intent.putExtra(DML_TYPE, UPDATE);
        startActivity(intent);
    }

    private void onEditNewRubric(String categoryID, String category) {
        Intent intent = new Intent(this, RubricActivity.class);
        intent.putExtra(CATEGORY, category);
        intent.putExtra(CATEGORYID, categoryID);
        startActivity(intent);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        ArrayList<Category> categories = sqliteService.getAllCategoryRecords();
        if (categories.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            for (int i = 0; i < categories.size(); i++) {
                final Category category = categories.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.bd_category_record, null);
                TextView tvKategorie = view.findViewById(R.id.tvKategorie);
                tvKategorie.setText(category.getCategory());
                ImageButton iBtnDelete = view.findViewById(R.id.iBtnDelete);
                ImageButton iBtnEdit = view.findViewById(R.id.iBtnEdit);
                ImageButton iBtnRubric = view.findViewById(R.id.iBtnRubric);

                iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUpdateRecord(category.getId(), category.getCategory());
                    }
                });
                iBtnRubric.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewRubric(category.getId(), category.getCategory());
                    }
                });

                iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(CategoryActivity.this);
                        deleteDialogOk.setTitle(R.string.kategorieloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteCategoryRecord(category.getId().toString())) {
                                            Toast.makeText(CategoryActivity.this, R.string.categorytoast, Toast.LENGTH_LONG).show();
                                        }
                                        displayAllRecords();
                                    }
                                }
                        );
                        deleteDialogOk.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        deleteDialogOk.show();
                    }
                });
                parentLayout.addView(view);
            }
        } else {
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }
    }
}
