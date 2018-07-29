package com.example.behnam.app.database;

/**
 * Created by moien on 08/02/2018.
 */

public class Drug {
    int id;
    String name;
    String namePersian;
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
    String overDose;
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

    public Drug() {

    }

    public Drug(int id, String name, String namePersian, String brand, String pregnancy, String lactation, String kids, String seniors, String howToUse, String product, String pharmacodynamic, String usage, String prohibition, String caution, String doseAdjustment, String complication, String interference, String effectOnTest, String overDose, String description, String relationWithFood, int status, String lastModified, String compounds, String effectiveIngredients, String standardized, String maintenance, String company, String vegetal) {
        this.id = id;
        this.name = name;
        this.namePersian = namePersian;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.howToUse = howToUse;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.doseAdjustment = doseAdjustment;
        this.complication = complication;
        this.interference = interference;
        this.effectOnTest = effectOnTest;
        this.overDose = overDose;
        this.description = description;
        this.relationWithFood = relationWithFood;
        this.status = status;
        this.lastModified = lastModified;
        this.compounds = compounds;
        this.effectiveIngredients = effectiveIngredients;
        this.maintenance = maintenance;
        this.company = company;
        this.standardized = standardized;
        this.vegetal = vegetal;
    }

    public Drug(String name, String namePersian, String brand, String pregnancy, String lactation, String kids, String seniors, String howToUse, String product, String pharmacodynamic, String usage, String prohibition, String caution, String doseAdjustment, String complication, String interference, String effectOnTest, String overDose, String description, String relationWithFood, int status, String lastModified) {
        this.name = name;
        this.namePersian = namePersian;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.howToUse = howToUse;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.doseAdjustment = doseAdjustment;
        this.complication = complication;
        this.interference = interference;
        this.effectOnTest = effectOnTest;
        this.overDose = overDose;
        this.description = description;
        this.relationWithFood = relationWithFood;
        this.status = status;
        this.lastModified = lastModified;
    }

    public Drug(int favorite) {
        this.favorite = favorite;
    }

    public Drug(String name) {
        this.name = name;
    }

    public Drug(int id, String name, String brand, String pregnancy, String lactation, String kids, String seniors, String howToUse, String product, String pharmacodynamic, String usage, String prohibition, String caution, String doseAdjustment, String complication, String interference, String effectOnTest, String overDose, String description, String relationWithFood, int status, String lastModified) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.howToUse = howToUse;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.doseAdjustment = doseAdjustment;
        this.complication = complication;
        this.interference = interference;
        this.effectOnTest = effectOnTest;
        this.overDose = overDose;
        this.description = description;
        this.relationWithFood = relationWithFood;
        this.status = status;
        this.lastModified = lastModified;
    }

    public Drug(String name, String brand, String pregnancy, String lactation, String kids, String seniors, String howToUse, String product, String pharmacodynamic, String usage, String prohibition, String caution, String doseAdjustment, String complication, String interference, String effectOnTest, String overDose, String description, String relationWithFood, int status, String lastModified) {
        this.name = name;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.howToUse = howToUse;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.doseAdjustment = doseAdjustment;
        this.complication = complication;
        this.interference = interference;
        this.effectOnTest = effectOnTest;
        this.overDose = overDose;
        this.description = description;
        this.relationWithFood = relationWithFood;
        this.status = status;
        this.lastModified = lastModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOverDose() {
        return overDose;
    }

    public void setOverDose(String overDose) {
        this.overDose = overDose;
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

    public void setChecked(boolean state) {
        this.checked = state;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getNamePersian() {
        return namePersian;
    }

    public void setNamePersian(String namePersian) {
        this.namePersian = namePersian;
    }
}

