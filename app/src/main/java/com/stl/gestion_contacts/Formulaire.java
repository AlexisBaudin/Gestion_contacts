package com.stl.gestion_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Formulaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulaire);
    }

    public void valid_contact (View view) {

        EditText editNom = findViewById(R.id.nom);
        EditText editPrenom = findViewById(R.id.prenom);
        EditText editNumTel = findViewById(R.id.num_tel);
        EditText editMail = findViewById(R.id.mail);

        String nom = editNom.getText().toString();
        String prenom = editPrenom.getText().toString();
        String num_tel = editNumTel.getText().toString();
        String mail = editMail.getText().toString();

        Contact new_contact = new Contact(nom, prenom, num_tel, mail);

        MainActivity.add_contact(new_contact);
        finish();
    }

}
