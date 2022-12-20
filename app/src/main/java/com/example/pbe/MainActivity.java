package com.example.pbe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


//para hacer peticiones http primero he importado la libreria volley https://google.github.io/volley/
//la he habilitado en build.grandle Module:... -> implementation 'com.android.volley:volley:1.2.1'
//despues en Android manifest he dado permisos para poder acceder a internet con el comando
//<uses-permission android:name="android.permission.INTERNET"></uses-permission>

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.Principal;

public class MainActivity extends AppCompatActivity {


    EditText username, host, password;
    Button buttonlogin;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rq = Volley.newRequestQueue(this);
        username = findViewById(R.id.user);
        host = findViewById(R.id.host);
        password = findViewById(R.id.password);
        buttonlogin = findViewById(R.id.button);

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peticionUsuario(username.getText().toString(),host.getText().toString(), password.getText().toString());
            }
        });
    }

    public void peticionUsuario(String usuario,String hostyport, String Password) {
        String url = "http://" + hostyport + "/" + "students";
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray mJsonArray = response.getJSONArray("contents"); //nombre de la lista
                            if(mJsonArray.length() != 0) {
                                boolean UsuarioExiste = false;
                                boolean ContraseñaCorrecta = false;
                                for (int i = 0; i < mJsonArray.length(); i++) {
                                    JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                    String comparar = mJsonObject.getString("name");
                                    if (usuario.equals(comparar)) {
                                        UsuarioExiste = true;
                                        String compararContraseña = mJsonObject.getString("password");
                                        if (Password.equals(compararContraseña)) {
                                            ContraseñaCorrecta = true;
                                        }
                                    }
                                }
                                if (UsuarioExiste && ContraseñaCorrecta) {
                                    //aqui cambio a la siguiente ventana
                                    Intent intent = new Intent(MainActivity.this, Calendario.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Credenciales corectas", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(!UsuarioExiste){
                                        Toast.makeText(MainActivity.this, "El usuario es incorrecto", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "La constraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                System.out.println(mJsonArray.length());
                            } else {
                                Toast.makeText(MainActivity.this, "PUERTO INCORRECTO", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println(error);
                    }
                });
        rq.add(request);
    }
}