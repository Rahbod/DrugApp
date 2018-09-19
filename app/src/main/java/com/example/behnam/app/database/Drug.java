package com.example.behnam.app.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Drug {
    int id;
    String content;

    //drug field
    String name;
    String faName;
    String brand;
    String pregnancy;
    String lactation;
    String kids;
    String seniors;
    String howToUse;
    String product;
    String pharmacodynamic;
    String usage;
    String prohibition;
    String caution;
    String doseAdjustment;
    String complication;
    String interference;
    String effectOnTest;
    String overdose;
    String description;
    String relationWithFood;
    String compounds;
    String effectiveIngredients;
    String standardized;
    String maintenance;
    String company;
    String vegetal;
    int status;
    String lastModified;
    String categoryName;
    int categoryType;
    boolean checked = false;
    int favorite;

    String TAG = "qqqq";

    public Drug(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaName() {
        return faName;
    }

    public void setFaName(String faName) {
        this.faName = faName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPregnancy() {
        return pregnancy;
    }

    public void setPregnancy(String pregnancy) {
        this.pregnancy = pregnancy;
    }

    public String getLactation() {
        return lactation;
    }

    public void setLactation(String lactation) {
        this.lactation = lactation;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getSeniors() {
        return seniors;
    }

    public void setSeniors(String seniors) {
        this.seniors = seniors;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPharmacodynamic() {
        return pharmacodynamic;
    }

    public void setPharmacodynamic(String pharmacodynamic) {
        this.pharmacodynamic = pharmacodynamic;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getProhibition() {
        return prohibition;
    }

    public void setProhibition(String prohibition) {
        this.prohibition = prohibition;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getDoseAdjustment() {
        return doseAdjustment;
    }

    public void setDoseAdjustment(String doseAdjustment) {
        this.doseAdjustment = doseAdjustment;
    }

    public String getComplication() {
        return complication;
    }

    public void setComplication(String complication) {
        this.complication = complication;
    }

    public String getInterference() {
        return interference;
    }

    public void setInterference(String interference) {
        this.interference = interference;
    }

    public String getEffectOnTest() {
        return effectOnTest;
    }

    public void setEffectOnTest(String effectOnTest) {
        this.effectOnTest = effectOnTest;
    }

    public String getOverdose() {
        return overdose;
    }

    public void setOverdose(String overdose) {
        this.overdose = overdose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelationWithFood() {
        return relationWithFood;
    }

    public void setRelationWithFood(String relationWithFood) {
        this.relationWithFood = relationWithFood;
    }

    public String getCompounds() {
        return compounds;
    }

    public void setCompounds(String compounds) {
        this.compounds = compounds;
    }

    public String getEffectiveIngredients() {
        return effectiveIngredients;
    }

    public void setEffectiveIngredients(String effectiveIngredients) {
        this.effectiveIngredients = effectiveIngredients;
    }

    public String getStandardized() {
        return standardized;
    }

    public void setStandardized(String standardized) {
        this.standardized = standardized;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVegetal() {
        return vegetal;
    }

    public void setVegetal(String vegetal) {
        this.vegetal = vegetal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setContent(JSONObject object) {
        try {
            this.name = object.getString("name");
            this.faName = object.getString("fa_name");
            this.brand = object.getString("brand");
            this.pregnancy = object.getString("pregnancy");
            this.lactation = object.getString("lactation");
            this.kids = object.getString("kids");
            this.seniors = object.getString("seniors");
            this.howToUse = object.getString("how_to_use");
            this.product = object.getString("product");
            this.pharmacodynamic = object.getString("pharmacodynamic");
            this.usage = object.getString("usage");
            this.prohibition = object.getString("prohibition");
            this.caution = object.getString("caution");
            this.doseAdjustment = object.getString("dose_adjustment");
            this.complication = object.getString("complication");
            this.interference = object.getString("interference");
            this.effectOnTest = object.getString("effect_on_test");
            this.overdose = object.getString("overdose");
            this.description = object.getString("description");
            this.relationWithFood = object.getString("relation_with_food");
            this.compounds = object.getString("compounds");
            this.effectiveIngredients = object.getString("effective_ingredients");
            this.standardized = object.getString("standardized");
            this.maintenance = object.getString("maintenance");
            this.company = object.getString("company");
            this.vegetal = object.getString("vegetal");
            this.status = object.getInt("status");
            this.lastModified = object.getString("last_modified");
//            this.categoryName = object.getString("category_name");
//            this.categoryType = object.getInt("category_type");
//            this.checked = object.getBoolean("checked");
//            this.favorite = object.getInt("favorite");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
