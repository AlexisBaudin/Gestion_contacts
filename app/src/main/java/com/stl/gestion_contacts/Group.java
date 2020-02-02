package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable, Printable, Contactable{

    private String name;
    private Date lastMsg;

    public Group(String name) {
        this.name = name;
        lastMsg = Contactable.NO_DATE;
    }


    public String getName() {
        return name;
    }

    public String getText() {
        return name;
    }

    @Override
    public Date getLastMsgDate() {
        return lastMsg;
    }

    public void setLastMsgDate(Date date) { lastMsg = date; }
}
