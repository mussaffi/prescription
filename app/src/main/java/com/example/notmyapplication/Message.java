package com.example.notmyapplication;

import java.sql.Timestamp;
import java.util.Date;

public class Message
{
    private String text;
    private String FromUser;

    public Message(String text, String FromUser)
    {
        this.text = text;
        this.FromUser = FromUser;
    }

    public Message()
    {

    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getFromUser() {
        return FromUser;
    }

    public void setFromUser(String fromUser) {
        FromUser = fromUser;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "text='" + text + '\'' +
                ", FromUser='" + FromUser + '\'' +
                '}';
    }
}