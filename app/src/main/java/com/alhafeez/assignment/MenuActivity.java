package com.alhafeez.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alhafeez.assignment.dialogs.MenuDialogFragment;
import com.alhafeez.assignment.dialogs.MenuRequestFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuActivity extends AppCompatActivity {

    private TextView txt_create,txt_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        txt_create = findViewById(R.id.textView);
        txt_view = findViewById(R.id.textView21);
        txt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialogFragment fragment= new MenuDialogFragment();
                fragment.show(getFragmentManager(), "MenutDialogFragment");
            }
        });
        txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuRequestFragment fragment= new MenuRequestFragment();
                fragment.show(getFragmentManager(), "MenuRequestFragment");
            }
        });
    }




}
