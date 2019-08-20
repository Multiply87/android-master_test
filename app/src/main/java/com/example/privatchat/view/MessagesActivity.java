package com.example.privatchat.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.privatchat.R;
import com.example.privatchat.mainclass.Message;
import com.example.privatchat.mainclass.User;
import com.example.privatchat.viewmodel.MyViewModel;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;

public class MessagesActivity extends AppCompatActivity implements MessageInput.InputListener, MessageInput.AttachmentsListener, MessageInput.TypingListener, MessagesListAdapter.SelectionListener, MessagesListAdapter.OnLoadMoreListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, MessagesActivity.class));
    }

    private MessagesList messagesList;
    protected MessagesListAdapter<Message> messagesAdapter;
    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    MyViewModel myViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        this.messagesList =  findViewById(R.id.messagesList);
        MessageInput input = findViewById(R.id.input);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);//переходим во вьюмодел
        messagesAdapter= new MessagesListAdapter<>(senderId,imageLoader);

        myViewModel.getMessageDialog().observe(this,arraymessage->{
            messagesAdapter.addToEnd(arraymessage,true);
        });

        this.messagesList.setAdapter(messagesAdapter);
        input.setInputListener(this);
        input.setTypingListener(this);
        input.setAttachmentsListener(this);
    }

    @Override
    public void onAddAttachments() {
        //Добавление вложеных файлов
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        //Получение нового сообщения
        User thisUser  = myViewModel.getUser();
        Message message = new Message(senderId,input.toString(), thisUser,new Date());
        messagesAdapter.addToStart(message,true);
        return true;
    }

    @Override
    public void onStartTyping() {

    }

    @Override
    public void onStopTyping() {

    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {

    }

    @Override
    public void onSelectionChanged(int count) {

    }
}
