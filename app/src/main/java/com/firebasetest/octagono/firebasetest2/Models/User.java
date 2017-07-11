package com.firebasetest.octagono.firebasetest2.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by OCTAGONO on 6/23/2017.
 */

public class User implements Serializable{

    private String Email;
    private String Name;
    private String CantNoVistos;
    private ArrayList<Message> msgNoVistos;

    public String getFoto64() {
        return foto64;
    }

    public void setFoto64(String foto64) {
        this.foto64 = foto64;
    }

    private String foto64;

    public ArrayList<Message> getMsgNoVistos() {
        return msgNoVistos;
    }

    public void setMsgNoVistos(ArrayList<Message> msgNoVistos) {
        this.msgNoVistos = msgNoVistos;
    }

    public String getCantNoVistos() {
        return CantNoVistos;
    }

    public void setCantNoVistos(String cantNoVistos) {
        CantNoVistos = cantNoVistos;
    }

    public User() {
    }

    public User(String email, String name) {
        Email = email;

        Name = name;
    }

    public int getCantMsgNoVistos(){
        if (msgNoVistos==null){
            return 0;
        }else{
            int counter = 0;
            for (Message msg:msgNoVistos) {
                if (msg.getMessageFrom().equals(getName()) && !msg.isVisto()){
                    counter++;
                }
            }
            return counter;
        }
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public User(String email) {
        Email = email;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
