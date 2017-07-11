package com.firebasetest.octagono.firebasetest2.Fragments.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebasetest.octagono.firebasetest2.CustomAdapters.UsersAdapter;
import com.firebasetest.octagono.firebasetest2.Message;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.firebasetest.octagono.firebasetest2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by OCTAGONO on 7/1/2017.
 */

public class UsersFragment extends Fragment {
    public UsersFragment getInstance(){return new UsersFragment();}


    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = fAuth.getCurrentUser();
    DatabaseReference myRef = database.getReference("users");

    UsersAdapter listAdapter;
    ListView listView;
    private ArrayList<User> allUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View userListView = inflater.inflate(R.layout.users_fragment, container, false);
        listView = (ListView) userListView.findViewById(R.id.lvUsers);
        prepareListData();


        return userListView;
    }

    public void prepareListData(){
        allUsers = new ArrayList<User>();

        myRef = database.getReference("users");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userMessage:dataSnapshot.getChildren()) {
                    User user = userMessage.getValue(User.class);
                    user.setName(userMessage.getKey());
                    allUsers.add(user);
                }
                listAdapter = new UsersAdapter(getActivity(), 0, allUsers);

//

                listView.setAdapter(listAdapter);
//
//
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = (User)parent.getItemAtPosition(position);
                        Intent newIntent = new Intent(getActivity(), Message.class);
//                        Bundle b = new Bundle();
//                        b.putString("messageUsername",user.getName());
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
//
//

    }


}
