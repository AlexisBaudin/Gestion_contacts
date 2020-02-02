package com.stl.gestion_contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

public class ListGroupFragment extends MainFragment {


    private static ListView groupListView;
    private Context context;
    Spinner spinner;

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
        spinner = view.findViewById(R.id.spinner);
        setSpinnerSort();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                GroupComparator selected = (GroupComparator)parentView.getItemAtPosition(position);
                switch (selected) {
                    case Alphabetic:
                        ((MainActivity)context).setGroupComp(GroupComparator.Alphabetic);
                        break;
                    case LastMsg:
                        ((MainActivity)context).setGroupComp(GroupComparator.LastMsg);
                        break;
                    case NumberContacts:
                        ((MainActivity)context).setGroupComp(GroupComparator.NumberContacts);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        return view;
    }

    public void filterGroup(String query) {
        groupListView.setFilterText(query);
    }

    public void open_group (Group group, int index) {
        Intent intent = new Intent(context, GroupActivity.class);
        intent.putExtra("EXTRA_GROUP", group);
        startActivity(intent);
    }


    private void setSpinnerSort() {
        GroupComparator[] sortMode = GroupComparator.values();
        int i =0;
        for (GroupComparator comp : sortMode) {
            if (comp == MainActivity.groupComp) {
                GroupComparator temp = sortMode[0];
                sortMode[0] = comp;
                sortMode[i] = temp;
                break;
            }
            i++;
        }
        ArrayAdapter<GroupComparator> sortAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, sortMode);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(sortAdapter);

    }

}
