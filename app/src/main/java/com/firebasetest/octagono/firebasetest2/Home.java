package com.firebasetest.octagono.firebasetest2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebasetest.octagono.firebasetest2.CustomAdapters.FriendsAdapter;
import com.firebasetest.octagono.firebasetest2.CustomAdapters.UsersAdapter;
import com.firebasetest.octagono.firebasetest2.Models.FriendsChats;
import com.firebasetest.octagono.firebasetest2.Models.MyChats;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = fAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");


    UsersAdapter listAdapter;
    ListView listView;
    List<String> listHeader;
    HashMap<String, List<String>> listChild;
    private ArrayList<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView) findViewById(R.id.lvUsers);

        prepareListData();


    }

   /* private void checkForUpdates(){
        Query lastUser = myRef.limitToLast(1);
        lastUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    User userModel = user.getValue(User.class);
                    if (!userModel.getEmail().equals(currentUser.getEmail())) {
                        listHeader.add(userModel.getEmail());
                        List<String> one = new ArrayList<String>();
                        one.add(userModel.getEmail());
                        listChild.put(listHeader.get(listHeader.size()-1), one);
                        listAdapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/



    public void prepareListData(){
        allUsers = new ArrayList<User>();
//        listHeader = new ArrayList<String>();
//        listChild = new HashMap<String, List<String>>();
//
        myRef = database.getReference("chats").child(currentUser.getEmail().split("@")[0]);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()) {
                    FriendsChats chats = user.getValue(FriendsChats.class);
                    int x = 0;
                }
//                HashMap<String, String> chats = (HashMap<String, String>) dataSnapshot.getValue();
//                for (Map.Entry<String, String> entry: chats.entrySet()) {
//                    User user = new User();
//                    int x = 0;
//                }
//                listAdapter = new UsersAdapter(Home.this, 0, allUsers);

//
//                listView.setAdapter(listAdapter);
//
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        User user = (User)parent.getItemAtPosition(position);
//                        String[] username = user.getEmail().split("@");
//                        Intent newIntent = new Intent(Home.this, Message.class);
//                        Bundle b = new Bundle();
//                        b.putString("messageUsername",username[0]);
//                        newIntent.putExtras(b);
//                        startActivity(newIntent);
//                    }
                //});


//

              /*  checkForUpdates();*/
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
//
//

    }

    public void LogoutAccount(View view){
        fAuth.signOut();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}
