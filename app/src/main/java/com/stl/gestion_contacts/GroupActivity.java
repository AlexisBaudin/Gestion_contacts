package com.stl.gestion_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(group.getName());

        ListView listView = findViewById(R.id.contactListView);


        final ArrayAdapter<Integer> adapter = MainActivity.cgm.get(group.getName()).getObjectAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(MainActivity.cm.getObjectsList().get(adapter.getItem(index)), adapter.getItem(index));
            }
        });

        FloatingActionButton fab_sms = findViewById(R.id.fab_sms);
        fab_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, SmsActivity.class);
                intent.putExtra("INTENT_GROUP_POSITION", index);
                GroupActivity.this.startActivity(intent);
            }
        });

        FloatingActionButton fab_trash = findViewById(R.id.fab_trash);
        fab_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delGroup(view);
            }
        });

        FloatingActionButton fab_modifier = findViewById(R.id.fab_modifier);
        fab_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageContacts(view);
            }
        });

    }

    public void delGroup (View view) {
        MainActivity.gm.removeObject(index);
        MainActivity.cgm.remove(this.group.getName());
        finish();
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

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
