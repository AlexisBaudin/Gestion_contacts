package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    private Contact contact;
    private int index;
    private SMS_Sender sms_sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contact = (Contact) getIntent().getSerializableExtra("EXTRA_CONTACT");
        index = getIntent().getIntExtra("CONTACT_POSITION", -1);
        sms_sender = new SMS_Sender(this);

        TextView name = findViewById(R.id.name);
        TextView num_tel = findViewById(R.id.contact_num);
        TextView mail = findViewById(R.id.contact_mail);

        name.setText(contact.prenom + " " + contact.nom);
        num_tel.setText(contact.num_tel);
        mail.setText(contact.mail);
    }

    public void delContact (View view) {
        MainActivity.cm.removeObject(index);
        finish();
    }

    public void sendSMStest (View view) {
        sms_sender.send_SMS(contact, "Message test application !");
    }
}
