package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable, Printable, Contactable{
    String nom;
    String prenom;
    String num_tel;
    String mail;
    Date lastMsg;

    public Contact (String nom, String prenom, String num_tel, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.mail = mail;
        lastMsg = new Date();
    }

    @Override
    public String getText() {
        return prenom + " " + nom;
    }

    @Override
    public Date getLastMsgDate() {
        return lastMsg;
    }
}
