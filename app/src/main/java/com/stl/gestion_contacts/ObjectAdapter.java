package com.stl.gestion_contacts;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
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

            if (t instanceof Contactable) {
                try {
                    TextView dateTextView = convertView.findViewById(R.id.date);
                    Date dateLastMsg = ((Contactable) t).getLastMsgDate();

                    SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yy");
                    String dateString;
                    if (formatterDay.format(Contactable.NO_DATE).equals(formatterDay.format(((Contactable) t).getLastMsgDate()))) {
                        dateString = "Jamais contacté";
                    }
                    else if (formatterDay.format(new Date()).equals(formatterDay.format(((Contactable) t).getLastMsgDate()))) {
                        dateString = "Contacté à " + formatDate(dateLastMsg);
                    }
                    else {
                        dateString = "Contacté le " + formatDate(dateLastMsg);
                    }
                    dateTextView.setText(dateString);
                } catch (NullPointerException e) {
                }
            }
        }
        if (t instanceof Integer) {
            TextView text = convertView.findViewById(R.id.text);
            text.setText((MainActivity.cm.getObjectsList().get((Integer) (t))).getText());
        }
        if (t instanceof Message) {
            TextView sms_destinataires = convertView.findViewById(R.id.sms_destinataires);
            TextView sms_date = convertView.findViewById(R.id.sms_date);
            TextView sms_content = convertView.findViewById(R.id.sms_content);

            Message message = MainActivity.mm.getObjectsList().get(position);

            ArrayList<Contactable> messageDestinataires = message.getDestinataires();
            Integer nbDestinataires = messageDestinataires.size();
            Contactable firstDestinataire = messageDestinataires.get(0);
            if (firstDestinataire instanceof Group) {
                String display = "Groupe : ";
                display += ((Group) firstDestinataire).getName();
                if (nbDestinataires > 1) {
                    display += ", et ";
                    display += nbDestinataires-1;
                    display += " autres";
                }
                sms_destinataires.setText(display);
            }
            else if (firstDestinataire instanceof Contact) {
                String display = "";
                display += ((Contact) firstDestinataire).getName();
                if (nbDestinataires > 1) {
                    display += ", et ";
                    display += nbDestinataires-1;
                    display += " autres";
                }
                sms_destinataires.setText(display);
            }


            Date messageDate = message.getDate();
            sms_date.setText(formatDate(messageDate));

            String sms = message.getMessage();
            sms = sms.replace('\n', ' ');
            sms_content.setText(sms);
        }


        return convertView;
    }

    private String formatDate (Date date) {
        SimpleDateFormat formatterDay = new SimpleDateFormat("dd/MM/yy");
        if (formatterDay.format(new Date()).equals(formatterDay.format(date))) {
            SimpleDateFormat formatterHour = new SimpleDateFormat("HH:mm");
            return formatterHour.format(date);
        }
        else {
            return formatterDay.format(date);
        }
    }

}