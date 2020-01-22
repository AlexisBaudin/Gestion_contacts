package com.stl.gestion_contacts;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable, Printable{

    private String name;
    ArrayList<Contact> contacts;

    public Group(String name) {
        this.name = name;
        this.contacts = new ArrayList<>();
    }

    public void removeContact(int n) {
        this.contacts.remove(n);
    }

    public void addContact(Contact c) {
        contacts.add(c);
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return name;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}
