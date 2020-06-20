package com.jossessoft.deletesentsmss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtContrasenia;

    private String usuarioGuardado;
    private String contraseniaGuardada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        CargarCredenciales();

        if(usuarioGuardado.equals("Caleb dylan") & contraseniaGuardada.equals("michalydylan1988")){
            Intent a = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(a);

            this.finish();

        }

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(txtUsuario.getText().toString().equals("Caleb dylan") & txtContrasenia.getText().toString().equals("michalydylan1988")){

                        Toast.makeText(getApplicationContext(),"Bienvenido " + txtUsuario.getText(),Toast.LENGTH_SHORT).show();

                        guardarCredenciales();

                        Intent a = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(a);

                    }else{
                        Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectos" + txtUsuario.getText() + " " + txtContrasenia.getText(),Toast.LENGTH_SHORT).show();

                        txtUsuario.setText("");
                        txtContrasenia.setText("");
                    }



            }
        });

    }

    private void guardarCredenciales(){

        SharedPreferences credenciales = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        String usuario = txtUsuario.getText().toString();
        String contrasenia = txtContrasenia.getText().toString();

        SharedPreferences.Editor editor = credenciales.edit();
        editor.putString("usuario",usuario);
        editor.putString("contrasenia",contrasenia);
        editor.commit();
    }

    private void CargarCredenciales(){
        SharedPreferences credenciales = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        usuarioGuardado = credenciales.getString("usuario","nada");
        contraseniaGuardada = credenciales.getString("contrasenia","nada");
    }

}