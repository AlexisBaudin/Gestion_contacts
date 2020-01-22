package com.stl.gestion_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public  class ObjectAdapter<T extends Printable> extends ArrayAdapter<T> {
    private final List<T> objects;
    private final int layoutResource;

    public ObjectAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        T t = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(this.layoutResource, null);
        }

        TextView name = convertView.findViewById(R.id.name);
        name.setText(t.getText());

        return convertView;
    }
}