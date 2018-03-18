package com.example.behnam.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.behnam.app.R;
import com.example.behnam.app.database.Drug;
import com.example.behnam.app.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Behnam on 3/18/2018.
 */

public class AdapterInterferenceStep3 extends RecyclerView.Adapter<AdapterInterferenceStep3.listViewHolder> {

    Context context;
    List<Drug> list;

    public AdapterInterferenceStep3(Context context, List<Drug> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_search_interfernce_step3, parent, false);
        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {
        holder.txt.setText(list.get(position).getName());
        String conflicts = SessionManager.getExtrasPref(context).getString("conflicts");
        JSONObject conflictsObj = new JSONObject();
        try {
            conflictsObj = new JSONObject(conflicts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(conflictsObj.getBoolean(String.valueOf(list.get(position).getId()))) {
                holder.txtChar.setBackgroundResource(R.drawable.multiply_icon);
                holder.txtInterference.setText("تداخل دارد");
                holder.txtInterference.setTextColor(Color.RED);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class listViewHolder extends RecyclerView.ViewHolder{

        TextView txt, txtChar, txtInterference;
        LinearLayout lin;
        public listViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            txtInterference = itemView.findViewById(R.id.txtInterference);
            txtChar = itemView.findViewById(R.id.txtChar);
            lin = itemView.findViewById(R.id.linearLayoutRecInterference);
        }
    }
}
