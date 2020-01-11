package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static ContactManager contactManager;
    private static String contacts_filename = "contacts.txt";
    private static ListView contactListView;
    public static final int SMS_PERMISSIONS_REQUEST = 1;
    public static boolean allow_sms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSIONS_REQUEST);

        }
        else {
            allow_sms = true;
        }

        contactManager = new ContactManager(this, contacts_filename);

        contactListView = findViewById(R.id.contactListView);

        contactListView.setAdapter(contactManager.contactAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(contactManager.contactAdapter.getItem(index), index);
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    allow_sms = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    allow_sms = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
