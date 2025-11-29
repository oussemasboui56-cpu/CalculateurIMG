package com.example.calculateurimg;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTaille, editTextPoids, editTextAge;
    private RadioGroup radioGroupSexe;
    private RadioButton radioFemme, radioHomme;
    private Button buttonCalculer;
    private TextView textViewIMG, textViewInterpretation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiserComposants();
        configurerEcouteurs();
    }

    private void initialiserComposants() {
        editTextTaille = findViewById(R.id.editTextTaille);
        editTextPoids = findViewById(R.id.editTextPoids);
        editTextAge = findViewById(R.id.editTextAge);
        radioGroupSexe = findViewById(R.id.radioGroupSexe);
        radioFemme = findViewById(R.id.radioFemme);
        radioHomme = findViewById(R.id.radioHomme);
        buttonCalculer = findViewById(R.id.buttonCalculer);
        textViewIMG = findViewById(R.id.textViewIMG);
        textViewInterpretation = findViewById(R.id.textViewInterpretation);

        radioFemme.setChecked(true);
    }

    private void configurerEcouteurs() {
        buttonCalculer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculerIMG();
            }
        });
    }

    private void calculerIMG() {
        String tailleStr = editTextTaille.getText().toString().trim();
        String poidsStr = editTextPoids.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        if (tailleStr.isEmpty() || poidsStr.isEmpty() || ageStr.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs");
            return;
        }

        try {
            double taille = Double.parseDouble(tailleStr);
            double poids = Double.parseDouble(poidsStr);
            int age = Integer.parseInt(ageStr);

            if (taille <= 0 || poids <= 0 || age <= 0) {
                afficherErreur("Les valeurs doivent être positives");
                return;
            }

            double tailleMetres = taille / 100;
            double imc = poids / (tailleMetres * tailleMetres);
            int sexe = radioHomme.isChecked() ? 1 : 0;

            double img = (1.20 * imc) + (0.23 * age) - (10.8 * sexe) - 5.4;
            img = Math.round(img * 10.0) / 10.0;

            afficherResultats(img, sexe);

        } catch (NumberFormatException e) {
            afficherErreur("Veuillez entrer des nombres valides");
        }
    }

    private void afficherResultats(double img, int sexe) {
        textViewIMG.setText(String.format("IMG: %.1f%%", img));

        String interpretation = getInterpretation(img, sexe);
        textViewInterpretation.setText("Interprétation: " + interpretation);

        int couleur = getCouleurInterpretation(img, sexe);
        textViewInterpretation.setBackgroundColor(couleur);
    }

    private String getInterpretation(double img, int sexe) {
        if (sexe == 1) {
            if (img < 15) return "Maigreur";
            else if (img < 20) return "Normal";
            else if (img < 25) return "Surpoids";
            else return "Obésité";
        } else {
            if (img < 25) return "Maigreur";
            else if (img < 30) return "Normal";
            else if (img < 35) return "Surpoids";
            else return "Obésité";
        }
    }

    private int getCouleurInterpretation(double img, int sexe) {
        if (sexe == 1) {
            if (img < 15) return Color.parseColor("#E3F2FD");
            else if (img < 20) return Color.parseColor("#E8F5E8");
            else if (img < 25) return Color.parseColor("#FFF3E0");
            else return Color.parseColor("#FFEBEE");
        } else {
            if (img < 25) return Color.parseColor("#E3F2FD");
            else if (img < 30) return Color.parseColor("#E8F5E8");
            else if (img < 35) return Color.parseColor("#FFF3E0");
            else return Color.parseColor("#FFEBEE");
        }
    }

    private void afficherErreur(String message) {
        textViewIMG.setText("Erreur");
        textViewInterpretation.setText(message);
        textViewInterpretation.setBackgroundColor(Color.parseColor("#FFCDD2"));
    }
}