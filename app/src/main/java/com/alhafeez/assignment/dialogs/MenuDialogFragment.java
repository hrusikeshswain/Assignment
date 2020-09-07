package com.alhafeez.assignment.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhafeez.assignment.DatabaseHandler;
import com.alhafeez.assignment.MenuActivity;
import com.alhafeez.assignment.R;
import com.alhafeez.assignment.RandomString;
import com.alhafeez.assignment.adapters.DateTimeAdapter;
import com.alhafeez.assignment.adapters.ItemlistAdapter;
import com.alhafeez.assignment.utility.SpacingItemDecoration;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Hrusikesh swain on 8/27/2020.
 * Be U Salons
 * hrusikeshswain@beusalons.com
 */
public class MenuDialogFragment extends DialogFragment {


    private ImageView close_activity;
    private LinearLayout linear_bottom,ll_paren;
    private TextView txt_items;
    private RecyclerView recycler_menu;
    public ArrayList<String> list_ids = new ArrayList<>();
    ItemlistAdapter itemlistAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linear_= (LinearLayout) inflater.inflate(R.layout.layout_menu, container, false);
        initViews(linear_);
        return linear_;
    }



    public void showToast(String msg){
//        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
        if (list_ids.size()>0){
            linear_bottom.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 120);
            ll_paren.setLayoutParams(layoutParams1);
        }else {
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            ll_paren.setLayoutParams(layoutParams1);
            linear_bottom.setVisibility(View.GONE);
        }
        txt_items.setText(list_ids.size()+" items selected | Send Now");
    }

    public void initViews(View linear_){
        ll_paren = linear_.findViewById(R.id.ll_parent);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        ll_paren.setLayoutParams(layoutParams);
        close_activity = linear_.findViewById(R.id.close_activity);
        linear_bottom = linear_.findViewById(R.id.linear_bottom);
        linear_bottom.setVisibility(View.GONE);
        txt_items = linear_.findViewById(R.id.txt_items);
        recycler_menu = linear_.findViewById(R.id.recycler_menu);
        GridLayoutManager layoutManager= new GridLayoutManager(getActivity(), 2);
        recycler_menu.setHasFixedSize(true);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<HashMap<String, String>> allItems = db.getAllItems();
        recycler_menu.setItemViewCacheSize(allItems.size());
        recycler_menu.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 0), true));
        recycler_menu.setLayoutManager(layoutManager);
        itemlistAdapter = new ItemlistAdapter(getActivity().getApplicationContext(),allItems,this);
        recycler_menu.setAdapter(itemlistAdapter);
        if (list_ids.size()>0){
            linear_bottom.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 120);
            ll_paren.setLayoutParams(layoutParams1);
        }else {
            ll_paren.setLayoutParams(layoutParams);
            linear_bottom.setVisibility(View.GONE);
        }
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txt_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list_ids.size()>0){
                    createOrder(list_ids);
                }else {
                    Log.e("Menudialog","no items");
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        Log.e("start","onStart");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public void createOrder(ArrayList<String> listitemids){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Gson gson = new Gson();
        String idsString= gson.toJson(listitemids);
        Calendar cd  = Calendar.getInstance();
        Date c = cd.getTime();
        SimpleDateFormat df_selectted = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate_selected = df_selectted.format(c);
        db.addOrders(RandomString.generateString(),formattedDate_selected,idsString,"1");
        DialogThanks fragment= new DialogThanks();
        fragment.setCancelable(false);

       /* Bundle bundle= new Bundle();
        bundle.putString("id" ,reorderData.get(position).get_id());
        fragment.setArguments(bundle);*/
        fragment.show(((MenuActivity)getActivity()).getSupportFragmentManager(), "thanks");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String  event) {
        Log.e("url in activity",event);
        if (event.equals("true")){
            list_ids.clear();
            linear_bottom.setVisibility(View.GONE);
            itemlistAdapter.notifyDataSetChanged();
            showToast("dismissed");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
