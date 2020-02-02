package com.stl.gestion_contacts;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageContactInGroupActivity extends AppCompatActivity {

    private Group group;

    @Override @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        group = (Group)getIntent().getSerializableExtra("EXTRA_GROUP");


        setContentView(R.layout.activity_manage_contact);
        setTitle("Ajouter dans "+group.getName());

        ListView listView = findViewById(R.id.contactListView);
        listView.setItemsCanFocus(false);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < MainActivity.cm.getObjectsList().size(); i++) {
            list.add(i);
        }

        ManageContactInGroupAdapter adapter = new ManageContactInGroupAdapter(this, R.layout.item_manage_contact, list, group);
        listView.setAdapter(adapter);

    }

    public void save(View view) {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
