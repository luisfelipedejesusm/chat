package com.firebasetest.octagono.firebasetest2.CustomAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebasetest.octagono.firebasetest2.Models.Message;
import com.firebasetest.octagono.firebasetest2.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by OCTAGONO on 6/27/2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, List<Message> messages, FirebaseUser user) {
        super(context, 0, messages);
        this.user = user;
    }

    FirebaseUser user;
    Calendar calendar = GregorianCalendar.getInstance();

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.chat, parent,false);
        }

        String[] username = user.getEmail().split("@");

        Message currentMessage = getItem(position);
        if (currentMessage.getMessageFrom().equals(username[0])){
            LinearLayout chatBubbleIncomingView = (LinearLayout) listItemView.findViewById(R.id.chat_incoming_view);
            LinearLayout chatBubbleOutgoingView = (LinearLayout) listItemView.findViewById(R.id.chat_outcoming_view);
            TextView chatBubbleOutgoing = (TextView) listItemView.findViewById(R.id.txtchatoutgoing);
            TextView chatBubbleOutgoingdate = (TextView) listItemView.findViewById(R.id.txtchatoutgoingdate);
            calendar.setTime(currentMessage.getMessageDate());

            chatBubbleOutgoing.setText(currentMessage.getMessageBody());
            chatBubbleOutgoingdate.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

            chatBubbleOutgoingView.setVisibility(View.VISIBLE);
            chatBubbleIncomingView.setVisibility(View.GONE);
        }else if (!currentMessage.getMessageFrom().equals(username[0])){
            TextView chatBubbleIncoming = (TextView) listItemView.findViewById(R.id.txtchatincoming);
            TextView chatBubbleIncomingdate = (TextView) listItemView.findViewById(R.id.txtchatincomingdate);
            LinearLayout chatBubbleIncomingView = (LinearLayout) listItemView.findViewById(R.id.chat_incoming_view);
            LinearLayout chatBubbleOutgoingView = (LinearLayout) listItemView.findViewById(R.id.chat_outcoming_view);
            calendar.setTime(currentMessage.getMessageDate());

            chatBubbleIncomingdate.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
            chatBubbleIncoming.setText(currentMessage.getMessageBody());

            chatBubbleOutgoingView.setVisibility(View.GONE);
            chatBubbleIncomingView.setVisibility(View.VISIBLE);
        }


        return listItemView;
    }
}
