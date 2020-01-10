package com.stl.gestion_contacts;

import java.io.Serializable;

public class Contact implements Serializable {
    String nom;
    String prenom;
    String num_tel;
    String mail;

    public Contact (String nom, String prenom, String num_tel, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.mail = mail;
    }

}
