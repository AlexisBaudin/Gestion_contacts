package com.stl.gestion_contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SmsActivity extends AppCompatActivity {

    private int intentContactPosition;

    private SMS_Sender smsSender;

    private EditText smsEditText;

    private SearchView searchContact;
    private ListView searchContactListView;
    private ObjectAdapter<Contact> searchContactAdapter;

    private LinearLayout destContactLayout;
    private ArrayList<Contact> destinatairesContact;
    private ListView destinatairesContactListView;
    private ObjectAdapter<Contact> destinatairesContactAdapter;

    private SearchView searchGroup;
    private ListView searchGroupListView;
    private ObjectAdapter<Group> searchGroupAdapter;

    private LinearLayout destGroupLayout;
    private ArrayList<Group> destinatairesGroup;
    private ListView destinatairesGroupListView;
    private ObjectAdapter<Group> destinatairesGroupAdapter;


    private final static double limitHeightDestinataire = 0;
    private final static double limitHeightSelect = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smsSender = new SMS_Sender(this);
        smsEditText = findViewById(R.id.sms);

        searchContact = findViewById(R.id.search_contact);
        searchContactListView = findViewById(R.id.list_resultat_contact);
        destinatairesContactListView = findViewById(R.id.destinataires_contacts);
        searchGroup = findViewById(R.id.search_group);
        searchGroupListView = findViewById(R.id.list_resultat_group);
        destinatairesGroupListView = findViewById(R.id.destinataires_groupes);

        /**
         * Contact search view
         */
        destContactLayout = findViewById(R.id.layout_dest_contact);
        destinatairesContact = new ArrayList<Contact>();

        intentContactPosition = getIntent().getIntExtra("INTENT_CONTACT_POSITION", -1);
        if (intentContactPosition >= 0) {
            destinatairesContact.add(MainActivity.cm.getObjectsList().get(intentContactPosition));
            destContactLayout.setVisibility(View.VISIBLE);
        }

        destinatairesContactAdapter = new ObjectAdapter<Contact>(
                SmsActivity.this,
                R.layout.item_destinataire_sms,
                destinatairesContact
        );
        destinatairesContactListView.setAdapter(destinatairesContactAdapter);
        destinatairesContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinatairesContact.remove(position);
                destinatairesContactAdapter.notifyDataSetChanged();
                if (destinatairesContact.isEmpty()) {
                    destContactLayout.setVisibility(View.GONE);
                }
                else {
                    setHeightListView(limitHeightDestinataire, destinatairesContactAdapter, destinatairesContactListView);
                }
            }
        });

        searchContactAdapter = new ObjectAdapter<Contact>(
                SmsActivity.this,
                R.layout.item_contact,
                MainActivity.cm.getObjectsList()
        );
        searchContactListView.setAdapter(searchContactAdapter);
        searchContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destContactLayout.setVisibility(View.VISIBLE);
                Contact contact = MainActivity.cm.getObjectsList().get(position);
                if (destinatairesContact.contains(contact)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Destinataire en double", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {
                    destinatairesContact.add(0,MainActivity.cm.getObjectsList().get(position));
                    destinatairesContactAdapter.notifyDataSetChanged();
                    setHeightListView(limitHeightDestinataire, destinatairesContactAdapter, destinatairesContactListView);
                }
                searchContactListView.setVisibility(View.GONE);
                refreshSearchView(searchContact, searchContactAdapter);
            }
        });

        searchContact.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().equals("")) {
                    searchContactListView.setVisibility(View.GONE);
                }
                else {
                    searchContactAdapter.filter(newText.toLowerCase(Locale.getDefault()));
                    if (searchContactAdapter.getCount() != 0) {
                        searchContactListView.setVisibility(View.VISIBLE);
                        setHeightListView(limitHeightSelect, searchContactAdapter, searchContactListView);
                    }
                    else {
                        searchContactListView.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });



        /**
         * Groupe search view
         */
        destGroupLayout = findViewById(R.id.layout_dest_group);
        destinatairesGroup = new ArrayList<Group>();
        destinatairesGroupAdapter = new ObjectAdapter<Group>(
                SmsActivity.this,
                R.layout.item_destinataire_sms,
                destinatairesGroup
        );
        destinatairesGroupListView.setAdapter(destinatairesGroupAdapter);
        destinatairesGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinatairesGroup.remove(position);
                destinatairesGroupAdapter.notifyDataSetChanged();
                if (destinatairesGroup.isEmpty()) {
                    destGroupLayout.setVisibility(View.GONE);
                }
                else {
                    setHeightListView(limitHeightDestinataire, destinatairesGroupAdapter, destinatairesGroupListView);
                }
            }
        });

        searchGroupAdapter = new ObjectAdapter<Group>(
                SmsActivity.this,
                R.layout.item_group,
                MainActivity.gm.getObjectsList()
        );
        searchGroupListView.setAdapter(searchGroupAdapter);
        searchGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destGroupLayout.setVisibility(View.VISIBLE);
                Group group = MainActivity.gm.getObjectsList().get(position);
                if (destinatairesGroup.contains(group)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Destinataire en double", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {
                    destinatairesGroup.add(0,MainActivity.gm.getObjectsList().get(position));
                    destinatairesGroupAdapter.notifyDataSetChanged();
                    setHeightListView(limitHeightDestinataire, destinatairesGroupAdapter, destinatairesGroupListView);
                }
                searchGroupListView.setVisibility(View.GONE);
                refreshSearchView(searchGroup, searchGroupAdapter);
            }
        });

        searchGroup.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().equals("")) {
                    searchGroupListView.setVisibility(View.GONE);
                }
                else {
                    searchGroupAdapter.filter(newText.toLowerCase(Locale.getDefault()));
                    if (searchGroupAdapter.getCount() != 0) {
                        searchGroupListView.setVisibility(View.VISIBLE);
                        setHeightListView(limitHeightSelect, searchGroupAdapter, searchGroupListView);
                    }
                    else {
                        searchGroupListView.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });


    }

    public void setHeightListView(double limit, ArrayAdapter adapter, ListView listView) {
        View item = adapter.getView(0, null, listView);
        item.measure(0, 0);

        int nb_elements_displayed;

        if(limit != 0 && adapter.getCount() > limit){
             nb_elements_displayed = (int) (limit * item.getMeasuredHeight());
        }
        else {
            nb_elements_displayed = (int) (adapter.getCount()*1.15) * item.getMeasuredHeight();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nb_elements_displayed);
        listView.setLayoutParams(params);
    }

    private void refreshSearchView(SearchView searchView, ObjectAdapter objectAdapter) {
        searchView.setQuery("", true);
        objectAdapter.filter("");
    }


    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!destinatairesContact.isEmpty()
                || !destinatairesGroup.isEmpty()
                || !smsEditText.getText().toString().trim().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Supprimer le SMS ?")
                    .setMessage("Si vous quittez, vous perdrez les données entrées.")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            refreshSearchView(searchContact, searchContactAdapter);
                            refreshSearchView(searchGroup, searchGroupAdapter);
                            finish();
                        }
                    }).create().show();
        }
        else {
            refreshSearchView(searchContact, searchContactAdapter);
            refreshSearchView(searchGroup, searchGroupAdapter);
            finish();
        }
    }

    public void sendSMS (View view) {
        if (smsEditText.getText().toString().trim().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Le SMS est vide.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (destinatairesGroup.isEmpty() && destinatairesContact.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer des destinataires.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            String sms = smsEditText.getText().toString();
            smsSender.send_SMS(sms, destinatairesContact, destinatairesGroup);

            refreshSearchView(searchContact, searchContactAdapter);
            refreshSearchView(searchGroup, searchGroupAdapter);
            finish();
        }
    }


}
