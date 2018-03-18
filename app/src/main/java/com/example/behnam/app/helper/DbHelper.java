package com.example.behnam.app.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.behnam.app.database.Category;
import com.example.behnam.app.database.CategoryDrug;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.database.Reminder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Drug";
    private SQLiteDatabase db;

    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_DRUGS = "drugs";
    private static final String TABLE_CATEGORY_DRUG = "category_drug";
    private static final String TABLE_INDEX = "indexes";
    private static final String TABLE_REMINDER = "reminders";

    private static final String KEY_ID_CATEGORY = "id";
    private static final String KEY_NAME_CATEGORY = "name";
    private static final String KEY_TYPE_CATEGORY = "type";

    private static final String KEY_DRUG_ID = "drug_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_TYPE = "type";


    private static final String KEY_ID_DRUG = "id";
    private static final String KEY_NAME_DRUG = "name";
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
    private static final String FAVORITE = "favorite";


    private static final String KEY_ID_REMINDER = "id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_COUNT = "use_count";
    private static final String KEY_PERIOD_TIME = "period_time";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " ( "
                + KEY_ID_CATEGORY + " INTEGER , " + KEY_NAME_CATEGORY + " TEXT , "
                + KEY_TYPE_CATEGORY + " INTEGER" + ")";

        String CREATE_TABLE_CATEGORY_DRUG = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_DRUG + " ( "
                + KEY_DRUG_ID + " INTEGER, " + KEY_CATEGORY_ID + " INTEGER , "
                + KEY_TYPE + " INTEGER  " +
//                " PRIMARY KEY ( " + KEY_DRUG_ID + ", " + KEY_CATEGORY_ID + ") " +
//                " FOREIGN KEY ( " + KEY_DRUG_ID + ") PREFERENCE " + TABLE_DRUGS + " ( " + KEY_DRUG_ID + " ) " +
//                " ON DELETE CASCADE ON UPDATE NO ACTION ," +
//                " FOREIGN KEY ( " + KEY_CATEGORY_ID + " ) PREFERENCE " + TABLE_CATEGORIES + " ( " + KEY_CATEGORY_ID + " ) " +
//                " ON DELETE CASCADE ON UPDATE NO ACTION " +
                ")";

        String CREATE_TABLE_DRUG = "CREATE TABLE " + TABLE_DRUGS + " ( "
                + KEY_ID_DRUG + " INTEGER ," + KEY_NAME_DRUG + " TEXT NOT NULL ,"
                + KEY_BRAND + " TEXT , " + KEY_PREGNANCY + " TEXT ," + KEY_LACTATION + " TEXT ,"
                + KEY_KIDS + " TEXT ," + KEY_SENIORS + " TEXT ," + KEY_HOW_TO_USE + " TEXT , "
                + KEY_PRODUCT + " TEXT , " + KEY_PHARMACODYNAMIC + " TEXT ," + KEY_USAGE + " TEXT ,"
                + KEY_PROHIBITION + " TEXT ," + KEY_CAUTION + " TEXT ," + KEY_DOSE_ADJUSTMENT + " TEXT , "
                + KEY_COMPLICATION + " TEXT , " + KEY_INTERFERENCE + " TEXT , " + KEY_EFFECT_ON_TEST + " TEXT , "
                + KEY_OVER_DOSE + " TEXT , " + KEY_DESCRIPTION + " TEXT , " + KEY_RELATION_WITH_FOOD + " TEXT , "
                + KEY_STATUS + " INTEGER , " + KEY_LAST_MODIFIED + " TEXT , " + FAVORITE + " INTEGER DEFAULT 0"
                + ")";

        String CREATE_TABLE_REMINDERS = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + " ( "
                + KEY_ID_REMINDER + " INTEGER , " + KEY_DRUG_ID + " INTEGER , "
                + KEY_START_TIME + " INTEGER , " + KEY_COUNT + " INTEGER , " + KEY_PERIOD_TIME + " INTEGER " + ")";

        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DRUG);
        db.execSQL(CREATE_TABLE_CATEGORY_DRUG);
        db.execSQL(CREATE_TABLE_REMINDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        onCreate(db);
    }

    public void addCategory(Category category) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CATEGORY, category.getName());
        values.put(KEY_TYPE_CATEGORY, category.getType());

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void addReminder(Reminder reminder) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DRUG_ID, reminder.getDrugId());
        values.put(KEY_START_TIME, reminder.getStart_time());
        values.put(KEY_COUNT, reminder.getCount());
        values.put(KEY_PERIOD_TIME, reminder.getPeriod_time());

        db.insert(TABLE_REMINDER, null, values);
        db.close();
    }

    public Reminder getReminder(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_ID_REMINDER + " = "+id,null);
        if (cursor != null)
            cursor.moveToFirst();
        return new Reminder(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
    }

    public List<Reminder> getAllReminder() {
        List<Reminder> reminderList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDER;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getInt(cursor.getColumnIndex("id")));
                reminder.setDrugId(cursor.getInt(1));
                reminder.setStart_time(cursor.getInt(2));
                reminder.setCount(cursor.getInt(3));
                reminder.setPeriod_time(cursor.getInt(4));

                reminderList.add(reminder);
            }
            while (cursor.moveToNext());
        }
        return reminderList;
    }

    public void addDrug(Drug drug) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DRUG, drug.getId());
        values.put(KEY_NAME_DRUG, drug.getName());
        values.put(KEY_BRAND, drug.getBrand());
        values.put(KEY_PREGNANCY, drug.getPregnancy());
        values.put(KEY_LACTATION, drug.getLactation());
        values.put(KEY_KIDS, drug.getKids());
        values.put(KEY_SENIORS, drug.getSeniors());
        values.put(KEY_HOW_TO_USE, drug.getHow_to_use());
        values.put(KEY_PRODUCT, drug.getProduct());
        values.put(KEY_PHARMACODYNAMIC, drug.getPharmacodynamic());
        values.put(KEY_USAGE, drug.getUsage());
        values.put(KEY_PROHIBITION, drug.getProhibition());
        values.put(KEY_CAUTION, drug.getCaution());
        values.put(KEY_DOSE_ADJUSTMENT, drug.getDose_adjustment());
        values.put(KEY_COMPLICATION, drug.getComplication());
        values.put(KEY_INTERFERENCE, drug.getInterference());
        values.put(KEY_EFFECT_ON_TEST, drug.getEffect_on_test());
        values.put(KEY_OVER_DOSE, drug.getOver_dose());
        values.put(KEY_DESCRIPTION, drug.getDescription());
        values.put(KEY_RELATION_WITH_FOOD, drug.getRelation_with_food());
        values.put(KEY_STATUS, drug.getStatus());
        values.put(KEY_LAST_MODIFIED, drug.getLast_modified());

        db.insert(TABLE_DRUGS, null, values);
        db.close();
    }

    public Drug getDrug(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DRUGS, new String[]{KEY_ID_DRUG, KEY_NAME_DRUG, KEY_BRAND, KEY_PREGNANCY
                , KEY_LACTATION, KEY_KIDS, KEY_SENIORS, KEY_HOW_TO_USE, KEY_PRODUCT, KEY_PHARMACODYNAMIC, KEY_USAGE, KEY_PROHIBITION
                , KEY_CAUTION, KEY_DOSE_ADJUSTMENT, KEY_COMPLICATION, KEY_INTERFERENCE, KEY_EFFECT_ON_TEST, KEY_OVER_DOSE, KEY_DESCRIPTION
                , KEY_RELATION_WITH_FOOD, KEY_STATUS, KEY_LAST_MODIFIED}, KEY_ID_DRUG + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Drug drug = new Drug(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)
                , cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12)
                , cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18)
                , cursor.getString(19), Integer.parseInt(cursor.getString(20)), cursor.getString(21));
        return drug;
    }

    public List<Drug> getDrugs(JSONArray ids) {
        db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM "+TABLE_DRUGS+" WHERE "+KEY_ID_DRUG+" IN ("+ ids.join(",") +")", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Drug> drugList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex("id")));
                drug.setName(cursor.getString(1));
                drug.setBrand(cursor.getString(2));
                drug.setPregnancy(cursor.getString(3));
                drug.setLactation(cursor.getString(4));
                drug.setKids(cursor.getString(5));
                drug.setSeniors(cursor.getString(6));
                drug.setHow_to_use(cursor.getString(7));
                drug.setProduct(cursor.getString(8));
                drug.setPharmacodynamic(cursor.getString(9));
                drug.setUsage(cursor.getString(10));
                drug.setProhibition(cursor.getString(11));
                drug.setCaution(cursor.getString(12));
                drug.setDose_adjustment(cursor.getString(13));
                drug.setComplication(cursor.getString(14));
                drug.setInterference(cursor.getString(15));
                drug.setEffect_on_test(cursor.getString(16));
                drug.setOver_dose(cursor.getString(17));
                drug.setDescription(cursor.getString(18));
                drug.setRelation_with_food(cursor.getString(19));
                drug.setStatus(Integer.parseInt(cursor.getString(20)));
                drug.setLast_modified(cursor.getString(21));
                drugList.add(drug);
            }
        }
        return drugList;
    }

    public List<Drug> getAllDrugs() {
        List<Drug> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DRUGS;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex("id")));
                drug.setName(cursor.getString(1));
                drug.setBrand(cursor.getString(2));
                drug.setPregnancy(cursor.getString(3));
                drug.setLactation(cursor.getString(4));
                drug.setKids(cursor.getString(5));
                drug.setSeniors(cursor.getString(6));
                drug.setHow_to_use(cursor.getString(7));
                drug.setProduct(cursor.getString(8));
                drug.setPharmacodynamic(cursor.getString(9));
                drug.setUsage(cursor.getString(10));
                drug.setProhibition(cursor.getString(11));
                drug.setCaution(cursor.getString(12));
                drug.setDose_adjustment(cursor.getString(13));
                drug.setComplication(cursor.getString(14));
                drug.setInterference(cursor.getString(15));
                drug.setEffect_on_test(cursor.getString(16));
                drug.setOver_dose(cursor.getString(17));
                drug.setDescription(cursor.getString(18));
                drug.setRelation_with_food(cursor.getString(19));
                drug.setStatus(Integer.parseInt(cursor.getString(20)));
                drug.setLast_modified(cursor.getString(21));
                drugList.add(drug);
            }
            while (cursor.moveToNext());
        }
        return drugList;
    }

    public void addCategoryDrug(CategoryDrug categoryDrug) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, categoryDrug.getCategory_id());
        values.put(KEY_DRUG_ID, categoryDrug.getDrug_id());
        values.put(KEY_TYPE, categoryDrug.getType());

        db.insert(TABLE_CATEGORY_DRUG, null, values);
        db.close();
    }

    public long getCount(String table) {
        db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, table);
        db.close();
        return count;
    }

    public JSONObject checkInterference(int id, String selectedIDs) {
        String[] idsArray = selectedIDs.split(",");
        db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_DRUGS+" d INNER JOIN "+TABLE_CATEGORY_DRUG+" cd  ON d."+KEY_ID_DRUG+" = cd."+KEY_DRUG_ID+" " +
                "WHERE cd.category_id IN (" +
                "SELECT category_id FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_DRUG_ID + "=" + String.valueOf(id) + " AND " + KEY_TYPE + "=1 AND " + KEY_CATEGORY_ID + " IN (SELECT " + KEY_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_TYPE + "=0 AND " + KEY_DRUG_ID + " IN (" + selectedIDs + "))" +
                ") and d."+KEY_ID_DRUG+" <> " + id;
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Integer> nID = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                nID.add(cursor.getInt(cursor.getColumnIndex("id")));
            }
        }
        JSONObject conflicts = new JSONObject();
        for (int i = 0; i < idsArray.length; i++) {
            Boolean hasInterference = false;
            if (nID.contains(Integer.parseInt(idsArray[i])))
                hasInterference = true;
            try {
                conflicts.put(idsArray[i], hasInterference);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return conflicts;
    }

    public void updateDrug(int id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT favorite FROM drugs WHERE id = " + id, null);
        ContentValues contentValues = new ContentValues();
        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex("favorite")) == 0) {
                contentValues.put("favorite", 1);
                db.update(TABLE_DRUGS, contentValues, null, null);
            } else {
                contentValues.put("favorite", 0);
                db.update(TABLE_DRUGS, contentValues, null, null);
            }
        }
    }

}