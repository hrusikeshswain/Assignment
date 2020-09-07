package com.alhafeez.assignment.adapters;

/**
 * Created by Ashish Sharma on 5/16/2020.
 * Be U Salons
 * ashishsharma@beusalons.com
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alhafeez.assignment.DatabaseHandler;
import com.alhafeez.assignment.R;
import com.alhafeez.assignment.dialogs.MenuDialogFragment;
import com.alhafeez.assignment.dialogs.MenuRequestFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class RequestitemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context ctx;
    ArrayList<HashMap<String, String>> allitems;
    private MenuRequestFragment fragment;

//    UserCart saved_cart= null;

    public RequestitemAdapter(Context ctx, ArrayList<HashMap<String, String>>  allitems, MenuRequestFragment fragment) {
        this.ctx = ctx;
        this.allitems = allitems;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_request, parent, false);
        vh = new RequestitemAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder holder, final int position) {

        ((OriginalViewHolder)holder).txt_date.setText(""+allitems.get(position).get("orderdate"));
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<String> finalOutputString = gson.fromJson(allitems.get(position).get("orderitems"), type);
        String appen = "";
        for (int k=0;k<finalOutputString.size();k++){
            DatabaseHandler db = new DatabaseHandler(ctx);
            appen = appen+"\n"+db.getChapterDetails(finalOutputString.get(k));
        }
        ((OriginalViewHolder)holder).txt_items.setText(appen);

        if (allitems.get(position).get("orderstatus").equals("1")){
            ((OriginalViewHolder)holder).txt_status.setText("Pending");
            ((OriginalViewHolder)holder).txt_status.setBackgroundColor(ctx.getResources().getColor(R.color.colorAppt));
        }else if (allitems.get(position).get("orderstatus").equals("2")){
            ((OriginalViewHolder)holder).txt_status.setText("Accepted");
            ((OriginalViewHolder)holder).txt_status.setBackgroundColor(ctx.getResources().getColor(R.color.coloredit));
        }else if (allitems.get(position).get("orderstatus").equals("3")){
            ((OriginalViewHolder)holder).txt_status.setText("Rejected");
            ((OriginalViewHolder)holder).txt_status.setBackgroundColor(ctx.getResources().getColor(R.color.colorcancel));
        }
        ((OriginalViewHolder)holder).txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment.openDialogBox(ctx,position);
                    }
                },100);

            }
        });

        ((OriginalViewHolder)holder).txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.cancelshowToast("Cancelled successfully",position);
            }
        });

        /*((OriginalViewHolder)holder).txt_date.setText(allitems.get(position).get("orderdate"));
        ((OriginalViewHolder)holder).txt_date.setText(allitems.get(position).get("orderdate"));*/

        /*((OriginalViewHolder)holder).title.setText(allitems.get(position).get("name"));
        ((OriginalViewHolder)holder).txt_addcart.setText("+ SELECT");
        ((OriginalViewHolder)holder).txt_addcart.setTextColor(ctx.getResources().getColor(R.color.list_card));*/

        /*((OriginalViewHolder)holder).ll_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((OriginalViewHolder)holder).txt_addcart.getText().equals("+ SELECT")){
                    ((OriginalViewHolder)holder).txt_addcart.setText("Selected");
                    ((OriginalViewHolder)holder).txt_addcart.setTextColor(ctx.getResources().getColor(R.color.colorWhite));
                    ((OriginalViewHolder)holder).txt_addcart.setBackgroundColor(ctx.getResources().getColor(R.color.list_card));
                    fragment.list_ids.add(allitems.get(position).get("id"));
                    fragment.showToast(allitems.get(position).get("name")+" selected");
                }else {
                    ((OriginalViewHolder)holder).txt_addcart.setText("+ SELECT");
                    ((OriginalViewHolder)holder).txt_addcart.setTextColor(ctx.getResources().getColor(R.color.list_card));
                    ((OriginalViewHolder)holder).txt_addcart.setBackgroundColor(ctx.getResources().getColor(R.color.list_cardselect));
                    fragment.list_ids.remove(allitems.get(position).get("id"));
                    fragment.showToast(allitems.get(position).get("name")+" removed");
                }

            }
        });*/



    }






    @Override
    public int getItemCount() {
        return allitems.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date,txt_status,txt_items,txt_cancel,txt_edit;
        public View lyt_parent;
        public LinearLayout ll_edit;


        public OriginalViewHolder(View v) {
            super(v);
            lyt_parent = (View) v.findViewById(R.id.parent_layout);
            txt_date = v.findViewById(R.id.txt_date);
            txt_status =  v.findViewById(R.id.txt_status);
            txt_items = v.findViewById(R.id.txt_items);
            txt_edit =  v.findViewById(R.id.txt_edit);
            txt_cancel =  v.findViewById(R.id.txt_cancel);

        }
    }
}

