package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import static com.stl.gestion_contacts.MainActivity.contactComp;

public class FormulaireActivity extends AppCompatActivity {

    private EditText editNom;
    private EditText editNumTel;

    private String numTel = "";
    private String nom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNom = findViewById(R.id.nom);
        editNumTel = findViewById(R.id.num_tel);

        editNom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editNumTel.setInputType(InputType.TYPE_CLASS_PHONE);

        editNumTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    // L'utilisateur sort du EditText
                    numTel = editNumTel.getText().toString().trim();
                    if (numTel.length() > 0) {
                        String formatedNumTel = formatNumTel(numTel);
                        if (formatedNumTel.equals("")) {
                            editNumTel.setError("Le numéro doit être un numéro de téléphone portable français.");
                        }
                        else {
                            editNumTel.setText(formatedNumTel);
                            numTel = formatedNumTel;
                        }
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
                        nom = nom.substring(0,1).toUpperCase() + nom.substring(1);
                        editNom.setText(nom);
                    }
                }
            }
        });

    }

    public void valid_contact (View view) {

        String nom = editNom.getText().toString().trim();
        numTel = editNumTel.getText().toString().trim();

        boolean formulaireValide = true;

        if (nom.equals("")) {
            formulaireValide = false;
            editNom.setError("Veuillez indiquer le nom du contact.");
        }
        if (numTel.equals("")) {
            formulaireValide = false;
            editNumTel.setError("Veuillez entre le numéro de téléphone du contact");
        }
        else {
            numTel = formatNumTel(numTel);
            if (numTel.equals("")) {
                formulaireValide = false;
                editNumTel.setError("Le numéro doit être un numéro de téléphone portable français.");
            }
        }

        if (formulaireValide) {
            Contact newContact = new Contact(nom, numTel);
            MainActivity.cm.addObject(newContact);
            if (MainActivity.contactComp == ContactComparator.Alphabetic)
                MainActivity.sortContacts();
            finish();
        }
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Formulaire invalide");
            alertDialog.setMessage("Il y a des erreurs dans le activity_formulaire. " +
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

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
