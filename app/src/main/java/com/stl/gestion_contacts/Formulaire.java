package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.InvalidPropertiesFormatException;

public class Formulaire extends AppCompatActivity {

    private EditText editNom;
    private EditText editPrenom;
    private EditText editNumTel;
    private EditText editMail;

    private String numTel = "";
    private String mail = "";
    private String nom = "";
    private String prenom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire);

        editNom = findViewById(R.id.nom);
        editPrenom = findViewById(R.id.prenom);
        editNumTel = findViewById(R.id.num_tel);
        editMail = findViewById(R.id.mail);

        editNom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editPrenom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editNumTel.setInputType(InputType.TYPE_CLASS_PHONE);
        editMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        editNumTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    // L'utilisateur sort du EditText
                    numTel = editNumTel.getText().toString().trim();
                    if (numTel.length() > 0) {
                        String formatedNumTel = formatNumTel(numTel);
                        if (formatedNumTel.equals("")) {
                            editNumTel.setError("Le numéro doit être \n" +
                                    "- un numéro de téléphone portable\n" +
                                    "- un numéro français");
                        }
                        else {
                            editNumTel.setText(formatedNumTel);
                            numTel = formatedNumTel;
                        }
                    }
                }
            }
        });

        editMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    mail = editMail.getText().toString().trim();
                    if (mail.length() > 0 && (!isValidMail(mail))) {
                            editMail.setError("Adresse mail invalide.");

                    }
                }
            }
        });

        editNom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    nom = editNom.getText().toString().trim();
                    if (! nom.equals("")) {
                        editNom.setError(null);
                        editPrenom.setError(null);
                        nom = nom.substring(0,1).toUpperCase() + nom.substring(1);
                        editNom.setText(nom);
                    }
                }
            }
        });

        editPrenom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    prenom = editPrenom.getText().toString().trim();
                    if (! prenom.equals("")) {
                        editNom.setError(null);
                        editPrenom.setError(null);
                        prenom = prenom.substring(0,1).toUpperCase() + prenom.substring(1);
                        editPrenom.setText(prenom);
                    }
                }
            }
        });


    }

    public void valid_contact (View view) {

        String nom = editNom.getText().toString().trim();
        String prenom = editPrenom.getText().toString().trim();

        boolean formulaireValide = true;

        if (nom.equals("") && prenom.equals("")) {
            formulaireValide = false;
            editNom.setError("Entrez au moins un nom ou un prénom.");
            editPrenom.setError("Entrez au moins un nom ou un prénom.");
        }
        if (numTel.equals("")) {
            formulaireValide = false;
            editNumTel.setError("Entrez le numéro de téléphone du contact.");
        }
        else if (formatNumTel(numTel).equals("")) {
            formulaireValide = false;
        }
        if (! (mail.equals("") || isValidMail(mail))) {
            formulaireValide = false;
        }

        if (formulaireValide) {
            Contact newContact = new Contact(nom, prenom, numTel, mail);
            MainActivity.contactManager.add_contact(newContact);
            finish();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Formulaire invalide");
            alertDialog.setMessage("Il y a des erreurs dans le formulaire. " +
                    "Veuillez vérifier les informations svp.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private String formatNumTel (String num_tel){
        // Supprimer tous les espaces
        String formatedNum = num_tel.replaceAll("\\s", "");
        // Formater le numéro
        if (formatedNum.length() < 10) {
            return "";
        }
        if (formatedNum.substring(0,3).equals("+33")) {
            formatedNum = formatedNum.substring(3);
            formatedNum = "0" + formatedNum;
            return formatNumTel(formatedNum);
        }
        else {
            if (formatedNum.charAt(0) == '0' &&
                (formatedNum.charAt(1) == '6' || formatedNum.charAt(1) == '7')) {
                if (formatedNum.length() == 10) {
                    String num = "";
                    for (int i = 0; i < 8; i+=2) {
                        num = num + formatedNum.substring(i,i+2) + " ";
                    }
                    num = num + formatedNum.substring(8,10);
                    return num;
                }
            }
        }
        return "";
    }
}
