package com.firebasetest.octagono.firebasetest2.Models;

import java.util.ArrayList;

/**
 * Created by OCTAGONO on 6/30/2017.
 */

public class FriendsChats {

    private ArrayList<Message> chats;
    private ArrayList<States> states;

    public FriendsChats() {
    }

    public ArrayList<Message> getChats() {

        return chats;
    }

    public void setChats(ArrayList<Message> chats) {
        this.chats = chats;
    }

    public ArrayList<States> getStates() {
        return states;
    }

    public void setStates(ArrayList<States> states) {
        this.states = states;
    }
}
