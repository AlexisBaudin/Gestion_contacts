package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private int position;
    private Message message;

    private TextView sms;

    private LinearLayout destGroupLayout;
    private ArrayList<Group> destinatairesGroup;
    private ListView destinatairesGroupListView;
    private ObjectAdapter<Group> destinatairesGroupAdapter;

    private LinearLayout destContactLayout;
    private ArrayList<Contact> destinatairesContact;
    private ListView destinatairesContactListView;
    private ObjectAdapter<Contact> destinatairesContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("MESSAGE_POSITION", -1);
        message = MainActivity.mm.getObjectsList().get(position);

        sms = findViewById(R.id.sms);
        destGroupLayout = findViewById(R.id.layout_dest_group);
        destContactLayout = findViewById(R.id.layout_dest_contact);
        destinatairesGroupListView = findViewById(R.id.destinataires_groupes);
        destinatairesContactListView = findViewById(R.id.destinataires_contacts);

        sms.setText(message.getMessage());

        destinatairesContact = new ArrayList<Contact>();
        destinatairesGroup = new ArrayList<Group>();
        for (Contactable c : message.getDestinataires()) {
            if (c instanceof Group) {
                destinatairesGroup.add((Group) c);
            }
            if (c instanceof Contact) {
                destinatairesContact.add((Contact) c);
            }
        }

        destinatairesContactAdapter = new ObjectAdapter<Contact>(
                MessageActivity.this,
                R.layout.item_destinataire_sms,
                destinatairesContact
        );
        destinatairesContactListView.setAdapter(destinatairesContactAdapter);

        destinatairesGroupAdapter = new ObjectAdapter<Group>(
                MessageActivity.this,
                R.layout.item_destinataire_sms,
                destinatairesGroup
        );
        destinatairesGroupListView.setAdapter(destinatairesGroupAdapter);

        /** Pour que les listView soient de la bonne taille dans le scrollView */

        if (!destinatairesGroup.isEmpty()) {
            View itemGroup = destinatairesGroupAdapter.getView(0, null, destinatairesGroupListView);
            itemGroup.measure(0,0);
            int nbElementsGroup = (int) (destinatairesGroupAdapter.getCount()*1.15) * itemGroup.getMeasuredHeight();
            LinearLayout.LayoutParams paramsGroup = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nbElementsGroup);
            destinatairesGroupListView.setLayoutParams(paramsGroup);
            destGroupLayout.setVisibility(View.VISIBLE);
        }
        if (!destinatairesContact.isEmpty()) {
            View itemContact = destinatairesContactAdapter.getView(0, null, destinatairesContactListView);
            itemContact.measure(0,0);
            int nbElementsContact = (int) (destinatairesContactAdapter.getCount()*1.15) * itemContact.getMeasuredHeight();
            LinearLayout.LayoutParams paramsContact = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nbElementsContact);
            destinatairesContactListView.setLayoutParams(paramsContact);
            destContactLayout.setVisibility(View.VISIBLE);
        }

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

    public void deleteMessage(View view) {
        MainActivity.mm.removeObject(position);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
