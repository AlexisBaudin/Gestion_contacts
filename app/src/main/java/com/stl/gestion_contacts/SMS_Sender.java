package com.stl.gestion_contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.SmsManager;

public class SMS_Sender {

    private Context context;

    SMS_Sender (Context context) {
        this.context = context;
    }

    public void send_SMS(Contact contact, String message) {

        if (MainActivity.allow_sms) {
            SmsManager.getDefault().sendTextMessage(
                    contact.num_tel,
                    null,
                    message,
                    null,
                    null);

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



}
