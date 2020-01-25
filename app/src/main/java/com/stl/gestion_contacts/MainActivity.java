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
import android.text.InputType;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /* ******************************************************
     * Managers pour les données stockées et listées
     * *****************************************************/
    /** Manager des contacts */
    static ObjectManager<Contact> cm;
    /** Manager des groupes */
    static ObjectManager<Group> gm;
    /** Manager des contacts pour chaque groupe, identifié par son nom.
     * Garde en mémoire les index du ContactManager*/
    static Map<String,ObjectManager<Integer>> cgm;

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


        /**
         * Création d'un nouveau groupe ou contact
         */
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
                            createGroup();
                }
            }
        });

        cm = new ObjectManager<Contact>(this, "contacts.txt", R.layout.item_contact);
        gm = new ObjectManager<Group>(this, "groups.txt", R.layout.item_group);
        cgm = new HashMap<>();
        for (Group g : gm.getObjectsList()) {
            cgm.put(g.getName(), new ObjectManager<Integer>(this, "group_"+g.getName()+".txt", R.layout.item_contact));

        }
        printState();
    }

    public void open_formulaire_contact (View view) {
        Intent intent = new Intent(this, FormulaireActivity.class);
        startActivity(intent);
    }

    private void createGroup() {
        final EditText taskEditText = new EditText(this);
        taskEditText.setHint(R.string.name_group);
        taskEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nouveau Groupe")
                .setView(taskEditText)
                .setPositiveButton("CREER", null)
                .setNegativeButton("ANNULER", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String name;
                        name = String.valueOf(taskEditText.getText());
                        if (name.equals("")) {
                            taskEditText.setError("Entrez un nom");
                        }
                        else if (alreadyExistsGroup(name)) {
                            taskEditText.setError("Ce groupe existe déjà");
                        }
                        else {
                            Group g = new Group(name);
                            gm.addObject(g);
                            cgm.put(g.getName(), new ObjectManager<Integer>(MainActivity.this, "group_"+name+".txt", R.layout.item_contact));
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public boolean alreadyExistsGroup(String name) {
        for (Group g : gm.getObjectsList()) {
            if (g.getName().equals(name))
                return true;
        }
        return false;
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

    public void printState() {
        System.out.println("\n==========================");
        System.out.println("Contacts");
        System.out.println("==========================");
        for (Contact c : cm.getObjectsList()) {
            System.out.println(c.getText());
        }
        System.out.println("==========================");
        System.out.println("Groups");
        System.out.println("==========================");
        for (Group g : gm.getObjectsList()) {
            System.out.println(g.getName());
        }
        System.out.println("==========================");
        System.out.println("Groups Composition");
        System.out.println("==========================");
        for (Group g : gm.getObjectsList()) {
            System.out.print(g.getName() + " ==>");
            System.out.println(cgm.get(g.getName()).getObjectsList().size());
            for (Integer i : cgm.get(g.getName()).getObjectsList()) {
                System.out.print(i + " | ");
            }
            System.out.println();
        }
    }
}
