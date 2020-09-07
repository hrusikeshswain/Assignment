package com.alhafeez.assignment.adapters;

/**
 * Created by Ashish Sharma on 5/16/2020.
 * Be U Salons
 * ashishsharma@beusalons.com
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.alhafeez.assignment.ApptDialogFragment;
import com.alhafeez.assignment.R;
import com.alhafeez.assignment.dialogs.MenuDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class ItemlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context ctx;
    ArrayList<HashMap<String, String>> allitems;
    private MenuDialogFragment fragment;
//    UserCart saved_cart= null;

    public ItemlistAdapter(Context ctx, ArrayList<HashMap<String, String>>  allitems, MenuDialogFragment fragment) {
        this.ctx = ctx;
        this.allitems = allitems;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_category, parent, false);
        vh = new ItemlistAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder holder, final int position) {

        ((OriginalViewHolder)holder).title.setText(allitems.get(position).get("name"));
        ((OriginalViewHolder)holder).txt_addcart.setText("+ SELECT");
        ((OriginalViewHolder)holder).txt_addcart.setTextColor(ctx.getResources().getColor(R.color.list_card));
        ((OriginalViewHolder)holder).txt_addcart.setBackgroundColor(ctx.getResources().getColor(R.color.list_cardselect));
//        ((OriginalViewHolder)holder).ll_addtocart.setEnabled(true);
        ((OriginalViewHolder)holder).ll_addtocart.setOnClickListener(new View.OnClickListener() {
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
        });



    }


    @Override
    public int getItemCount() {
        return allitems.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title,txt_addcart;
        public View lyt_parent;
        public LinearLayout ll_addtocart;


        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.img_prd_combo);
            title = (TextView) v.findViewById(R.id.textView34);
            txt_addcart = (TextView) v.findViewById(R.id.txt_addcart);
            lyt_parent = (View) v.findViewById(R.id.parent_layout);
            ll_addtocart = (LinearLayout) v.findViewById(R.id.ll_addtocart);

        }
    }
}

