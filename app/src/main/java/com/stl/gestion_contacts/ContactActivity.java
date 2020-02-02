package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    /*private Contact contact;*/
    /**
     * Index dans Le main ContactManager
     */
    private int index;
    TextView num_tel;
    Contact contact;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);



        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        index = getIntent().getIntExtra("CONTACT_POSITION", -1);
        num_tel = findViewById(R.id.contact_num);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        contact = MainActivity.cm.getObjectsList().get(index);

        num_tel.setText(contact.getNumTel());
        collapsingToolbarLayout.setTitle(contact.getText());

        FloatingActionButton fab_sms = findViewById(R.id.fab_sms);
        fab_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, SmsActivity.class);
                intent.putExtra("INTENT_CONTACT_POSITION", index);
                ContactActivity.this.startActivity(intent);
            }
        });

        FloatingActionButton fab_trash = findViewById(R.id.fab_trash);
        fab_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delContact(view);
            }
        });

        FloatingActionButton fab_modifier = findViewById(R.id.fab_modifier);
        fab_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifierContact(view);
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String newName = data.getStringExtra("name");
                String newNumTel = data.getStringExtra("numTel");
                num_tel.setText(newNumTel);
                collapsingToolbarLayout.setTitle(newName);
            }
        }
    }

    private void displayData () {

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

    public void modifierContact (View view) {
        Intent intent = new Intent(this, FormulaireActivity.class);
        intent.putExtra("CONTACT_POSITION", index);
        startActivityForResult(intent, 1);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

}
