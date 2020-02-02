package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FormulaireActivity extends AppCompatActivity {

    private EditText editNom;
    private EditText editNumTel;

    private String numTel = "";
    private String nom = "";

    int indexContact;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editNom = findViewById(R.id.nom);
        editNom.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MainActivity.MAX_NAME_CONTACT_LENGTH)});

        editNumTel = findViewById(R.id.num_tel);

        editNom.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editNumTel.setInputType(InputType.TYPE_CLASS_PHONE);

        indexContact = getIntent().getIntExtra("CONTACT_POSITION", -1);
        if (indexContact != -1) {
            contact = MainActivity.cm.getObjectsList().get(indexContact);
            editNom.setText(contact.getName());
            editNumTel.setText(contact.getNumTel());
        }

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
            if (indexContact != -1) {
                MainActivity.cm.removeObject(indexContact);
            }
            Contact newContact = new Contact(nom, numTel);
            MainActivity.cm.addObject(newContact);
            if (MainActivity.contactComp == ContactComparator.Alphabetic)
                MainActivity.sortContacts();
            if (indexContact != -1) {
                Intent intent = new Intent(this, ContactActivity.class);
                int indexNewContact = MainActivity.cm.getObjectsList().indexOf(newContact);
                intent.putExtra("CONTACT_POSITION", indexNewContact);
                startActivity(intent);
            }
            finish();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Il y a des erreurs dans le formulaire. " +
                    "Veuillez vérifier les informations svp.",
                    Toast.LENGTH_SHORT);
            toast.show();
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
