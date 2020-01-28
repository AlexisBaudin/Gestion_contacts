package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Group implements Serializable, Printable, Contactable{

    private String name;
    private Date lastMsg = new Date();

    public Group(String name) {
        this.name = name;
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
}
