package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Contact implements Serializable, Printable, Contactable{
    String nom;
    String num_tel;
    Date lastMsg;

    public Contact (String nom, String num_tel) {
        this.nom = nom;
        this.num_tel = num_tel;
        lastMsg = new Date();
    }

    @Override
    public String getText() {
        return nom;
    }

    @Override
    public Date getLastMsgDate() {
        return lastMsg;
    }

}
