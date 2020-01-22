package com.stl.gestion_contacts;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class ContactManager {

    public ArrayList<Contact> contactList;
    public ArrayAdapter<Contact> contactAdapter;

    private Context context;
    private InternalStorage internalStorage;
    public final static String CONTACTS_FILENAME = "contacts.txt";

    ContactManager(Context context) {

        this.context = context;

        internalStorage = new InternalStorage(context);
        try {
            // Retrieve the list from internal storage
            contactList = (ArrayList<Contact>) internalStorage.readObject(
                    CONTACTS_FILENAME,
                    "MainActivity onCreate");
        }
        catch (IOException e) {
            contactList = new ArrayList();
        }

        contactAdapter = new ContactAdapter(
                context,
                R.layout.item_contact,
                contactList);

    }

    public void add_contact(Contact contact) {
        contactList.add(contact);
        contactAdapter.notifyDataSetChanged();
        save_contacts();
    }

    public void remove_contact(int position) {
        contactList.remove(position);
        contactAdapter.notifyDataSetChanged();
        save_contacts();
    }

    private void save_contacts () {
        // Save the list of entries to internal storage
        internalStorage.writeObject(
                CONTACTS_FILENAME,
                contactList,
                "save_contacts() MainActivity");
    }

}
