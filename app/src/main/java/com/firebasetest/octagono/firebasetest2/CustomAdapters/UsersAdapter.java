package com.firebasetest.octagono.firebasetest2.CustomAdapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebasetest.octagono.firebasetest2.Models.User;
import com.firebasetest.octagono.firebasetest2.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by OCTAGONO on 6/30/2017.
 */

public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(Context context, int resources, ArrayList<User> users){
        super(context, 0, users);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.users_listview, parent,false);
        }
        User currentUser = getItem(position);
        TextView user_nombre = (TextView) listItemView.findViewById(R.id.home_txtNombre);
        user_nombre.setText(currentUser.getEmail().split("@")[0]);

        return listItemView;
    }
}
