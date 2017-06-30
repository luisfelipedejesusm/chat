package com.firebasetest.octagono.firebasetest2.Models;

import java.util.Date;

/**
 * Created by OCTAGONO on 6/27/2017.
 */

public class Message
{
    private String messageFrom;
    private String messageTo;
    private String messageBody;
    private Date messageDate;
    private boolean visto;

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public Message() {
    }

    public Message(String messageFrom, String messageTo, String messageBody) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.messageBody = messageBody;
    }

    public Message(String messageFrom, String messageTo, String messageBody, Date messageDate) {

        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.messageBody = messageBody;
        this.messageDate = messageDate;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
