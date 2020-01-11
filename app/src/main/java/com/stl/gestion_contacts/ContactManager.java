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
    private String contacts_filename;

    ContactManager(Context context, String contacts_filename) {

        this.context = context;
        this.contacts_filename = contacts_filename;

        internalStorage = new InternalStorage(context);
        try {
            // Retrieve the list from internal storage
            contactList = (ArrayList<Contact>) internalStorage.readObject(
                    contacts_filename,
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
                contacts_filename,
                contactList,
                "save_contacts() MainActivity");
    }

}
