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

public class ListMessageFragment extends MainFragment {

    private static ListView messageListView;
    private Context context;

    public ListMessageFragment (Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        messageListView = view.findViewById(R.id.messageListView);
        messageListView.setAdapter(MainActivity.mm.getObjectAdapter());
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                open_message(position);
            }
        });

        return view;
    }

    private void open_message (int position) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("MESSAGE_POSITION", position);
        startActivity(intent);
    }

}

