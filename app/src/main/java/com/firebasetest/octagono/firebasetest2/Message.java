package com.firebasetest.octagono.firebasetest2;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebasetest.octagono.firebasetest2.CustomAdapters.MessageAdapter;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message extends AppCompatActivity {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = fAuth.getCurrentUser();
    private DatabaseReference myRef;
    private ArrayList<com.firebasetest.octagono.firebasetest2.Models.Message> allMessages;
    private MessageAdapter messageAdapter;
    private ListView messagesListView;
    private String[] username;
    private EditText editText;
    private TextView isWritingTextView;
    private User userChat;
    private User currentUser;
    private boolean isOpen;
    private DatabaseReference contactRef;
    private ValueEventListener checkForUpdatesValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot message: dataSnapshot.getChildren()) {
                com.firebasetest.octagono.firebasetest2.Models.Message Message = message.getValue(com.firebasetest.octagono.firebasetest2.Models.Message.class);
                if (Message.getMessageBody() != null){
                    if(allMessages.size() != 0){
                        if (allMessages.get(allMessages.size()-1).getMessageDate().compareTo(Message.getMessageDate()) != 0) {
                            if (!Message.getMessageFrom().equals(user.getEmail().split("@")[0]) && isOpen){
                                Message.setVisto(true);
                                myRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                                contactRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                            }
                            allMessages.add(Message);
                            messageAdapter.notifyDataSetChanged();
                            messagesListView.smoothScrollToPosition(messageAdapter.getCount()-1);
                        }else{
                            if(Message.isVisto()){
                                for (com.firebasetest.octagono.firebasetest2.Models.Message msg : allMessages) {
                                    msg.setVisto(true);
                                }
                                messageAdapter.notifyDataSetChanged();
                            }
                        }
                    }else{
                        if (!Message.getMessageFrom().equals(user.getEmail().split("@")[0]) && isOpen){
                            Message.setVisto(true);
                            myRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                            contactRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                        }
                        allMessages.add(Message);
                        messageAdapter.notifyDataSetChanged();
                        messagesListView.smoothScrollToPosition(messageAdapter.getCount()-1);

                    }
                }
            }

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private ValueEventListener isWritingValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Boolean isWriting = dataSnapshot.getValue(Boolean.class);
            if (isWriting != null){
                if (isWriting){
                    isWritingTextView.setVisibility(View.VISIBLE);
                }else{
                    isWritingTextView.setVisibility(View.GONE);
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count != 0){
                contactRef.child("states").child("isWriting").setValue(true);
            }else{
                contactRef.child("states").child("isWriting").setValue(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setCurrentUser();
        userChat = (User) getIntent().getSerializableExtra("userChat");
        TextView nombre = (TextView) findViewById(R.id.textView4);
        ImageView imageView = (ImageView) findViewById(R.id.chat_image_profile);
        editText = (EditText) findViewById(R.id.editText);
        Glide.with(this).load(userChat.getFoto64()).into(imageView);
        isWritingTextView = (TextView) findViewById(R.id.isWritingTextView);
        nombre.setText(userChat.getName());
        username = user.getEmail().split("@");
        messagesListView = (ListView) findViewById(R.id.listView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myRef = database.getReference()
                .child("chats")
                .child(user.getEmail().split("@")[0])
                .child(userChat.getName());
        contactRef = database.getReference()
                .child("chats")
                .child(userChat.getName())
                .child(user.getEmail().split("@")[0]);
        getMessages();
    }
    private void setCurrentUser() {
        DatabaseReference currentUserRef = database.getReference("users");
        Query currentUserRefQuery = currentUserRef.orderByChild("email").equalTo(user.getEmail()).limitToLast(1);
        currentUserRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    currentUser = user.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void checkForUpdates(){

        Query lastValue = myRef.child("chat").limitToLast(1);
        Query isWritingListener = myRef.child("states").child("isWriting");

        lastValue.addValueEventListener(checkForUpdatesValueEventListener);
        editText.addTextChangedListener(textChangeListener);
        isWritingListener.addValueEventListener(isWritingValueListener);

    }
    private void getMessages(){
        allMessages = new ArrayList<>();
        myRef.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot message: dataSnapshot.getChildren()) {
                    com.firebasetest.octagono.firebasetest2.Models.Message Message = message.getValue(com.firebasetest.octagono.firebasetest2.Models.Message.class);
                    if (!Message.getMessageFrom().equals(user.getEmail().split("@")[0])){
                        Message.setVisto(true);
                        myRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                        contactRef.child("chat").child(String.valueOf(Message.getMessageDate().getTime() / 1000L)).setValue(Message);
                    }
                    allMessages.add(Message);
                }
                messageAdapter = new MessageAdapter(Message.this,0,allMessages,user);
                messagesListView.setAdapter(messageAdapter);
                messagesListView.setDivider(null);
                messagesListView.setSelection(messageAdapter.getCount()-1);
                checkForUpdates();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void sendMessage(View view){
        EditText txtMessage = (EditText) findViewById(R.id.editText);
        if (!txtMessage.getText().toString().equals("")) {
            Date date = new Date();
            com.firebasetest.octagono.firebasetest2.Models.Message message = new com.firebasetest.octagono.firebasetest2.Models.Message();
            message.setMessageFrom(user.getEmail().split("@")[0]);
            message.setMessageTo(userChat.getName());
            message.setMessageDate(date);
            message.setMessageBody(txtMessage.getText().toString());
            message.setVisto(false);
            myRef.child("user").setValue(userChat);
            myRef.child("chat").child(String.valueOf(System.currentTimeMillis() / 1000L)).setValue(message);
            myRef.child("lastModified").setValue(System.currentTimeMillis() / 1000L);
            message.setVisto(false);
            contactRef.child("user").setValue(currentUser);
            contactRef.child("chat").child(String.valueOf(System.currentTimeMillis() / 1000L)).setValue(message);
            contactRef.child("lastModified").setValue(System.currentTimeMillis() / 1000L);
            txtMessage.setText(null);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        isOpen = true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        isOpen = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        isOpen = true;
    }
}
