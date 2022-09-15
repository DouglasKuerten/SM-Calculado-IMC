package com.dgk.calculadoraimc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText inputPeso, inputAltura;
    Button botaoCalcular;
    TextView resultado;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences("com.dgk.calculadoraimc", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText inputPeso = findViewById(R.id.inputPeso);
        EditText inputAltura = findViewById(R.id.inputAltura);
        TextView resultado = findViewById(R.id.textResultado);
        Button botaoCalcular = findViewById(R.id.botaoCalcular);

        inputPeso.setText(sharedPref.getString("INPUT-PESO", null));
        inputAltura.setText(sharedPref.getString("INPUT-ALTURA", null));
        resultado.setText(sharedPref.getString("IMC-RESULTADO", null));

        botaoCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputPeso.getText().toString().trim().isEmpty() && !inputAltura.getText().toString().trim().isEmpty()) {
                    DecimalFormat df = new DecimalFormat("##.##");
                    Double altura = 0.0;
                    if (!inputAltura.getText().toString().contains(".")) {
                        altura = Double.parseDouble(inputAltura.getText().toString()) / 100;
                    } else {
                        altura = Double.parseDouble(inputAltura.getText().toString());
                    }
                    Double imc = Double.parseDouble(inputPeso.getText().toString()) / (altura * 2);
                    String imcFormatado = df.format(imc);
                    String textoResultado = "O seu IMC é\n" + imcFormatado;
                    if (imc < 18.5) {
                        textoResultado = textoResultado + "\nAbaixo do Peso";
                    } else if (imc >= 18.5 && imc <= 24.9) {
                        textoResultado = textoResultado + "\nPeso Normal";
                    } else if (imc >= 25.0 && imc <= 29.9) {
                        textoResultado = textoResultado + "\nSobrepeso";
                    } else if (imc >= 30.0 && imc <= 34.9) {
                        textoResultado = textoResultado + "\nObesidade Grau I";
                    } else if (imc >= 35.0 && imc <= 39.9) {
                        textoResultado = textoResultado + "\nObesidade Grau II";
                    } else if (imc >= 40) {
                        textoResultado = textoResultado + "\nObesidade Grau III";
                    }
                    inputAltura.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    inputPeso.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    resultado.setText(textoResultado);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("INPUT-PESO", inputPeso.getText().toString());
                    editor.putString("INPUT-ALTURA", inputAltura.getText().toString());
                    editor.putString("IMC-RESULTADO", textoResultado);
                    editor.commit();
                }
            }
        });
    }
}