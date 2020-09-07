package com.alhafeez.assignment.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alhafeez.assignment.DatabaseHandler;
import com.alhafeez.assignment.MenuActivity;
import com.alhafeez.assignment.R;
import com.alhafeez.assignment.RandomString;
import com.alhafeez.assignment.adapters.ItemlistAdapter;
import com.alhafeez.assignment.adapters.RequestitemAdapter;
import com.alhafeez.assignment.utility.SpacingItemDecoration;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Hrusikesh swain on 8/27/2020.
 * Be U Salons
 * hrusikeshswain@beusalons.com
 */
public class MenuRequestFragment extends DialogFragment {


    private ImageView close_activity;

    private RecyclerView recycler_requests;
    public ArrayList<String> list_ids = new ArrayList<>();
    RequestitemAdapter requestitemAdapter;
    private Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linear_= (LinearLayout) inflater.inflate(R.layout.layout_requests, container, false);
        initViews(linear_);
        return linear_;

    }



    public void showToast(String msg){
        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<HashMap<String, String>> allRequests = db.getAllRequests();
        requestitemAdapter = new RequestitemAdapter(getActivity().getApplicationContext(),allRequests,this);
        recycler_requests.setAdapter(requestitemAdapter);
    }

    public void cancelshowToast(String msg,int position){
        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
        DatabaseHandler db = new DatabaseHandler(getActivity());
        db.deleteOrder(db.getAllRequests().get(position).get("orderid"));
        ArrayList<HashMap<String, String>> allRequests = db.getAllRequests();
        requestitemAdapter = new RequestitemAdapter(getActivity().getApplicationContext(),allRequests,this);
        recycler_requests.setAdapter(requestitemAdapter);
    }

    public void initViews(View linear_){

        close_activity = linear_.findViewById(R.id.close_activity);
        recycler_requests = linear_.findViewById(R.id.recycler_requests);
        GridLayoutManager layoutManager= new GridLayoutManager(getActivity(), 1);
        recycler_requests.setHasFixedSize(true);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<HashMap<String, String>> allRequests = db.getAllRequests();
        recycler_requests.setItemViewCacheSize(allRequests.size());
        recycler_requests.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 0), true));
        recycler_requests.setLayoutManager(layoutManager);
        requestitemAdapter = new RequestitemAdapter(getActivity().getApplicationContext(),allRequests,this);
        recycler_requests.setAdapter(requestitemAdapter);
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }



    public void createOrder(ArrayList<String> listitemids){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Gson gson = new Gson();
        String idsString= gson.toJson(listitemids);
        db.addItems(RandomString.generateString(),idsString);
        DialogThanks fragment= new DialogThanks();
        fragment.setCancelable(false);

       /* Bundle bundle= new Bundle();
        bundle.putString("id" ,reorderData.get(position).get_id());
        fragment.setArguments(bundle);*/
        fragment.show(((MenuActivity)getActivity()).getSupportFragmentManager(), "thanks");
    }


    public void openDialogBox(final Context cts, final int position) {
        ((MenuActivity)getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_changelang);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RadioGroup rbgroup;
                RadioButton rb_pending,rb_accepted,rb_rejected;
                rb_pending = dialog.findViewById(R.id.rb_pending);
                rb_accepted = dialog.findViewById(R.id.rb_accepted);
                rb_rejected = dialog.findViewById(R.id.rb_rejected);
                rbgroup = dialog.findViewById(R.id.rbgroup);
                DatabaseHandler db = new DatabaseHandler(getActivity());
                final ArrayList<HashMap<String, String>> allitems = db.getAllRequests();

                if (allitems.get(position).get("orderstatus").equals("1")){
                    rb_pending.setChecked(true);
                    rb_accepted.setChecked(false);
                    rb_rejected.setChecked(false);
                }else if (allitems.get(position).get("orderstatus").equals("2")){
                    rb_pending.setChecked(false);
                    rb_accepted.setChecked(true);
                    rb_rejected.setChecked(false);

                }else if (allitems.get(position).get("orderstatus").equals("3")){
                    rb_pending.setChecked(false);
                    rb_accepted.setChecked(false);
                    rb_rejected.setChecked(true);
                }

                rbgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        RadioButton rb=(RadioButton)dialog.findViewById(checkedId);
//                textViewChoice.setText("You Selected " + rb.getText());

                        DatabaseHandler db = new DatabaseHandler(cts);
                        if (rb.getText().equals("Pending")){
                            db.updateStatus(db.getAllRequests().get(position).get("orderid"),"1");
                        }else if (rb.getText().equals("Accepted")){
                            db.updateStatus(db.getAllRequests().get(position).get("orderid"),"2");
                        }else if (rb.getText().equals("Rejected")){
                            db.updateStatus(db.getAllRequests().get(position).get("orderid"),"3");
                        }
                        dialog.dismiss();
                        showToast("You Selected " + rb.getText());

                    }
                });

                dialog.show();
            }
        });

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
