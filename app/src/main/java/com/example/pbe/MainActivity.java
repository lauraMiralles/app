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
    boolean UsuarioExiste;
    boolean ContraseñaCorrecta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rq = Volley.newRequestQueue(this);

        username = findViewById(R.id.user);
        host = findViewById(R.id.host);
        password = findViewById(R.id.password);
        buttonlogin = findViewById(R.id.button);
        boolean existe = false;

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(username.getText().toString().equals("a") && username.getText().toString().equals("a") && host.getText().toString().equals("a")){
                if(username.getText().toString().equals("a")){
                    //aqui cambio a la siguiente ventana
                    Intent intent = new Intent(MainActivity.this,Calendario.class );
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "Credenciales corectas", Toast.LENGTH_SHORT).show();
                }else{
                    //si no existe ese usuario salta un error
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
            /*
            public void onClick(View view) {
                //if(username.getText().toString().equals("a") && username.getText().toString().equals("a") && host.getText().toString().equals("a")){
                public boolean peticionUsuario(username.getText().toString(),host.getText().toString(),password.getText().toString())
                if(UsuarioExiste==true&&ContraseñaCorrecta==true){
                    //aqui cambio a la siguiente ventana
                    Intent intent = new Intent(MainActivity.this,Calendario.class );
                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "Credenciales corectas", Toast.LENGTH_SHORT).show();
                }else{
                    //si no existe ese usuario salta un error
                    if(UsuarioExiste==false){
                        Toast.makeText(MainActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            */
        });



    }


    public void peticionUsuario(String usuario,String host,String port, String Password) {
        String url = "http://" + host + ":" + port + "/" + "user";
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray mJsonArray = response.getJSONArray("contents"); //nombre de la lista
                            for(int i =0; i<mJsonArray.length();i++){
                                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                                String comparar = mJsonObject.getString("id");
                                if(usuario.equals(comparar)){
                                    UsuarioExiste = true;
                                    String compararContraseña = mJsonObject.getString("id");
                                    if(password.equals(compararContraseña)){
                                        ContraseñaCorrecta = true;
                                    }

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                });
        rq.add(request);
    }
}