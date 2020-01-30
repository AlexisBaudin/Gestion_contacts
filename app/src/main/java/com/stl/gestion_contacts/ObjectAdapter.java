package com.stl.gestion_contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public  class ObjectAdapter<T> extends ArrayAdapter<T> {


    private final List<T> objects;
    private ArrayList<T> filtered = null;
    private final int layoutResource;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(filtered);
        }
        else
        {
            for (T t : filtered) {
                if (((Printable)t).getText().toLowerCase(Locale.getDefault()).contains(charText)) {
                    objects.add(t);
                }
            }
        }
        notifyDataSetChanged();
    }

    public ObjectAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        this.filtered = new ArrayList<T>();
        this.filtered.addAll(objects);
        this.layoutResource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        T t = getItem(position);
        RecyclerView.ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(this.layoutResource, null);

        }

        if (t instanceof Printable) {
            TextView text = convertView.findViewById(R.id.text);
            text.setText(((Printable)(t)).getText());
        }
        if (t instanceof Integer) {
            TextView text = convertView.findViewById(R.id.text);
            text.setText((MainActivity.cm.getObjectsList().get((Integer) (t))).getText());
        }

        return convertView;
    }

}