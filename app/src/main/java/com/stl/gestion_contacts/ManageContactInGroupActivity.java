package com.stl.gestion_contacts;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
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

        ManageContactInGroupAdapter adapter = new ManageContactInGroupAdapter(this, R.layout.item_manage_contact, MainActivity.cm.getObjectsList(), group);
        listView.setAdapter(adapter);

    }

    public void save(View view) {
        /*MainActivity.cgm.get(group.getName()).clear();

        for (Contact c :checked) {
            MainActivity.cgm.get(group.getName()).addObject(c);
        }*/
        finish();
    }

}
