package com.stl.gestion_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupActivity extends AppCompatActivity {

    private Group group;
    private int index;
    private SMS_Sender sms_sender;

    private ListView listView;
    private ArrayAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group);
        group = (Group) getIntent().getSerializableExtra("EXTRA_GROUP");
        sms_sender = new SMS_Sender(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(group.getName());

        listView = findViewById(R.id.contactListView);


        adapter = MainActivity.cgm.get(group.getName()).getObjectAdapter();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(MainActivity.cm.getObjectsList().get(adapter.getItem(index)), adapter.getItem(index));
            }
        });
        adaptSizeListView();

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

    private void adaptSizeListView () {
        if (adapter.getCount() > 0) {
            View item = adapter.getView(0, null, listView);
            item.measure(0, 0);
            int nb_elements_displayed = (int) (adapter.getCount()) * item.getMeasuredHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nb_elements_displayed);
            listView.setLayoutParams(params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptSizeListView();
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
