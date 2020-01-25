package com.stl.gestion_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ManageContactInGroupAdapter extends ArrayAdapter<Contact> {

    private final int layoutResource;
    private final Group group;
    //Set<Contact> checked;

    public ManageContactInGroupAdapter(@NonNull Context context, int resource, @NonNull List<Contact> contacts, Group group/*, Set<Contact> checks*/) {
        super(context, resource, contacts);
        this.layoutResource = resource;
        this.group = group;
        //this.checked = checks;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println(position);
        final Contact c = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(layoutResource, null);
        }

        final CheckBox checkbox = convertView.findViewById(R.id.check);
        checkbox.setChecked(MainActivity.cgm.get(group.getName()).getObjectsList().contains(c));
        checkbox.setText(c.getText());
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox.isChecked())
                    MainActivity.cgm.get(group.getName()).removeObject(c);
                else
                    MainActivity.cgm.get(group.getName()).addObject(c);
            }
        });

        return convertView;
    }


}
