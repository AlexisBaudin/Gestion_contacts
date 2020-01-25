package com.stl.gestion_contacts;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageContactInGroupActivity extends AppCompatActivity {

    private Group group;

    @Override @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
