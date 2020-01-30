package com.stl.gestion_contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /* ******************************************************
     * Managers pour les données stockées et listées
     * *****************************************************/
    /** Manager des messages */
    static ObjectManager<Message> mm;
    /** Manager des contacts */
    static ObjectManager<Contact> cm;
    /** Manager des groupes */
    static ObjectManager<Group> gm;
    /** Manager des contacts pour chaque groupe, identifié par son nom.
     * Garde en mémoire les index du ContactManager*/
    static Map<String,ObjectManager<Integer>> cgm;

    static GroupComparator groupComp;
    static ContactComparator contactComp;

    public final static String GROUP_COMPARATOR_FILENAME = "groupComparator.txt";
    public final static String CONTACT_COMPARATOR_FILENAME = "contactComparator.txt";

    public static final int SMS_PERMISSIONS_REQUEST = 1;
    public static boolean allow_sms = false;
    public static final int MAX_NAME_GROUP_LENGTH = 25;
    public static final int MAX_NAME_CONTACT_LENGTH = 25;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

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
               if (currentFragment().equals(ListContactFragment.class)) {
                    cm.getObjectAdapter().filter(newText.toLowerCase(Locale.getDefault()));
                }
                else if (currentFragment().equals(ListGroupFragment.class))
                    gm.getObjectAdapter().filter(newText.toLowerCase(Locale.getDefault()));
                return false;
           }
       });

       return super.onCreateOptionsMenu(menu);
    }

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

        } else {
            allow_sms = true;
        }

        mm = new ObjectManager<Message>(this, "messages.txt", R.layout.item_message);
        cm = new ObjectManager<Contact>(this, "contacts.txt", R.layout.item_contact);
        gm = new ObjectManager<Group>(this, "groups.txt", R.layout.item_group);
        cgm = new HashMap<>();
        for (Group g : gm.getObjectsList()) {
            cgm.put(g.getName(), new ObjectManager<Integer>(this, "group_" + g.getName() + ".txt", R.layout.item_contact));

        }
        try {
            groupComp = (GroupComparator) InternalStorage.readObject(GROUP_COMPARATOR_FILENAME, "MainActivity oncreate");
        } catch (IOException e) {
            groupComp = GroupComparator.Alphabetic;
            InternalStorage.writeObject(GROUP_COMPARATOR_FILENAME, groupComp, "MainActivity oncreate");
        }
        try {
            contactComp = (ContactComparator) InternalStorage.readObject(CONTACT_COMPARATOR_FILENAME, "MainActivity oncreate");
        } catch (IOException e) {
            contactComp = ContactComparator.Alphabetic;
            InternalStorage.writeObject(CONTACT_COMPARATOR_FILENAME, contactComp, "MainActivity oncreate");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fab_sms = findViewById(R.id.fab_sms);

        /**
         * Création d'un nouveau groupe ou contact
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment().equals(ListGroupFragment.class))
                    createGroup();
                else if (currentFragment().equals(ListContactFragment.class))
                    open_formulaire_contact();
            }
        });

        /**
         * Envoi de message
         */
        fab_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSMS(MainActivity.this, -1);
            }
        });
    }



    public Class currentFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.getUserVisibleHint())
                if (fragment instanceof ListContactFragment) {
                    return ListContactFragment.class;
                }
                else if (fragment instanceof ListGroupFragment) {
                    return ListGroupFragment.class;
                }
                else if (fragment instanceof  ListMessageFragment) {
                    return ListMessageFragment.class;
                }

        }
        throw new RuntimeException("Fragment inconnu");
    }

    public void open_formulaire_contact () {
        Intent intent = new Intent(this, FormulaireActivity.class);
        startActivity(intent);
    }

    public static void openSMS (Context context, int position) {
        Intent intent = new Intent(context, SmsActivity.class);
        if (position >= 0) {
            intent.putExtra("INTENT_CONTACT_POSITION", position);
        }
        context.startActivity(intent);
    }

    private void createGroup() {
        final EditText taskEditText = new EditText(this);
        taskEditText.setHint(R.string.name_group);
        taskEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        taskEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_NAME_GROUP_LENGTH)});

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
                            if (groupComp == GroupComparator.Alphabetic)
                                sortGroup();
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



    public static void sortContacts() {
        ContactComparator comp = contactComp;
        ArrayList<Contact> list = cm.getObjectsList();
        ArrayList<Contact> listcpy = new ArrayList<>();
        int [] correspondance = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            listcpy.add(cm.getObjectsList().get(i));
        }
        switch (comp) {
            case Alphabetic:
                list.sort(new AlphabeticComparator());
                break;
            case LastMsg:
                list.sort(new LastMsgComparator());
                break;
        }
        for (int i = 0; i < list.size(); i++) {
            correspondance[i] = list.indexOf(listcpy.get(i));
        }
        //Mise à jour des index du ContactGroupeManager
        for (String groupName : cgm.keySet()) {
            ArrayList<Integer> pos = cgm.get(groupName).getObjectsList();
            for (int i = 0; i < pos.size(); i++) {
                cgm.get(groupName).getObjectsList().set(i, correspondance[pos.get(i)]);
            }
        }
        cm.getObjectAdapter().notifyDataSetChanged();
        cm.saveObjects();

    }

    public static void sortGroup(){
        GroupComparator comp = groupComp;
        ArrayList<Group> list = gm.getObjectsList();
        switch (comp) {
            case Alphabetic:
                list.sort(new AlphabeticComparator());
                break;
            case LastMsg:
                list.sort(new LastMsgComparator());
                break;
            case NumberContacts:
                list.sort(new NumberContactsComparator());
                break;
        }
        gm.getObjectAdapter().notifyDataSetChanged();
        gm.saveObjects();
    }

    static class LastMsgComparator implements Comparator<Contactable> {
        @Override
        public int compare(Contactable o1, Contactable o2) {
            return o2.getLastMsgDate().compareTo(o1.getLastMsgDate());
        }
    }

    static class AlphabeticComparator implements Comparator<Printable> {
        @Override
        public int compare(Printable o1, Printable o2) {
            return o1.getText().compareTo(o2.getText());
        }
    }


    static class NumberContactsComparator implements Comparator<Group> {
        @Override
        public int compare(Group o1, Group o2) {
            int n1 = MainActivity.cgm.get(o1.getName()).getObjectsList().size();
            int n2 = MainActivity.cgm.get(o2.getName()).getObjectsList().size();
            if (n1 < n2)
                return 1;
            else if (n1 > n2)
                return -1;
            else
                return 0;
        }
    }

    public void setGroupComp(GroupComparator comp) {
        if (comp == groupComp)
            return;
        groupComp = comp;
        InternalStorage.writeObject(GROUP_COMPARATOR_FILENAME, comp, "MainActivity oncreate");
        sortGroup();
    }

    public void setContactComp(ContactComparator comp) {
        if (comp == contactComp)
            return;
        contactComp = comp;
        InternalStorage.writeObject(CONTACT_COMPARATOR_FILENAME, comp, "MainActivity oncreate");
        sortContacts();
    }


}
