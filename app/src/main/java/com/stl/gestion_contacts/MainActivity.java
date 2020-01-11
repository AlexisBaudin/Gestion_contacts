package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String contacts_filename = "contacts.txt";

    private static ListView contactListView;
    public static ArrayList<Contact> contactList = new ArrayList<>();
    public static ArrayAdapter<Contact> contactAdapter;

    private static InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internalStorage = new InternalStorage(this);

        try {
            // Retrieve the list from internal storage
            contactList = (ArrayList<Contact>) internalStorage.readObject(
                    contacts_filename,
                    "MainActivity onCreate");
        }
        catch (IOException e) {
            contactList = new ArrayList();
        }

        contactListView = findViewById(R.id.contactListView);
        contactAdapter = new ContactAdapter(
                this,
                R.layout.item_contact,
                contactList);
        contactListView.setAdapter(contactAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(contactAdapter.getItem(index), index);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactAdapter.notifyDataSetChanged();
    }

    public static void add_contact(Contact contact) {
        contactList.add(contact);
        contactAdapter.notifyDataSetChanged();
        save_contacts();
    }

    public static void remove_contact(int position) {
        contactList.remove(position);
        contactAdapter.notifyDataSetChanged();
        save_contacts();
    }

    private static void save_contacts () {
        // Save the list of entries to internal storage
        internalStorage.writeObject(
                contacts_filename,
                contactList,
                "save_contacts() MainActivity");
    }


    public void open_formulaire_contact (View view) {
        Intent intent = new Intent(this, Formulaire.class);
        startActivity(intent);
    }

    public void open_contact (Contact contact, int index) {
        Intent intent = new Intent(this, Display_contact.class);
        intent.putExtra("EXTRA_CONTACT", contact);
        intent.putExtra("CONTACT_POSITION", index);
        startActivity(intent);
    }
}
