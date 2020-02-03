package com.stl.gestion_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageContactInGroupActivity extends AppCompatActivity {


    private Group group;
    private ManageContactInGroupAdapter adapter;
    private ListView listView;

    /*public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_group_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView)item.getActionView();

        searchView.setQueryHint("Entrez un nom");

        searchView.onActionViewExpanded();
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText.toLowerCase(Locale.getDefault()));
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        group = (Group)getIntent().getSerializableExtra("EXTRA_GROUP");


        setContentView(R.layout.activity_manage_contact);
        setTitle("Ajouter dans "+group.getName());

        listView = findViewById(R.id.contactListView);
        listView.setItemsCanFocus(false);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < MainActivity.cm.getObjectsList().size(); i++) {
            list.add(i);
        }

        adapter = new ManageContactInGroupAdapter(this, R.layout.item_manage_contact, list, group);
        listView.setAdapter(adapter);
        adaptSizeListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptSizeListView();
    }

    private void adaptSizeListView () {
        if (adapter.getCount() > 0) {
            View item = adapter.getView(0, null, listView);
            item.measure(0, 0);
            int nb_elements_displayed = (int) (adapter.getCount() * 1.25) * item.getMeasuredHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nb_elements_displayed);
            listView.setLayoutParams(params);
        }
    }

    public void save(View view) {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
