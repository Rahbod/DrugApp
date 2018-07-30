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
import com.example.behnam.app.database.Interference;
import com.example.behnam.app.database.Reminder;
import com.example.behnam.reminder.ReminderModel;

import org.json.JSONArray;
import org.json.JSONException;

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
    private static final String FAVORITE = "favorite";

    private static final String KEY_ID_REMINDER = "id";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_COUNT = "use_count";
    private static final String KEY_PERIOD_TIME = "period_time";
    private static final String KEY_SHOW_COUNT = "show_count";

    private static final String KEY_ID_INTERFERENCE = "id";
    private static final String KEY_MODEL = "model";
    private static final String KEY_MODEL_ID = "modelID";
    private static final String KEY_TEXT = "text";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " ( "
                + KEY_ID_CATEGORY + " INTEGER ," + KEY_PARENT_ID_CATEGORY + " INTEGER , " + KEY_NAME_CATEGORY + " TEXT , "
                + KEY_TYPE_CATEGORY + " INTEGER" + ")";

        String CREATE_TABLE_CATEGORY_DRUG = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORY_DRUG + " ( "
                + KEY_ID_CATEGORY_DRUG + " INTEGER , " + KEY_DRUG_ID + " INTEGER, " + KEY_CATEGORY_ID + " INTEGER )";

        String CREATE_TABLE_DRUG = "CREATE TABLE IF NOT EXISTS " + TABLE_DRUGS + " ( "
                + KEY_ID_DRUG + " INTEGER ," + KEY_NAME_DRUG + " TEXT NOT NULL ," + KEY_NAME_PERSIAN_DRUG + " TEXT ,"
                + KEY_BRAND + " TEXT , " + KEY_PREGNANCY + " TEXT ," + KEY_LACTATION + " TEXT ,"
                + KEY_KIDS + " TEXT ," + KEY_SENIORS + " TEXT ," + KEY_HOW_TO_USE + " TEXT , "
                + KEY_PRODUCT + " TEXT , " + KEY_PHARMACODYNAMIC + " TEXT ," + KEY_USAGE + " TEXT ,"
                + KEY_PROHIBITION + " TEXT ," + KEY_CAUTION + " TEXT ," + KEY_DOSE_ADJUSTMENT + " TEXT , "
                + KEY_COMPLICATION + " TEXT , " + KEY_INTERFERENCE + " TEXT , " + KEY_EFFECT_ON_TEST + " TEXT , "
                + KEY_OVER_DOSE + " TEXT , " + KEY_DESCRIPTION + " TEXT , " + KEY_RELATION_WITH_FOOD + " TEXT , "
                + KEY_STATUS + " INTEGER , " + KEY_LAST_MODIFIED + " TEXT , " + FAVORITE + " INTEGER DEFAULT 0 , "
                + KEY_COMPOUNDS + " TEXT , " + KEY_EFFECTIVE_INGREDIENTS + " TEXT , " + KEY_STANDARDIZED + " TEXT , "
                + KEY_MAINTENANCE + " TEXT , " + KEY_COMPANY + " TEXT , " + KEY_VEGETAL + " INTEGER "
                + ")";

        String CREATE_TABLE_REMINDERS = "CREATE TABLE IF NOT EXISTS " + TABLE_REMINDER + " ( "
                + KEY_ID_REMINDER + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_DRUG_ID + " INTEGER , "
                + KEY_START_TIME + " INTEGER , " + KEY_COUNT + " INTEGER , " + KEY_PERIOD_TIME + " INTEGER , " + KEY_SHOW_COUNT + " INTEGER DEFAULT 0 )";

        String CREATE_TABLE_INTERFERENCE = "CREATE TABLE IF NOT EXISTS " + TABLE_INTERFERENCE + " ( "
                + KEY_ID_INTERFERENCE + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_DRUG_ID + " INTEGER , "
                + KEY_MODEL + " TEXT , " + KEY_MODEL_ID + " INTEGER , " + KEY_TEXT + " TEXT )";

        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DRUG);
        db.execSQL(CREATE_TABLE_CATEGORY_DRUG);
        db.execSQL(CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_INTERFERENCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERFERENCE);
        onCreate(db);
    }

    public void addInterference(Interference interference) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_INTERFERENCE, interference.getId());
        values.put(KEY_DRUG_ID, interference.getDrugID());
        values.put(KEY_MODEL, interference.getModel());
        values.put(KEY_MODEL_ID, interference.getModelID());
        values.put(KEY_TEXT, interference.getText());

        db.insert(TABLE_INTERFERENCE, null, values);
        db.close();
    }

    public void addCategory(Category category) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_CATEGORY, category.getId());
        values.put(KEY_NAME_CATEGORY, category.getName());
        values.put(KEY_TYPE_CATEGORY, category.getType());
        values.put(KEY_PARENT_ID_CATEGORY, category.getParentID());

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void addReminder(ReminderModel reminderModel) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DRUG_ID, reminderModel.getDrugID());
        values.put(KEY_START_TIME, reminderModel.getStartTime());
        values.put(KEY_COUNT, reminderModel.getCount());
        values.put(KEY_PERIOD_TIME, reminderModel.getPeriodTime());
        values.put(KEY_SHOW_COUNT, reminderModel.getShowCount());

        db.insert(TABLE_REMINDER, null, values);
    }

    public ReminderModel getReminder(int id) {
        ReminderModel reminderModel = new ReminderModel();
        String query = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_ID_REMINDER + " = " + id;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                reminderModel.setDrugID(cursor.getInt(cursor.getColumnIndex(KEY_DRUG_ID)));
                reminderModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_REMINDER)));
                reminderModel.setPeriodTime(cursor.getInt(cursor.getColumnIndex(KEY_PERIOD_TIME)));
                reminderModel.setCount(cursor.getInt(cursor.getColumnIndex(KEY_COUNT)));
                reminderModel.setStartTime(cursor.getLong(cursor.getColumnIndex(KEY_START_TIME)));
                reminderModel.setShowCount(cursor.getInt(cursor.getColumnIndex(KEY_SHOW_COUNT)));
            }
            return reminderModel;
        } else return null;
    }

    public List<Integer> getCurrentReminders() {
        List<Integer> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDER + " WHERE " + KEY_SHOW_COUNT + " < " + KEY_COUNT;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID_REMINDER));
                list.add(id);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public int getMaxID() {
        String query = "SELECT MAX(id) FROM " + TABLE_REMINDER;
        db = this.getReadableDatabase();
        int MaxID = 0;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            MaxID = cursor.getInt(0);
        }
        return MaxID;
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
                reminder.setDrugID(cursor.getInt(1));
                reminder.setStartTime(cursor.getInt(2));
                reminder.setCount(cursor.getInt(3));
                reminder.setPeriodTime(cursor.getInt(4));
                reminder.setRowCount(cursor.getInt(5));

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
        values.put(KEY_NAME_PERSIAN_DRUG, drug.getNamePersian());
        values.put(KEY_BRAND, drug.getBrand());
        values.put(KEY_PREGNANCY, drug.getPregnancy());
        values.put(KEY_LACTATION, drug.getLactation());
        values.put(KEY_KIDS, drug.getKids());
        values.put(KEY_SENIORS, drug.getSeniors());
        values.put(KEY_HOW_TO_USE, drug.getHowToUse());
        values.put(KEY_PRODUCT, drug.getProduct());
        values.put(KEY_PHARMACODYNAMIC, drug.getPharmacodynamic());
        values.put(KEY_USAGE, drug.getUsage());
        values.put(KEY_PROHIBITION, drug.getProhibition());
        values.put(KEY_CAUTION, drug.getCaution());
        values.put(KEY_DOSE_ADJUSTMENT, drug.getDoseAdjustment());
        values.put(KEY_COMPLICATION, drug.getComplication());
        values.put(KEY_INTERFERENCE, drug.getInterference());
        values.put(KEY_EFFECT_ON_TEST, drug.getEffectOnTest());
        values.put(KEY_OVER_DOSE, drug.getOverDose());
        values.put(KEY_DESCRIPTION, drug.getDescription());
        values.put(KEY_RELATION_WITH_FOOD, drug.getRelationWithFood());
        values.put(KEY_STATUS, drug.getStatus());
        values.put(KEY_LAST_MODIFIED, drug.getLastModified());
        values.put(KEY_COMPOUNDS, drug.getCompounds());
        values.put(KEY_EFFECTIVE_INGREDIENTS, drug.getEffectiveIngredients());
        values.put(KEY_MAINTENANCE, drug.getMaintenance());
        values.put(KEY_COMPANY, drug.getCompany());
        values.put(KEY_STANDARDIZED, drug.getStandardized());
        values.put(KEY_VEGETAL, drug.getVegetal());

        db.insert(TABLE_DRUGS, null, values);
        db.close();
    }

    public Drug getDrug(int id) {
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DRUGS, new String[]{KEY_ID_DRUG, KEY_NAME_DRUG, KEY_NAME_PERSIAN_DRUG
                , KEY_BRAND, KEY_PREGNANCY, KEY_LACTATION, KEY_KIDS, KEY_SENIORS, KEY_HOW_TO_USE, KEY_PRODUCT
                , KEY_PHARMACODYNAMIC, KEY_USAGE, KEY_PROHIBITION, KEY_CAUTION, KEY_DOSE_ADJUSTMENT
                , KEY_COMPLICATION, KEY_INTERFERENCE, KEY_EFFECT_ON_TEST, KEY_OVER_DOSE, KEY_DESCRIPTION
                , KEY_RELATION_WITH_FOOD, KEY_STATUS, KEY_LAST_MODIFIED, KEY_COMPOUNDS, KEY_EFFECTIVE_INGREDIENTS
                , KEY_STANDARDIZED, KEY_MAINTENANCE, KEY_COMPANY, KEY_VEGETAL}, KEY_ID_DRUG + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Drug(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                    , cursor.getString(5), cursor.getString(6), cursor.getString(7)
                    , cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13)
                    , cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17), cursor.getString(18), cursor.getString(19)
                    , cursor.getString(20), Integer.parseInt(cursor.getString(21)), cursor.getString(22), cursor.getString(cursor.getColumnIndex(KEY_COMPOUNDS)), cursor.getString(cursor.getColumnIndex(KEY_EFFECTIVE_INGREDIENTS))
                    , cursor.getString(cursor.getColumnIndex(KEY_STANDARDIZED)), cursor.getString(cursor.getColumnIndex(KEY_MAINTENANCE)), cursor.getString(cursor.getColumnIndex(KEY_COMPANY)), cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
        } else return null;
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
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                category.setParentID(cursor.getInt(cursor.getColumnIndex(KEY_PARENT_ID_CATEGORY)));
                category.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE_CATEGORY)));
                list.add(category);
            }
        }
        return list;
    }

    public List<Drug> getAllDrugs() {
        List<Drug> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_VEGETAL + " = 0 ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex("id")));
                drug.setName(cursor.getString(1));
                drug.setNamePersian(cursor.getString(2));
                drug.setBrand(cursor.getString(3));
                drug.setPregnancy(cursor.getString(4));
                drug.setLactation(cursor.getString(5));
                drug.setKids(cursor.getString(6));
                drug.setSeniors(cursor.getString(7));
                drug.setHowToUse(cursor.getString(8));
                drug.setProduct(cursor.getString(9));
                drug.setPharmacodynamic(cursor.getString(10));
                drug.setUsage(cursor.getString(11));
                drug.setProhibition(cursor.getString(12));
                drug.setCaution(cursor.getString(13));
                drug.setDoseAdjustment(cursor.getString(14));
                drug.setComplication(cursor.getString(15));
                drug.setInterference(cursor.getString(16));
                drug.setEffectOnTest(cursor.getString(17));
                drug.setOverDose(cursor.getString(18));
                drug.setDescription(cursor.getString(19));
                drug.setRelationWithFood(cursor.getString(20));
                drug.setStatus(Integer.parseInt(cursor.getString(21)));
                drug.setLastModified(cursor.getString(22));
                drug.setCompounds(cursor.getString(cursor.getColumnIndex(KEY_COMPOUNDS)));
                drug.setEffectiveIngredients(cursor.getString(cursor.getColumnIndex(KEY_EFFECTIVE_INGREDIENTS)));
                drug.setStandardized(cursor.getString(cursor.getColumnIndex(KEY_STANDARDIZED)));
                drug.setMaintenance(cursor.getString(cursor.getColumnIndex(KEY_MAINTENANCE)));
                drug.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                drugList.add(drug);
            }
            while (cursor.moveToNext());
        }
        return drugList;
    }

    public List<Drug> getAllDrugsNotInReminder() {
        List<Drug> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_ID_DRUG + " NOT IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_REMINDER + " )";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex("id")));
                drug.setName(cursor.getString(1));
                drug.setNamePersian(cursor.getString(2));
                drug.setBrand(cursor.getString(3));
                drug.setPregnancy(cursor.getString(4));
                drug.setLactation(cursor.getString(5));
                drug.setKids(cursor.getString(6));
                drug.setSeniors(cursor.getString(7));
                drug.setHowToUse(cursor.getString(8));
                drug.setProduct(cursor.getString(9));
                drug.setPharmacodynamic(cursor.getString(10));
                drug.setUsage(cursor.getString(11));
                drug.setProhibition(cursor.getString(12));
                drug.setCaution(cursor.getString(13));
                drug.setDoseAdjustment(cursor.getString(14));
                drug.setComplication(cursor.getString(15));
                drug.setInterference(cursor.getString(16));
                drug.setEffectOnTest(cursor.getString(17));
                drug.setOverDose(cursor.getString(18));
                drug.setDescription(cursor.getString(19));
                drug.setRelationWithFood(cursor.getString(20));
                drug.setStatus(Integer.parseInt(cursor.getString(21)));
                drug.setLastModified(cursor.getString(22));
                drug.setCompounds(cursor.getString(cursor.getColumnIndex(KEY_COMPOUNDS)));
                drug.setEffectiveIngredients(cursor.getString(cursor.getColumnIndex(KEY_EFFECTIVE_INGREDIENTS)));
                drug.setStandardized(cursor.getString(cursor.getColumnIndex(KEY_STANDARDIZED)));
                drug.setMaintenance(cursor.getString(cursor.getColumnIndex(KEY_MAINTENANCE)));
                drug.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                drugList.add(drug);
            }
            while (cursor.moveToNext());
        }
        return drugList;
    }

    public void addCategoryDrug(CategoryDrug categoryDrug) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, categoryDrug.getCategoryID());
        values.put(KEY_DRUG_ID, categoryDrug.getDrugID());
        values.put(KEY_ID_CATEGORY_DRUG, categoryDrug.getId());

        db.insert(TABLE_CATEGORY_DRUG, null, values);
        db.close();
    }

    public long getCount(String table) {
        db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, table);
        db.close();
        return count;
    }

    public void bookMark(int id) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + FAVORITE + " FROM " + TABLE_DRUGS + " WHERE id =" + id, null);
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
        Cursor cursor = db.rawQuery("SELECT " + FAVORITE + " FROM " + TABLE_DRUGS + " WHERE id =" + id, null);
        return cursor.moveToFirst() && cursor.getInt(cursor.getColumnIndex("favorite")) == 1;
    }

    public List<Drug> getFavorite() {
        List<Drug> listFavorite = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DRUGS + " WHERE " + FAVORITE + " = 1", null);
        if (cursor.moveToFirst()) {
            do {
                Drug drug = new Drug();
                drug.setFavorite(cursor.getInt(cursor.getColumnIndex(FAVORITE)));
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                listFavorite.add(drug);
            }
            while (cursor.moveToNext());
        }
        return listFavorite;
    }

    public List<Category> getHealingCategory(int drugID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " IN (SELECT category_id FROM category_drug WHERE drug_id = " + drugID + ") AND type = " + Category.TYPE_HEALING;
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

    public List<Category> getPharmaCategory(int drugID) {
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ID_CATEGORY + " IN (SELECT category_id FROM category_drug WHERE drug_id = " + drugID + ") AND type = " + Category.TYPE_HEALING;
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                List<Category> tempList = getPharmaParents(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                list.addAll(tempList);
            }
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

    public Reminder countDrugReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_REMINDER + " where id = " + id, null);
        cursor.moveToFirst();
        return new Reminder(cursor.getInt(0)
                , cursor.getInt(1)
                , cursor.getInt(2)
                , cursor.getInt(3)
                , cursor.getInt(4)
                , cursor.getInt(5));

    }

    public List<Reminder> getAllCountDrugReminder() {
        List<Reminder> reminderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + KEY_COUNT + " from " + TABLE_REMINDER, null);
        Reminder reminder = new Reminder();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                reminder.setId(Integer.parseInt(cursor.getString(0)));
                reminder.setDrugID(Integer.parseInt(cursor.getString(1)));
                reminder.setStartTime(Integer.parseInt(cursor.getString(2)));
                reminder.setCount(Integer.parseInt(cursor.getString(3)));
                reminder.setPeriodTime(Integer.parseInt(cursor.getString(4)));
                reminder.setRowCount(Integer.parseInt(cursor.getString(5)));

                reminderList.add(reminder);
            }
        }
        return reminderList;
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
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_CATEGORY_ID + " = " + id + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                drug.setNamePersian(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
                drug.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                list.add(drug);
            }
        }
        return list;
    }

    public List<Drug> getDrugInterference(int id) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_DRUG_ID + " = " + id + " AND " + KEY_MODEL + " = 'drg' )";
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                list.add(drug);
            }
        }
        return list;
    }

    public List<Category> getCategoryInterference(int id) {
        List<Category> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE id IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_DRUG_ID + " = " + id + " AND " + KEY_MODEL + " = 'cat' )";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_CATEGORY)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_CATEGORY)));
                list.add(category);
            }
        }
        return list;
    }

    public List<Drug> getListDrugInterference() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE id IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_INTERFERENCE + ")";
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                list.add(drug);
            }
        }
        return list;
    }

    public List<Drug> getVegetalDrug() {
        List<Drug> drugList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_VEGETAL + " = 1 ";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex("id")));
                drug.setName(cursor.getString(1));
                drug.setNamePersian(cursor.getString(2));
                drug.setBrand(cursor.getString(3));
                drug.setPregnancy(cursor.getString(4));
                drug.setLactation(cursor.getString(5));
                drug.setKids(cursor.getString(6));
                drug.setSeniors(cursor.getString(7));
                drug.setHowToUse(cursor.getString(8));
                drug.setProduct(cursor.getString(9));
                drug.setPharmacodynamic(cursor.getString(10));
                drug.setUsage(cursor.getString(11));
                drug.setProhibition(cursor.getString(12));
                drug.setCaution(cursor.getString(13));
                drug.setDoseAdjustment(cursor.getString(14));
                drug.setComplication(cursor.getString(15));
                drug.setInterference(cursor.getString(16));
                drug.setEffectOnTest(cursor.getString(17));
                drug.setOverDose(cursor.getString(18));
                drug.setDescription(cursor.getString(19));
                drug.setRelationWithFood(cursor.getString(20));
                drug.setStatus(Integer.parseInt(cursor.getString(21)));
                drug.setLastModified(cursor.getString(22));
                drug.setCompounds(cursor.getString(cursor.getColumnIndex(KEY_COMPOUNDS)));
                drug.setEffectiveIngredients(cursor.getString(cursor.getColumnIndex(KEY_EFFECTIVE_INGREDIENTS)));
                drug.setStandardized(cursor.getString(cursor.getColumnIndex(KEY_STANDARDIZED)));
                drug.setMaintenance(cursor.getString(cursor.getColumnIndex(KEY_MAINTENANCE)));
                drug.setCompany(cursor.getString(cursor.getColumnIndex(KEY_COMPANY)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                drugList.add(drug);
            }
            while (cursor.moveToNext());
        }
        return drugList;
    }

    public List<Drug> getAllInterferenceDrug(int id) {
        List<Drug> drugList = getDrugInterference(id);
        List<Drug> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS + " WHERE " + KEY_ID_DRUG + " IN (SELECT " + KEY_DRUG_ID + " FROM " + TABLE_CATEGORY_DRUG + " WHERE " + KEY_CATEGORY_ID + " IN (SELECT " + KEY_MODEL_ID + " FROM " + TABLE_INTERFERENCE + " WHERE " + KEY_DRUG_ID + " = " + id + " AND " + KEY_MODEL + " = 'cat'))";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                drug.setNamePersian(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
                drug.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                list.add(drug);
            }
        }
        drugList.addAll(list);
        return drugList;
    }

    public List<Drug> getDrugs() {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUGS;
        Cursor cursor = db.rawQuery(query, null);
        List<Drug> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drug drug = new Drug();
                drug.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_DRUG)));
                drug.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME_DRUG)));
                drug.setNamePersian(cursor.getString(cursor.getColumnIndex(KEY_NAME_PERSIAN_DRUG)));
                drug.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
                drug.setVegetal(cursor.getString(cursor.getColumnIndex(KEY_VEGETAL)));
                list.add(drug);
            }
        }
        return list;
    }
}