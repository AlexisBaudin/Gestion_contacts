package com.stl.gestion_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class GroupActivity extends AppCompatActivity {

    private Group group;
    private int index;
    private SMS_Sender sms_sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group);
        group = (Group) getIntent().getSerializableExtra("EXTRA_GROUP");
        index = getIntent().getIntExtra("GROUP_POSITION", -1);
        sms_sender = new SMS_Sender(this);

        setTitle(group.getName());

        ListView contacts = findViewById(R.id.contactListView);


        final ArrayAdapter<Contact> adapter = MainActivity.cgm.get(group.getName()).getObjectAdapter();

        contacts.setAdapter(adapter);
        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(adapter.getItem(index), index);
            }
        });

    }

    public void delGroup (View view) {
        MainActivity.gm.removeObject(index);
        finish();
    }

    public void sendSMStest (View view) {
        for (Contact c : MainActivity.cgm.get(group.getName()).getObjectsList()) {
            sms_sender.send_SMS(c, "Message test application !");
        }
    }

    public void manageContacts(View view) {
        Intent intent = new Intent(this, ManageContactInGroupActivity.class);
        intent.putExtra("EXTRA_GROUP", group);
        startActivity(intent);
    }

    public void open_contact (Contact contact, int index) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("EXTRA_CONTACT", contact);
        intent.putExtra("CONTACT_POSITION", index);
        startActivity(intent);
    }

}
