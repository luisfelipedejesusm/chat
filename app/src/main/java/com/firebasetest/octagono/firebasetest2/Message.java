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
import android.widget.ListView;
import android.widget.TextView;

import com.firebasetest.octagono.firebasetest2.CustomAdapters.MessageAdapter;
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

    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = fAuth.getCurrentUser();
    DatabaseReference myRef;
    DatabaseReference checkRef;
    ArrayList<com.firebasetest.octagono.firebasetest2.Models.Message> allMessages;
    public MessageAdapter messageAdapter;
    ListView messagesListView;
    Bundle b;
    String[] username;
    EditText editText;
    TextView isWritingTextView;

    private boolean isOpen;

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private DatabaseReference contactRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        //ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(R.layout.actionbar_custom_layout);

        b = getIntent().getExtras();
        TextView nombre = (TextView) findViewById(R.id.textView4);
        editText = (EditText) findViewById(R.id.editText);
        isWritingTextView = (TextView) findViewById(R.id.isWritingTextView);
        nombre.setText(b.getString("messageUsername","Not Found"));
        username = user.getEmail().split("@");
        final MessageAdapter messageAdapter;
        messagesListView = (ListView) findViewById(R.id.listView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        myRef = database.getReference()
                .child("chats")
                .child(user.getEmail().split("@")[0])
                .child(b.getString("messageUsername"));
        contactRef = database.getReference()
                .child("chats")
                .child(b.getString("messageUsername"))
                .child(user.getEmail().split("@")[0]);
//        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(username[0]+"-_=_-"+b.getString("messageUsername"))){
//                    myRef = database.getReference("chats/"+username[0]+"-_=_-"+b.getString("messageUsername"));
//                    getMessages();
//                }else{
//                    myRef = database.getReference("chats/"+b.getString("messageUsername")+"-_=_-"+username[0]);
//                    getMessages();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        getMessages();



    }
    private void checkForUpdates(){
        Query lastValue = myRef.child("chat").limitToLast(1);
        lastValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot message: dataSnapshot.getChildren()) {
                    com.firebasetest.octagono.firebasetest2.Models.Message Message = message.getValue(com.firebasetest.octagono.firebasetest2.Models.Message.class);
                    if (Message.getMessageBody() != null){
                        if(allMessages.size() != 0){
                            if (allMessages.get(allMessages.size()-1).getMessageDate().compareTo(Message.getMessageDate()) != 0) {
                                if (!Message.getMessageFrom().equals(user.getEmail().split("@")[0]) && isOpen){
                                    Message.setVisto(true);
                                    contactRef.child("chat").child(dateFormat.format(Message.getMessageDate())).setValue(Message);
                                }
                                allMessages.add(Message);
                                messageAdapter.notifyDataSetChanged();
                                messagesListView.smoothScrollToPosition(messageAdapter.getCount()-1);
                            }
                        }else{
                            if (!Message.getMessageFrom().equals(user.getEmail().split("@")[0]) && isOpen){
                                Message.setVisto(true);
                                contactRef.child("chat").child(dateFormat.format(Message.getMessageDate())).setValue(Message);
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
        });

        editText.addTextChangedListener(new TextWatcher() {
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
        });

//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if(!editText.getText().equals("")){
//                        myRef.child("isWriting").setValue(true);
//                    }
//                }else{
//                    myRef.child("isWriting").setValue(true);
//                }
//            }
//        });

        Query isWritingListener = myRef.child("states").child("isWriting");
        isWritingListener.addValueEventListener(new ValueEventListener() {
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

//                for (DataSnapshot isWritingValue:dataSnapshot.getChildren()) {
//                    Boolean isWriting = isWritingValue.getValue(Boolean.class);
//
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                        myRef.child("chat").child(dateFormat.format(Message.getMessageDate())).setValue(Message);
                        contactRef.child("chat").child(dateFormat.format(Message.getMessageDate())).setValue(Message);
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
            message.setMessageTo(b.getString("messageUsername"));
            message.setMessageDate(date);
            message.setMessageBody(txtMessage.getText().toString());
            message.setVisto(true);
            myRef.child("chat").child(dateFormat.format(date)).setValue(message);
            message.setVisto(false);
            contactRef.child("chat").child(dateFormat.format(date)).setValue(message);
            txtMessage.setText(null);
            //allMessages.add(message);
           // messageAdapter.notifyDataSetChanged();
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
