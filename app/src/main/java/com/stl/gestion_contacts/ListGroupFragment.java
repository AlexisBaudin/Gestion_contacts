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

public class ListGroupFragment extends MainFragment {


    private static ListView groupListView;
    private Context context;

    public ListGroupFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        groupListView = view.findViewById(R.id.groupListView);
        groupListView.setAdapter(MainActivity.gm.getObjectAdapter());
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                open_group(MainActivity.gm.getObjectAdapter().getItem(index), index);
            }
        });
        return view;
    }

    public void open_group (Group group, int index) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra("EXTRA_GROUP", group);
        intent.putExtra("GROUP_POSITION", index);
        startActivity(intent);
    }
}
