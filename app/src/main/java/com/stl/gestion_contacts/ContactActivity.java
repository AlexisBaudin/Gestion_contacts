package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    /*private Contact contact;*/
    /**
     * Index dans Le main ContactManager
     */
    private int index;
    private SMS_Sender sms_sender;
    private String textToSend = "Message Test application !";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        index = getIntent().getIntExtra("CONTACT_POSITION", -1);
        Contact contact = MainActivity.cm.getObjectsList().get(index);
        sms_sender = new SMS_Sender(this);

        setTitle(contact.getText());

        TextView num_tel = findViewById(R.id.contact_num);
        TextView mail = findViewById(R.id.contact_mail);

        num_tel.setText(contact.num_tel);
        mail.setText(contact.mail);
    }

    public void delContact (View view) {

        for (String groupName : MainActivity.cgm.keySet()) {
            List<Integer> positions = MainActivity.cgm.get(groupName).getObjectsList();
            for(int i = 0; i < positions.size(); i++) {
                Integer contactPos = positions.get(i);
                if (index == contactPos) {
                    MainActivity.cgm.get(groupName).removeObject(i);
                    break;
                }
            }
            for (int i = 0; i <positions.size(); i++) {
                Integer contactPos = MainActivity.cgm.get(groupName).getObjectsList().get(i);
                if (index < contactPos) {
                    positions.set(i, contactPos-1);

                }
                MainActivity.cgm.get(groupName).getObjectAdapter().notifyDataSetChanged();
                MainActivity.cgm.get(groupName).saveObjects();
            }
        }
        MainActivity.cm.removeObject(index);
        finish();
    }

    public void sendSMStest (View view) {
        sms_sender.send_SMS(MainActivity.cm.getObjectsList().get(index), textToSend);
    }
}
