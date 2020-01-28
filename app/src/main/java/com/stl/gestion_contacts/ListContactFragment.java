package com.stl.gestion_contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Comparator;


public class ListContactFragment extends MainFragment {

    private static ListView contactListView;
    private Context context;
    Spinner spinner;

    public ListContactFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        contactListView = view.findViewById(R.id.contactListView);
        contactListView.setAdapter(MainActivity.cm.getObjectAdapter());
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_contact(MainActivity.cm.getObjectAdapter().getItem(index), index);
            }
        });
        spinner = view.findViewById(R.id.spinner);
        setSpinnerSort();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ContactComparator selected = (ContactComparator)parentView.getItemAtPosition(position);
                switch (selected) {
                    case Alphabetic:
                        ((MainActivity)context).setContactComp(ContactComparator.Alphabetic);
                        break;
                    case LastMsg:
                        ((MainActivity)context).setContactComp(ContactComparator.LastMsg);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        return view;
    }

    public void open_contact (Contact contact, int index) {
        Intent intent = new Intent(context, ContactActivity.class);
        intent.putExtra("EXTRA_CONTACT", contact);
        intent.putExtra("CONTACT_POSITION", index);
        startActivity(intent);
    }


    private void setSpinnerSort() {
        ContactComparator[] sortMode = ContactComparator.values();
        int i =0;
        for (ContactComparator comp : sortMode) {
            if (comp == MainActivity.contactComp) {
                ContactComparator temp = sortMode[0];
                sortMode[0] = comp;
                sortMode[i] = temp;
                break;
            }
            i++;
        }
        ArrayAdapter<ContactComparator> sortAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sortMode);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(sortAdapter);

    }


}
