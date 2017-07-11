package com.firebasetest.octagono.firebasetest2.CustomAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.firebasetest.octagono.firebasetest2.R;

import java.util.ArrayList;

/**
 * Created by OCTAGONO on 7/4/2017.
 */

public class ChatsAdapter  extends ArrayAdapter<User> {
    public ChatsAdapter(Context context, int resources, ArrayList<User> users){
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chats_listview, parent,false);
        }
        User currentUser = getItem(position);
        TextView user_nombre = (TextView) listItemView.findViewById(R.id.home_txtNombre);
        user_nombre.setText(currentUser.getName());

        TextView cant = (TextView) listItemView.findViewById(R.id.numberNotification);

        if (currentUser.getCantMsgNoVistos() == 0){
            cant.setVisibility(View.GONE);
        }else if(currentUser.getCantMsgNoVistos() != 0){
            cant.setVisibility(View.VISIBLE);
            cant.setText(String.valueOf(currentUser.getCantMsgNoVistos()));
        }
        ImageView user_image = (ImageView) listItemView.findViewById(R.id.home_chat_imageview);

        if (currentUser.getFoto64() != null) {
            Glide.with(getContext())
                    .load(currentUser.getFoto64())
                    .into(user_image);
        }

        return listItemView;
    }
}
