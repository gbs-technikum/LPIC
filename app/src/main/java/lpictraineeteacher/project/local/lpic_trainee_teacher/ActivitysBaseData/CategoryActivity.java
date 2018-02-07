package lpictraineeteacher.project.local.lpic_trainee_teacher.ActivitysBaseData;

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
import java.util.HashMap;
import java.util.UUID;

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
    private String kategoryID;
    private ArrayList<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdcategory);
        sqliteService = SqliteService.getInstance(this);
        initComponents();
        initEvents();
        displayAllRecords();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            String kategorie = intent.getStringExtra(CATEGORY);
            Category category = new Category();
            category.setCategory(kategorie);
            if (requestCode == DML_ADD_RECORD) {
                category.setId(UUID.randomUUID().toString());
                sqliteService.insertCategoryRecord(category);
            } else if (requestCode == DML_UPDATE_RECORD) {
                category.setId(kategoryID);
                sqliteService.updateCategoryRecord(category);
            }
            displayAllRecords();
        }
    }

    private void initComponents() {
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
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void onUpdateRecord(String kategoryID, String kategorie) {
        Intent intent = new Intent(this, CategoryNewActivity.class);
        intent.putExtra(CATEGORYID, kategoryID);
        intent.putExtra(CATEGORY, kategorie);
        intent.putExtra(DML_TYPE, UPDATE);
        startActivityForResult(intent, DML_UPDATE_RECORD);
    }

    private void onEditNewRubric(String kategoryID, String kategorie) {
        Intent intent = new Intent(this, RubricActivity.class);
        intent.putExtra(CATEGORY, kategorie);
        intent.putExtra(CATEGORYID, kategoryID);
        startActivityForResult(intent, DML_ADD_RECORD);
    }

    private void displayAllRecords() {
        parentLayout.removeAllViews();
        //final ArrayList<Category> categories = sqliteService.getAllAnswerRecords();
        ArrayList<Category> categories = sqliteService.getAllCategoryRecords();
        if (categories.size() > 0) {
            tvNoRecordsFound.setVisibility(View.GONE);
            Category category;
            for (int i = 0; i < categories.size(); i++) {
                category = categories.get(i);
                final MRow mRow = new MRow();
                final View view = LayoutInflater.from(this).inflate(R.layout.bd_category_record, null);
                view.setTag(category.getId());
                mRow.tvKategorie = view.findViewById(R.id.tvKategorie);
                mRow.tvKategorie.setText(category.getCategory());
                mRow.iBtnDelete = view.findViewById(R.id.iBtnDelete);
                mRow.iBtnEdit = view.findViewById(R.id.iBtnEdit);
                mRow.iBtnRubric = view.findViewById(R.id.iBtnRubric);

                mRow.iBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kategoryID = view.getTag().toString();
                        onUpdateRecord( view.getTag().toString(), mRow.tvKategorie.getText().toString());
                    }
                });
                mRow.iBtnRubric.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditNewRubric( view.getTag().toString(), mRow.tvKategorie.getText().toString());
                    }
                });

                mRow.iBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder deleteDialogOk = new AlertDialog.Builder(CategoryActivity.this);
                        deleteDialogOk.setTitle(R.string.kategorieloeschen);
                        deleteDialogOk.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!sqliteService.deleteCategoryRecord(view.getTag().toString())) {
                                            Toast.makeText( CategoryActivity.this, "Der Datensatz kann nicht gelöscht werden, bitte erst die korrespondierenden Rubriken löschen.",Toast.LENGTH_LONG).show();
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

    private class MRow {
        TextView tvKategorie;
        ImageButton iBtnDelete;
        ImageButton iBtnEdit;
        ImageButton iBtnRubric;
    }
}
