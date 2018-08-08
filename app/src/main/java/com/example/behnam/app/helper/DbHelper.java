package com.example.behnam.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Index;
import com.example.behnam.app.database.Reminder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Drug";
    private SQLiteDatabase db;

    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_DRUGS = "drugs";
    private static final String TABLE_CATEGORY_DRUG = "category_drug";
    private static final String TABLE_REMINDER = "reminders";
    private static final String TABLE_INTERFERENCE = "interference";
    private static final String TABLE_INDEX = "indexes";

    private static final String KEY_ID_CATEGORY = "id";
    private static final String KEY_NAME_CATEGORY = "name";
    private static final String KEY_TYPE_CATEGORY = "type";
    private static final String KEY_PARENT_ID_CATEGORY = "parent_id";

    private static final String KEY_ID_CATEGORY_DRUG = "id";
    private static final String KEY_DRUG_ID = "drug_id";
    private static final String KEY_CATEGORY_ID = "category_id";

    private static final String KEY_ID_DRUG = "id";
    private static final String KEY_NAME_DRUG = "name";
    private static final String KEY_NAME_PERSIAN_DRUG = "persian";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_PREGNANCY = "pregnancy";
    private static final String KEY_LACTATION = "lactation";
    private static final String KEY_KIDS = "kids";
    private static final String KEY_SENIORS = "seniors";
    private static final String KEY_HOW_TO_USE = "how_to_use";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_PHARMACODYNAMIC = "pharmacodynamic";
    private static final String KEY_USAGE = "usage";
    private static final String KEY_PROHIBITION = "prohibition";
    private static final String KEY_CAUTION = "caution";
    private static final String KEY_DOSE_ADJUSTMENT = "dose_adjustment";
    private static final String KEY_COMPLICATION = "complication";
    private static final String KEY_INTERFERENCE = "interference";
    private static final String KEY_EFFECT_ON_TEST = "effect_on_test";
    private static final String KEY_OVER_DOSE = "over_dose";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_RELATION_WITH_FOOD = "relation_with_food";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LAST_MODIFIED = "last_modified";
    private static final String KEY_COMPOUNDS = "compounds";
    private static final String KEY_EFFECTIVE_INGREDIENTS = "effective_ingredients";
    private static final String KEY_STANDARDIZED = "standardized";
    private static final String KEY_MAINTENANCE = "maintenance";
    private static final String KEY_COMPANY = "company";
    private static final String KEY_VEGETAL = "vegetal";
    private static final String KEY_FAVORITE = "favorite";

    private static final String KEY_ID_REMINDER = "id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_COUNT = "use_count";
    private static final String KEY_PERIOD_TIME = "period_time";
    private static final String KEY_SHOW_COUNT = "show_count";

    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_ID_INTERFERENCE = "id";
    private static final String KEY_MODEL = "model";
    private static final String KEY_MODEL_ID = "modelID";
    private static final String KEY_TEXT = "text";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_INDEX = "CREATE TABLE IF NOT EXISTS " + TABLE_INDEX + " (" + KEY_ID_DRUG + " INTEGER, "
                + KEY_NAME_DRUG + " TEXT, " + KEY_BRAND + " TEXT, " + KEY_NAME_PERSIAN_DRUG + " TEXT ," + KEY_VEGETAL +
                " INTEGER )";

        String
                CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " ( "
                + KEY_ID_CATEGORY + " INTEGER ," + KEY_PARENT_ID_CATEGORY + " INTEGER , " + KEY_NAME_CATEGORY + " TEXT , "
                + KEY_TYPE_CATEGORY + " INTEGER" + ")";

        String CREATE_TABLE_CATEGORY_DRUG = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_DRUG + " ( "
                + KEY_ID_CATEGORY_DRUG + " INTEGER , " + KEY_DRUG_ID + " INTEGER, " + KEY_CATEGORY_ID + " INTEGER )";


        String CREATE_TABLE_REMINDERS = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + " ( "
                + KEY_ID_REMINDER + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_DRUG_ID + " INTEGER , "
                + KEY_START_TIME + " INTEGER , " + KEY_COUNT + " INTEGER , " + KEY_PERIOD_TIME + " INTEGER , "
                + KEY_SHOW_COUNT + " INTEGER DEFAULT 0 )";

        String CREATE_TABLE_INTERFERENCE = "CREATE TABLE IF NOT EXISTS " + TABLE_INTERFERENCE + " ( "
                + KEY_ID_INTERFERENCE + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_DRUG_ID + " INTEGER , "
                + KEY_MODEL + " TEXT , " + KEY_MODEL_ID + " INTEGER , " + KEY_TEXT + " TEXT )";

        String CREATE_TABLE_DRUG = "CREATE TABLE IF NOT EXISTS " + TABLE_DRUGS + " ( " + KEY_ID + " INTEGER , "
                + KEY_CONTENT + " TEXT, " + KEY_FAVORITE + " INTEGER )";

        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DRUG);
        db.execSQL(CREATE_TABLE_CATEGORY_DRUG);
        db.execSQL(CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_INTERFERENCE);
        db.execSQL(CREATE_TABLE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERFERENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEX);
        onCreate(db);
    }

    public void addReminder(Reminder reminder) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DRUG_ID, reminder.getDrugID());
        values.put(KEY_START_TIME, reminder.getStartTime());
        values.put(KEY_COUNT, reminder.getCount());
        values.put(KEY_PERIOD_TIME, reminder.getPeriodTime());
        values.put(KEY_SHOW_COUNT, reminder.getShowCount());

        db.insert(TABLE_REMINDER, null, values);
    }

    public Reminder getReminder(int id) {
        Reminder reminder = new Reminder();
        String query = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_ID_REMINDER + " = " + id;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            reminder.setDrugID(cursor.getInt(cursor.getColumnIndex(KEY_DRUG_ID)));
            reminder.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_REMINDER)));
            reminder.setPeriodTime(cursor.getInt(cursor.getColumnIndex(KEY_PERIOD_TIME)));
            reminder.setCount(cursor.getInt(cursor.getColumnIndex(KEY_COUNT)));
            reminder.setStartTime(cursor.getLong(cursor.getColumnIndex(KEY_START_TIME)));
            reminder.setShowCount(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_COUNT)));
        }
        return reminder;
    }

    public List<Integer> getCurrentReminders() {
        List<Integer> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_SHOW_COUNT + " < " + KEY_COUNT;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID_REMINDER));
            list.add(id);
            cursor.moveToNext();
        }
        return list;
    }

    public int getMaxID() {
        String query = "SELECT MAX(id) FROM " + TABLE_REMINDER;
        db = this.getReadableDatabase();
        int MaxID = 0;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            MaxID = cursor.getInt(0);
        }
        return MaxID;
    }

    public List<Reminder> getAllReminder() {
        List<Reminder> reminderList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDER;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Reminder reminder = new Reminder();
            reminder.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            reminder.setDrugID(cursor.getInt(cursor.getColumnIndex(KEY_DRUG_ID)));
            reminder.setStartTime(cursor.getInt(cursor.getColumnIndex(KEY_START_TIME)));
            reminder.setCount(cursor.getInt(cursor.getColumnIndex(KEY_COUNT)));
            reminder.setPeriodTime(cursor.getInt(cursor.getColumnIndex(KEY_PERIOD_TIME)));
            reminder.setShowCount(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_COUNT)));
            reminderList.add(reminder);
        }
        return reminderList;
    }

    public Drug getDrug(int id) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_ID_DRUG + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        Drug drug = new Drug();
        while (cursor.moveToNext()) {
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
        }
        return drug;
    }

    public List<Category> getCategories(int type) {
        return getCategories(type, "");
    }

    public List<Category> getCategories(int type, String condition) {
        if (condition.isEmpty())
            condition = "1 = 1";
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_TYPE_CATEGORY + " = " + type + " AND " + condition;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
            category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
            category.setParentID(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)));
            category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));
            list.add(category);
        }
        return list;
    }

    public List<Index> getAllDrugs() {
        List<Index> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_INDEX + " WHERE " + KEY_VEGETAL + " = 0 ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Index index = new Index();
            index.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            index.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
            index.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            index.setFa_name(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
            index.setVegetal(cursor.getInt(cursor.getColumnIndex(KEY_VEGETAL)));
            drugList.add(index);
        }
        return drugList;
    }

    public List<Index> getAllDrugsNotInReminder() {
        List<Index> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_INDEX + " WHERE " + KEY_ID_DRUG + " NOT IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_REMINDER + " )";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Index index = new Index();
            index.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            index.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
            index.setFa_name(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
            index.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            index.setVegetal(cursor.getInt(cursor.getColumnIndex(KEY_VEGETAL)));
            drugList.add(index);
        }
        return drugList;
    }

    public long getCount(String table) {
        db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, table);
        db.close();
        return count;
    }

    public void bookMark(int id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_FAVORITE + " FROM " + TABLE_DRUGS + " WHERE id =" + id, null);
        ContentValues contentValues = new ContentValues();
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex("favorite")) == 0) {
                contentValues.put("favorite", 1);
                db.update(TABLE_DRUGS, contentValues, "id = " + id, null);
            } else {
                contentValues.put("favorite", 0);
                db.update(TABLE_DRUGS, contentValues, "id = " + id, null);
            }
        }
    }

    public boolean checkFavorite(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_FAVORITE + " FROM " + TABLE_DRUGS + " WHERE id =" + id, null);
        return cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndex("favorite")) == 1;
    }

    public List<Drug> getFavorite() {
        List<Drug> listFavorite = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_FAVORITE + " = 1", null);
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            drug.setFavorite(cursor.getInt(cursor.getColumnIndex(KEY_FAVORITE)));
            listFavorite.add(drug);
        }
        return listFavorite;
    }

    public List<Category> getHealingCategory(int drugID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " IN (SELECT category_id FROM category_drug WHERE drug_id = " + drugID + ") AND type = " + Category.TYPE_HEALING;
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)) == 0) {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));

                list.add(category);
            } else {
                Category category = getBaseParent(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)));

                list.add(category);
            }
        }
        return list;
    }

    public List<Category> getPharmaCategory(int drugID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " IN (SELECT category_id FROM category_drug WHERE drug_id = " + drugID + ") AND type = " + Category.TYPE_HEALING;
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            List<Category> tempList = getPharmaParents(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
            list.addAll(tempList);
        }
        return list;
    }

    public List<Category> getSicknessCategory(int drugID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " IN (SELECT category_id FROM category_drug WHERE drug_id = " + drugID + ") AND type = " + Category.TYPE_SICKNESS;
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)) == 0) {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                    category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                    category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));

                    list.add(category);
                } else {
                    Category category = getBaseParent(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)));

                    list.add(category);
                }
            }
        }
        return list;
    }

    private List<Category> getPharmaParents(int categoryID) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " = " + categoryID;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int parentID = cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY));
            while (parentID != 0) {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));
                list.add(category);

                query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " = " + parentID;
                cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst())
                    parentID = cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY));
                else parentID = 0;
            }
        }

        return list;
    }

    private Category getBaseParent(int pID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " = " + pID;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)) == 0) {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));

                return category;
            } else {
                return getBaseParent(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)));
            }
        }
        return null;
    }

    public int incrementRowCountReminder(int val, int id) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SHOW_COUNT, val);
        return db.update(TABLE_REMINDER, contentValues, " id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteReminder(int id) {
        String delete = "DELETE FROM reminders WHERE id= ";
        db.execSQL(delete + id);
    }

    public List<Drug> getCategoryDrug(int id) {
        List<Drug> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_ID_DRUG + " IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_CATEGORY_ID + " IN (SELECT "+ KEY_PARENT_ID_CATEGORY + " FROM "+ TABLE_CATEGORIES +" WHERE " + KEY_TYPE_CATEGORY + " = 0))";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            list.add(drug);
        }
        return list;
    }

    //
    public List<Drug> getAllDrugInterference(int id) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_MODEL + " = 'drg' AND " + KEY_DRUG_ID + " = " + id + ")";
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            list.add(drug);
        }

        return list;
    }

    public List<Category> getCategoryInterference(int id) {
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE id IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_MODEL + " = 'cat' AND " + KEY_DRUG_ID + " = " + id + ")";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
            category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
            list.add(category);
        }
        return list;
    }

    public List<Drug> getListDrugInterference() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_INTERFERENCE + ")";
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            list.add(drug);
        }
        return list;
    }

    public List<Index> getVegetalDrug() {
        List<Index> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_INDEX + " WHERE " + KEY_VEGETAL + " = 1 ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Index index = new Index();
            index.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            index.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
            index.setFa_name(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
            index.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            index.setVegetal(cursor.getInt(cursor.getColumnIndex(KEY_VEGETAL)));
            drugList.add(index);
        }
        return drugList;
    }

    public List<Drug> getAllInterferenceDrug(int id) {
        List<Drug> drugList = getDrugInterference(id);
        List<Drug> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_ID_DRUG + " IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_CATEGORY_ID + " IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_DRUG_ID + " = " + id + " AND " + KEY_MODEL + " = 'cat'))";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            list.add(drug);
        }
        drugList.addAll(list);
        cursor.close();
        return drugList;
    }

    public List<Drug> getDrugInterference(int id) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_DRUG_ID + " = " + id + " AND " + KEY_MODEL + " = 'drg' )";
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Drug drug = new Drug();
            drug.setContent(getDrugContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT))));
            drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            list.add(drug);
        }
        cursor.close();
        return list;
    }

    public List<Index> getDrugs() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INDEX;
        Cursor cursor = db.rawQuery(query, null);
        List<Index> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Index index = new Index();
            index.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
            index.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
            index.setFa_name(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
            index.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
            index.setVegetal(cursor.getInt(cursor.getColumnIndex(KEY_VEGETAL)));
            list.add(index);
        }
        return list;
    }

    public void execSQL(String query) {
        db = this.getWritableDatabase();
        db.execSQL(query);
    }

    private JSONObject getDrugContent(String data) {
        JSONObject object = null;
        try {
            object = new JSONObject(Components.decrypt(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}