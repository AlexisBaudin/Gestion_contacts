package com.stl.gestion_contacts;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        TextView name = findViewById(R.id.name);

        name.setText(group.getName());
    }

    public void delGroup (View view) {
        MainActivity.gm.removeObject(index);
        finish();
    }

    public void sendSMStest (View view) {
        for (Contact c : group.getContacts()) {
            sms_sender.send_SMS(c, "Message test application !");
        }
    }

}
