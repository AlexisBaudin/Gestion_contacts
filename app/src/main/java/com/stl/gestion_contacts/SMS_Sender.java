package com.stl.gestion_contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class SMS_Sender {

    private Context context;

    SMS_Sender (Context context) {
        this.context = context;
    }

    public void send_SMS(String sms, ArrayList<Contact> contactDestinataires, ArrayList<Group> groupDestinataires) {

        if (MainActivity.allow_sms) {

            /** Liste sans doublon des contacts à qui envoyer le SMS */
            ArrayList<Contact> finalDestinataires = new ArrayList<Contact>();
            for (Contact c : contactDestinataires) {
                if (!finalDestinataires.contains(c)) {
                    finalDestinataires.add(c);
                    int index = MainActivity.cm.getObjectsList().indexOf(c);
                    c.setDateLastMsg(new Date());
                    MainActivity.cm.modifyObject(index, c);
                }
            }
            for (Group g : groupDestinataires) {
                ArrayList<Integer> groupContacts = MainActivity.cgm.get(g.getName()).getObjectsList();
                for (Integer i : groupContacts) {
                    Contact contact = MainActivity.cm.getObjectsList().get(i);
                    if (!finalDestinataires.contains(contact)) {
                        finalDestinataires.add(contact);
                        int index = MainActivity.cm.getObjectsList().indexOf(contact);
                        contact.setDateLastMsg(new Date());
                        MainActivity.cm.modifyObject(index, contact);
                    }
                }
                int index = MainActivity.gm.getObjectsList().indexOf(g);
                g.setLastMsgDate(new Date());
                MainActivity.gm.modifyObject(index, g);
            }

            try {
                /** Découper le message */
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(sms);


                for (Contact c : finalDestinataires) {
                    smsManager.sendMultipartTextMessage(c.getNumTel(), null, parts, null, null);
                }



                /*SmsManager.getDefault().sendTextMessage(
                        contact.num_tel,
                        null,
                        message,
                        null,
                        null);*/
                Toast.makeText(context, "SMS envoyé ! :)",
                        Toast.LENGTH_LONG).show();

                /** Sauvegarder le message */
                save_sms(sms, contactDestinataires, groupDestinataires);
            }
            catch (Exception e) {
                Toast.makeText(context, "Problème avec l'envoi du SMS...\nSVP contactez les développeurs.",
                        Toast.LENGTH_LONG).show();
            }

        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Autorisation SMS");
            alertDialog.setMessage("Veuillez autoriser l'application à envoyer des SMS.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

    private static void save_sms (String sms, ArrayList<Contact> contactList, ArrayList<Group> groupList) {
        Message message = new Message(sms, new Date(), contactList, groupList);
        MainActivity.mm.addObject(message);
    }

}
