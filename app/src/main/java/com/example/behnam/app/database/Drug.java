package com.example.behnam.app.database;

/**
 * Created by moien on 08/02/2018.
 */

public class Drug {
    int id;
    String name;
    String brand;
    String pregnancy;
    String lactation;
    String kids;
    String seniors;
    String how_to_use;
    String product;
    String pharmacodynamic;
    String usage;
    String prohibition;
    String caution;
    String dose_adjustment;
    String complication;
    String interference;
    String effect_on_test;
    String over_dose;
    String description;
    String relation_with_food;
    int status;
    String last_modified;
    boolean checked = false;
    int favorite;

    public Drug() {

    }

    public Drug(int favorite) {
        this.favorite = favorite;
    }

    public Drug(String name) {
        this.name = name;
    }

    public Drug(int id, String name, String brand, String pregnancy, String lactation, String kids, String seniors, String how_to_use, String product, String pharmacodynamic, String usage, String prohibition, String caution, String dose_adjustment, String complication, String interference, String effect_on_test, String over_dose, String description, String relation_with_food, int status, String last_modified) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.how_to_use = how_to_use;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.dose_adjustment = dose_adjustment;
        this.complication = complication;
        this.interference = interference;
        this.effect_on_test = effect_on_test;
        this.over_dose = over_dose;
        this.description = description;
        this.relation_with_food = relation_with_food;
        this.status = status;
        this.last_modified = last_modified;
    }

    public Drug(String name, String brand, String pregnancy, String lactation, String kids, String seniors, String how_to_use, String product, String pharmacodynamic, String usage, String prohibition, String caution, String dose_adjustment, String complication, String interference, String effect_on_test, String over_dose, String description, String relation_with_food, int status, String last_modified) {
        this.name = name;
        this.brand = brand;
        this.pregnancy = pregnancy;
        this.lactation = lactation;
        this.kids = kids;
        this.seniors = seniors;
        this.how_to_use = how_to_use;
        this.product = product;
        this.pharmacodynamic = pharmacodynamic;
        this.usage = usage;
        this.prohibition = prohibition;
        this.caution = caution;
        this.dose_adjustment = dose_adjustment;
        this.complication = complication;
        this.interference = interference;
        this.effect_on_test = effect_on_test;
        this.over_dose = over_dose;
        this.description = description;
        this.relation_with_food = relation_with_food;
        this.status = status;
        this.last_modified = last_modified;
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

    public String getHow_to_use() {
        return how_to_use;
    }

    public void setHow_to_use(String how_to_use) {
        this.how_to_use = how_to_use;
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

    public String getDose_adjustment() {
        return dose_adjustment;
    }

    public void setDose_adjustment(String dose_adjustment) {
        this.dose_adjustment = dose_adjustment;
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

    public String getEffect_on_test() {
        return effect_on_test;
    }

    public void setEffect_on_test(String effect_on_test) {
        this.effect_on_test = effect_on_test;
    }

    public String getOver_dose() {
        return over_dose;
    }

    public void setOver_dose(String over_dose) {
        this.over_dose = over_dose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelation_with_food() {
        return relation_with_food;
    }

    public void setRelation_with_food(String relation_with_food) {
        this.relation_with_food = relation_with_food;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
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
}
