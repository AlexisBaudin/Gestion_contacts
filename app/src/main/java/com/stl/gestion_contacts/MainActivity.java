package com.stl.gestion_contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static  ObjectManager<Contact> cm;
    public static ObjectManager<Group> gm;
    public static final int SMS_PERMISSIONS_REQUEST = 1;
    public static boolean allow_sms = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSIONS_REQUEST);

        }
        else {
            allow_sms = true;
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                for(Fragment fragment : fragments){
                    if(fragment != null && fragment.getUserVisibleHint())
                        if (fragment instanceof ListContactFragment)
                            open_formulaire_contact(view);
                        else if (fragment instanceof  ListGroupFragment)
                            creerGroupe();
                }
            }
        });

        cm = new ObjectManager<Contact>(this, "contacts.txt", R.layout.item_contact);
        gm = new ObjectManager<Group>(this, "groups.txt", R.layout.item_group);

    }

    public MainFragment getVisibleFragment(){

        return null;
    }

    public void open_formulaire_contact (View view) {
        Intent intent = new Intent(this, FormulaireActivity.class);
        startActivity(intent);
    }

    private void creerGroupe() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nouveau Groupe")
                .setMessage("Entrez un nom")
                .setView(taskEditText)
                .setPositiveButton("CREER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = String.valueOf(taskEditText.getText());
                        gm.addObject(new Group(name));

                    }
                })
                .setNegativeButton("ANNULER", null)
                .create();
        dialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    allow_sms = true;
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    allow_sms = false;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
