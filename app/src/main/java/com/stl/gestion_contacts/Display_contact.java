package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Display_contact extends AppCompatActivity {

    Contact contact;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_contact);

        contact = (Contact) getIntent().getSerializableExtra("EXTRA_CONTACT");
        index = getIntent().getIntExtra("CONTACT_POSITION", -1);

        TextView name = findViewById(R.id.name);
        TextView num_tel = findViewById(R.id.contact_num);
        TextView mail = findViewById(R.id.contact_mail);

        name.setText(contact.prenom + " " + contact.nom);
        num_tel.setText(contact.num_tel);
        mail.setText(contact.mail);
    }

    public void del_contact (View view) {
        MainActivity.contactList.remove(index);
        MainActivity.contactAdapter.notifyDataSetChanged();
        finish();
    }
}
