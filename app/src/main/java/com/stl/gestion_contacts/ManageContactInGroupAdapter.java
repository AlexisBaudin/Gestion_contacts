package com.stl.gestion_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManageContactInGroupAdapter extends ArrayAdapter<Integer> {

    private String filterQuerry;
    private ArrayList<Integer> filtered;

    private List<Integer> contacts;
    private final int layoutResource;
    private final Group group;

    public ManageContactInGroupAdapter(@NonNull Context context, int resource, @NonNull List<Integer> contacts, Group group) {
        super(context, resource, contacts);
        this.layoutResource = resource;
        this.group = group;
        this.contacts = contacts;
        filtered = new ArrayList<>();
        filtered.addAll(contacts);
    }

    public void filter(String charText) {
        filterQuerry = charText;
        charText = charText.toLowerCase(Locale.getDefault());
        contacts.clear();
        if (charText.length() == 0) {
            contacts.addAll(filtered);
        }
        else
        {
            for (Integer i : filtered) {
                if (MainActivity.cm.getObjectsList().get(i).getText().toLowerCase(Locale.getDefault()).contains(charText)) {
                    contacts.add(i);
                }
            }
        }
        super.notifyDataSetChanged();
    }

    /*@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        filtered.clear();
        filtered.addAll(contacts);
        if (!filterQuerry.equals(""))
            filter(filterQuerry);
    }*/

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Contact c = MainActivity.cm.getObjectsList().get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layoutResource, null);
        }

        final CheckBox checkbox = convertView.findViewById(R.id.check);
        checkbox.setChecked(MainActivity.cgm.get(group.getName()).getObjectsList().contains(position));
        checkbox.setText(c.getText());
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox.isChecked())
                    MainActivity.cgm.get(group.getName()).removeObject(Integer.valueOf(position));
                else
                    MainActivity.cgm.get(group.getName()).addObject(position);
            }
        });

        return convertView;
    }
}
