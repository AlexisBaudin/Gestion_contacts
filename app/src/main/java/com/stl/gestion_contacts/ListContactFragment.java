package com.stl.gestion_contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ListContactFragment extends MainFragment {

    private static ListView contactListView;
    private Context context;

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
        return view;
    }

    public void open_contact (Contact contact, int index) {
        Intent intent = new Intent(context, ContactActivity.class);
        intent.putExtra("EXTRA_CONTACT", contact);
        intent.putExtra("CONTACT_POSITION", index);
        startActivity(intent);
    }




}
