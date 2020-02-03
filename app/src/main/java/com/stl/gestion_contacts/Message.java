package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Message implements Serializable,Printable {

    private String msg;
    private Date date;
    private ArrayList<Contactable> destinataires;

    public Message (String msg, Date date,
                    ArrayList<Contact> contactDestinataires, ArrayList<Group> groupDestinataires) {
        this.msg = msg;
        this.date = date;

        destinataires = new ArrayList<Contactable>();
        for (Group g : groupDestinataires) {
            destinataires.add(g);
        }
        for (Contact c : contactDestinataires) {
            destinataires.add(c);
        }
    }

    public String getMessage () {
        return msg;
    }

    public Date getDate () {
        return date;
    }

    public ArrayList<Contactable> getDestinataires () {
        return destinataires;
    }

    @Override
    public String getText() {
        return msg;
    }
}
