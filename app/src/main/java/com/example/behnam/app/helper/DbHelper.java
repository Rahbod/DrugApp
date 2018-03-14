package com.example.behnam.app.helper;

import android.content.ContentUris;
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
                + KEY_STATUS + " INTEGER , " + KEY_LAST_MODIFIED + " TEXT"
                + ")";

        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DRUG);
        db.execSQL(CREATE_TABLE_CATEGORY_DRUG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDEX);
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

    public JSONObject checkInterference(int id, String[] selectedIDs) {
        String[] params = new String[selectedIDs.length + 1];
        params[0] = String.valueOf(id);
        for (int i = 0; i < selectedIDs.length; i++)
            params[i + 1] = selectedIDs[i];
        db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_DRUG_ID + "=? AND " + KEY_TYPE + "=1 AND " + KEY_CATEGORY_ID + " IN (SELECT " + KEY_CATEGORY_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_TYPE + "=0 AND " + KEY_DRUG_ID + " IN (" + makePlaceholders(selectedIDs.length) + "));";
        Cursor cursor = db.rawQuery(sql, params);
        JSONObject output = new JSONObject();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_DRUG_ID + "=?", new String[]{params[0]});
        StringBuilder test = new StringBuilder();
        for (int i=0;i<params.length;i++)
            test.append("##").append(params[i]);
        Log.e("masoud", sql);
        Log.e("masoud", test.toString());
        Log.e("masoud", String.valueOf(cursor.getCount()));
        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                // selectedIDs =>  [1,2,6,4]
                // cursor => [2,4]
                // خروجی
                // {"1": false, "2": true, "6": false, "4":true}
                for (int i = 0; i < selectedIDs.length; i++) {
                    Boolean hasInterference = false;
                    Log.e("rrrrrr", c.getString(Integer.parseInt("id")));
                    if (String.valueOf(c.getString(c.getColumnIndex("id"))) == selectedIDs[i])
                        hasInterference = true;
                    try {
                        output.put(selectedIDs[i], hasInterference);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        cursor.close();
        return output;
    }

    String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}