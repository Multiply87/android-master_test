package com.example.privatchat.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.privatchat.R;
import com.example.privatchat.mainclass.Dialog;
import com.example.privatchat.viewmodel.MyViewModel;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public class DialogsActivity extends AppCompatActivity implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>{


    public static void open(Context context) {
        context.startActivity(new Intent(context, DialogsActivity.class));
    }

    DialogsList dialogsList;
    private DialogsListAdapter<Dialog> dialogsAdapter;
    MyViewModel myViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogs);
        dialogsList = findViewById(R.id.DL);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);//переходим во вьюмодел
        getLifecycle().addObserver(myViewModel);
        dialogsAdapter  = new DialogsListAdapter<>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {

            }
        });
        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);
        dialogsList.setAdapter(dialogsAdapter);
        myViewModel.getDialog().observe(this,DialogsList -> {
            dialogsAdapter.setItems(DialogsList);
        });
    }


    @Override
    public void onDialogClick(Dialog dialog) {
        //переход в диалог
        MessagesActivity.open(this);
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {

    }
}
