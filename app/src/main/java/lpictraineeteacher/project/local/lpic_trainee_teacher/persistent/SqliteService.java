package lpictraineeteacher.project.local.lpic_trainee_teacher.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Answer;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Category;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Question;
import lpictraineeteacher.project.local.lpic_trainee_teacher.classes.Rubric;

/**
 * Created by mkoenig on 28.01.2018.
 */

public class SqliteService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "lpicapp.db";
    private static final String TABLE_ANSWER_NAME = "ANTWORTEN";
    private static final String TABLE_RUBRIC_NAME = "RUBRIKEN";
    private static final String TABLE_QUESTION_NAME = "FRAGEN";
    private static final String TABLE_CATEGORY_NAME = "KATEGORIEN";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_KATEGORIE = "KATEGORIE";
    private static final String COLUMN_KATEGORIEID = "KID";
    private static final String COLUMN_RUBRIK = "RUBRIK";
    private static final String COLUMN_RUBRIKID = "RID";
    private static final String COLUMN_FRAGE = "FRAGE";
    private static final String COLUMN_ART = "ART";
    private static final String COLUMN_HINWEIS = "HINWEIS";
    private static final String COLUMN_QID = "FID";
    private static final String COLUMN_ANTWORT = "ANTWORT";
    private static final String COLUMN_RICHTIG = "ISTRICHTIG";

    private static SqliteService ourInstance;

    public static SqliteService getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SqliteService(context.getApplicationContext());
        }
        return ourInstance;
    }

    private SqliteService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // *********** Answer section ****************

    public void insertAnswerRecord(Answer answer) {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, answer.getId());
        contentValues.put(COLUMN_QID, answer.getFid());
        contentValues.put(COLUMN_ANTWORT, answer.getAnswer());
        contentValues.put(COLUMN_RICHTIG, answer.getTruefalse());
        database.insert(TABLE_ANSWER_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<Answer> getAllAnswerRecords(String questionid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_ANSWER_NAME, null, COLUMN_QID + " = ?", new String[]{questionid}, null, null, COLUMN_ANTWORT);

        ArrayList<Answer> answers = new ArrayList<>();
        Answer answer;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                answer = new Answer();
                answer.setId(cursor.getString(0));
                answer.setFid(cursor.getString(1));
                answer.setAnswer(cursor.getString(2));
                answer.setTruefalse(cursor.getString(3));
                answers.add(answer);
            }
        }
        cursor.close();
        database.close();

        return answers;
    }

    public Answer getAnswerRecord(String answerid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_ANSWER_NAME, null, COLUMN_ID + " = ?", new String[]{answerid}, null, null, COLUMN_ANTWORT);
        Answer answer = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            answer = new Answer();
            answer.setId(cursor.getString(0));
            answer.setFid(cursor.getString(1));
            answer.setAnswer(cursor.getString(2));
            answer.setTruefalse(cursor.getString(3));
        }
        cursor.close();
        database.close();
        return answer;
    }

    public void updateAnswerRecord(Answer answer) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ANTWORT, answer.getAnswer());
        contentValues.put(COLUMN_RICHTIG, answer.getTruefalse());
        database.update(TABLE_ANSWER_NAME, contentValues, COLUMN_ID + " = ?", new String[]{answer.getId()});
        database.close();
    }


    public void deleteAnswerRecord(String answerid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        database.delete(TABLE_ANSWER_NAME, COLUMN_ID + " = ?", new String[]{answerid});
        database.close();
    }

    // *********** Question section ****************

    public void insertQuestionRecord(Question question) {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, question.getId());
        contentValues.put(COLUMN_RUBRIKID, question.getRid());
        contentValues.put(COLUMN_FRAGE, question.getFrage());
        contentValues.put(COLUMN_ART, question.getArt());
        contentValues.put(COLUMN_HINWEIS, question.getHinweis());
        database.insert(TABLE_QUESTION_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<Question> getAllQuestionRecords(String rubrikid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_QUESTION_NAME, null, COLUMN_RUBRIKID + " = ?", new String[]{rubrikid}, null, null, COLUMN_FRAGE);

        ArrayList<Question> questions = new ArrayList<>();
        Question question;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                question = new Question();
                question.setId(cursor.getString(0));
                question.setRid(cursor.getString(1));
                question.setFrage(cursor.getString(2));
                question.setArt(cursor.getString(3));
                question.setHinweis(cursor.getString(4));
                questions.add(question);
            }
        }
        cursor.close();
        database.close();

        return questions;
    }

    public Question getQuestionRecord(String questionid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_QUESTION_NAME, null, COLUMN_ID + " = ?", new String[]{questionid}, null, null, COLUMN_FRAGE);
        Question question = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            question = new Question();
            question.setId(cursor.getString(0));
            question.setRid(cursor.getString(1));
            question.setFrage(cursor.getString(2));
            question.setArt(cursor.getString(3));
            question.setHinweis(cursor.getString(4));
        }
        cursor.close();
        database.close();
        return question;
    }

    public void updateQuestionRecord(Question question) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FRAGE, question.getFrage());
        contentValues.put(COLUMN_ART, question.getArt());
        contentValues.put(COLUMN_HINWEIS, question.getHinweis());
        database.update(TABLE_QUESTION_NAME, contentValues, COLUMN_ID + " = ?", new String[]{question.getId()});
        database.close();
    }


    public boolean deleteQuestionRecord(String questionid) {
        if (getCountAnswer(questionid) < 1) {
            SQLiteDatabase database;
            database = this.getReadableDatabase();
            database.delete(TABLE_QUESTION_NAME, COLUMN_ID + " = ?", new String[]{questionid});
            database.close();
            return true;
        }
        return false;
    }


    public int getCountAnswer(String qid) {
        SQLiteDatabase database;
        int anzahl = 0;
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT count(*) as anzahl FROM ANTWORTEN where fid = '" + qid + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            anzahl = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return anzahl;
    }

    // *********** Rubric section ****************

    public void insertRubricRecord(Rubric rubric) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RUBRIK, rubric.getRubrik());
        contentValues.put(COLUMN_ID, rubric.getId());
        contentValues.put(COLUMN_KATEGORIEID, rubric.getKategorieID());
        database.insert(TABLE_RUBRIC_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<Rubric> getAllRubricRecords(String categoryid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_RUBRIC_NAME, null, COLUMN_KATEGORIEID + " = ?", new String[]{String.valueOf(categoryid)}, null, null, COLUMN_RUBRIK);

        ArrayList<Rubric> rubrics = new ArrayList<>();
        Rubric rubric;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                rubric = new Rubric();
                rubric.setId(cursor.getString(0));
                rubric.setKategorieID(cursor.getString(1));
                rubric.setRubrik(cursor.getString(2));
                rubrics.add(rubric);
            }
        }
        cursor.close();
        database.close();

        return rubrics;
    }

    public Rubric getRubricRecord(String rubricid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_RUBRIC_NAME, null, COLUMN_ID + " = ?", new String[]{rubricid}, null, null, COLUMN_RUBRIK);
        Rubric rubric = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            rubric = new Rubric();
            rubric.setId(cursor.getString(0));
            rubric.setKategorieID(cursor.getString(1));
            rubric.setRubrik(cursor.getString(2));
        }
        cursor.close();
        database.close();
        return rubric;
    }

    public void updateRubricRecord(Rubric rubric) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RUBRIK, rubric.getRubrik());
        database.update(TABLE_RUBRIC_NAME, contentValues, COLUMN_ID + " = ?", new String[]{rubric.getId()});
        database.close();
    }

    public boolean deleteRubricRecord(String rubricid) {
        if (getCountQuestion(rubricid) < 1) {
            SQLiteDatabase database;
            database = this.getReadableDatabase();
            database.delete(TABLE_RUBRIC_NAME, COLUMN_ID + " = ?", new String[]{rubricid});
            database.close();
            return true;
        }
        return false;
    }


    public int getCountRubrik(String kid) {
        SQLiteDatabase database;
        int anzahl = 0;
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT count(*) as anzahl FROM RUBRIKEN where kid = '" + kid + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            anzahl = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return anzahl;
    }

    public int getCountQuestion(String rid) {
        SQLiteDatabase database;
        int anzahl = 0;
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT count(*) as anzahl FROM FRAGEN where rid = '" + rid + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            anzahl = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return anzahl;
    }

    // *********** Category section ****************


    public void insertCategoryRecord(Category category) {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, category.getId());
        contentValues.put(COLUMN_KATEGORIE, category.getCategory());
        database.insert(TABLE_CATEGORY_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<Question> getAllCategoryQuestionRecords(String categoryID) {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select id, rid, frage, art, hinweis from fragen where rid in(select id from rubriken where kid = '" + categoryID + "')", null);

        ArrayList<Question> questions = new ArrayList<>();

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                Question question = new Question();
                question.setId(cursor.getString(0));
                question.setRid(cursor.getString(1));
                question.setFrage(cursor.getString(2));
                question.setArt(cursor.getString(3));
                question.setHinweis(cursor.getString(4));
                questions.add(question);
            }
        }
        cursor.close();
        database.close();
        return questions;
    }

    public ArrayList<Category> getAllCategoryRecords() {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_CATEGORY_NAME, null, null, null, null, null, COLUMN_KATEGORIE);

        ArrayList<Category> categories = new ArrayList<>();
        Category category;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                category = new Category();
                category.setId(cursor.getString(0));
                category.setCategory(cursor.getString(1));
                categories.add(category);
            }
        }
        cursor.close();
        database.close();

        return categories;
    }

    public Category getCategoryRecord(String categoryid) {
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_CATEGORY_NAME, null, COLUMN_ID + " = ?", new String[]{categoryid}, null, null, COLUMN_KATEGORIE);
        Category category = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            category = new Category();
            category.setId(cursor.getString(0));
            category.setCategory(cursor.getString(1));
        }
        cursor.close();
        database.close();
        return category;
    }

    public void updateCategoryRecord(Category category) {
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_KATEGORIE, category.getCategory());
        database.update(TABLE_CATEGORY_NAME, contentValues, COLUMN_ID + " = ?", new String[]{category.getId()});
        database.close();
    }

    public boolean deleteCategoryRecord(String categoryid) {
        if (getCountRubrik(categoryid) < 1) {
            SQLiteDatabase database;
            database = this.getReadableDatabase();
            database.delete(TABLE_CATEGORY_NAME, COLUMN_ID + " = ?", new String[]{categoryid});
            database.close();
            return true;
        }
        return false;
    }

}
