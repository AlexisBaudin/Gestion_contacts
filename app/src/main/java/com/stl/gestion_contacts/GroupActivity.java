package com.stl.gestion_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_group);
        group = (Group) getIntent().getSerializableExtra("EXTRA_GROUP");
        index = getIntent().getIntExtra("GROUP_POSITION", -1);
        sms_sender = new SMS_Sender(this);

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
