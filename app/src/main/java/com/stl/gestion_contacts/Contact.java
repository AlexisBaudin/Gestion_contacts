package com.stl.gestion_contacts;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class Contact implements Serializable, Printable, Contactable{

    private String nom;
    private String num_tel;
    private Date lastMsg;

    public Contact (String nom, String num_tel) {
        this.nom = nom;
        this.num_tel = num_tel;
        this.lastMsg = Contactable.NO_DATE;
    }

    @Override
    public String getText() {
        return nom;
    }

    @Override
    public Date getLastMsgDate() { return lastMsg; }

    public String getNumTel() {
        return num_tel;
    }

    public String getName() {
        return nom;
    }

    public void setDateLastMsg (Date date) {
        lastMsg = date;
    }

}
