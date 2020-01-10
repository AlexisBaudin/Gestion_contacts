package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Array of strings...
    private ListView contactListView;
    public static List<Contact> contactList = new ArrayList<>();
    public static ArrayAdapter<Contact> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
