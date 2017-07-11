package com.firebasetest.octagono.firebasetest2.Fragments.HomeFragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.firebasetest.octagono.firebasetest2.CustomAdapters.ChatsAdapter;
import com.firebasetest.octagono.firebasetest2.CustomAdapters.UsersAdapter;
import com.firebasetest.octagono.firebasetest2.Home;
import com.firebasetest.octagono.firebasetest2.Message;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.firebasetest.octagono.firebasetest2.R;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by OCTAGONO on 7/1/2017.
 */

public class ChatsFragment extends Fragment {
    private NotificationManager mNotificationManager;
    private boolean isApplicationOpen;

    public ChatsFragment newInstance(){return new ChatsFragment();}
    private int lastBadgeCant;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = fAuth.getCurrentUser();
    DatabaseReference myRef = database.getReference("users");

    ChatsAdapter listAdapterChat;
    ListView listViewChat;
    private ArrayList<User> allUsers;
    ArrayList<com.firebasetest.octagono.firebasetest2.Models.Message> noVistosMsg;
    Context context;
    Intent openMyApp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isApplicationOpen = true;
        Log.d("status","application open");
        context = getContext();
        openMyApp = new Intent(context,Home.class);
        lastBadgeCant = 0;
        View chatsListView = inflater.inflate(R.layout.chat_fragment, container, false);
        listViewChat = (ListView) chatsListView.findViewById(R.id.lvChats);
        prepareListData();
        checkForUpdates();


        return chatsListView;
    }

    public void prepareListData(){
        allUsers = new ArrayList<User>();

        myRef = database.getReference("chats").child(currentUser.getEmail().split("@")[0]);

        myRef.orderByChild("lastModified").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userMessage:dataSnapshot.getChildren()) {
                    User user = userMessage.child("user").getValue(User.class);
                    if (user.getName() == null)
                        user.setName(userMessage.getKey());


                    allUsers.add(user);
                }
                Collections.reverse(allUsers);

                listAdapterChat = new ChatsAdapter(getActivity(), 0, allUsers);

                listViewChat.setAdapter(listAdapterChat);

                listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = (User)parent.getItemAtPosition(position);
                        Intent newIntent = new Intent(getActivity(), Message.class);
                        user.setMsgNoVistos(null);
//                        Bundle b = new Bundle();
//                        b.putString("messageUsername",user.getName());
//                        newIntent.putExtras(b);
                        newIntent.putExtra("userChat",user);
                        startActivity(newIntent);
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });





    }

    public void checkForUpdates(){
        myRef.orderByChild("lastModified").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers = new ArrayList<User>();
                int cantNoVistos = 0;
                for (final DataSnapshot data:dataSnapshot.getChildren()) {
                    noVistosMsg = new ArrayList<com.firebasetest.octagono.firebasetest2.Models.Message>();
                    User user = data.child("user").getValue(User.class);
                    if (user.getName() == null)
                        user.setName(data.getKey());
//                    user.setName(data.getKey());
//                    Query cantMensajesNoVistos =
//                            myRef.child(data.getKey())
//                                    .child("chat")
//                                    .orderByChild("visto")
//                                    .equalTo(false);
//                    cantMensajesNoVistos.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            noVistosMsg = new ArrayList<com.firebasetest.octagono.firebasetest2.Models.Message>();
//                            Log.d("dataTest",data.getKey()+" "+String.valueOf(dataSnapshot.getChildrenCount()));
//                            for (DataSnapshot msg:dataSnapshot.getChildren()) {
//                                com.firebasetest.octagono.firebasetest2.Models.Message message = msg.getValue(com.firebasetest.octagono.firebasetest2.Models.Message.class);
//                                noVistosMsg.add(message);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                    for (DataSnapshot data2: data.child("chat").getChildren()) {
                        com.firebasetest.octagono.firebasetest2.Models.Message message = data2.getValue(com.firebasetest.octagono.firebasetest2.Models.Message.class);
                        noVistosMsg.add(message);
                    }
                    user.setMsgNoVistos(noVistosMsg);
                    cantNoVistos += user.getCantMsgNoVistos();
                    allUsers.add(user);
                }
                Collections.reverse(allUsers);
                listAdapterChat = new ChatsAdapter(context, 0, allUsers);


                listViewChat.setAdapter(listAdapterChat);
                addNotification(cantNoVistos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(int cantN){
        if (lastBadgeCant != cantN) {
            try {
                Badges.setBadge(getContext(), cantN);
                if (cantN != 0 && !isApplicationOpen) {
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    openMyApp,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_remove_red_eye_white_24dp)
                                    .setContentTitle("Chat Test II")

                                    .setContentText("Usted tiene " + cantN + " no vistos.")
                                    .setContentIntent(resultPendingIntent);
                    if (lastBadgeCant < cantN){
                        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                        .setLights(ContextCompat.getColor(context, R.color.colorPrimary),3000, 3000);
                    }

                    mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.notify(001, mBuilder.build());
                } else {
                    mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(001);
                }
            } catch (BadgesNotSupportedException badgesNotSupportedException) {
                Log.d("Notification", badgesNotSupportedException.getMessage());
            }
        }
        lastBadgeCant = cantN;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isApplicationOpen = false;
        Log.d("status","application closed");
    }

    @Override
    public void onPause() {
        super.onPause();
        isApplicationOpen = false;
        Log.d("status","application closed");
    }

    @Override
    public void onResume() {
        super.onResume();
        isApplicationOpen = true;
        Log.d("status","application open");
    }
}
