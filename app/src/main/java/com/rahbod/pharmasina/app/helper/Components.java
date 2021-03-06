package com.rahbod.pharmasina.app.helper;

import com.rahbod.pharmasina.app.controller.AppController;
import org.json.JSONArray;
import org.json.JSONException;

public class Components extends AppController {
    public static JSONArray jsonArrayRemove(JSONArray list, int position) {
        JSONArray newList = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.length(); i++)
                if (i != position) {
                    try {
                        newList.put(list.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        }
        return newList;
    }

    public static String decrypt(String encrypted, String key, String iv){
        MCrypt mCrypt = new MCrypt(key, iv);
        String data = "";
        try {
            byte[] cod =mCrypt.decrypt(encrypted);
            data = new String(cod);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}