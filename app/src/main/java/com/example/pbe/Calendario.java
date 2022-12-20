package com.example.pbe;

import static java.util.logging.Level.parse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableLayout;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Calendario extends AppCompatActivity {

    TableLayout tl;
    EditText et;
    TextView titulo;
    Button button, logout;
    private RequestQueue rq;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        tl = (TableLayout) findViewById(R.id.tableLayout);
        et = (EditText) findViewById(R.id.et);
        titulo = (TextView) findViewById(R.id.textView3);
        button = (Button) findViewById(R.id.button);
        logout = (Button) findViewById(R.id.button2);
        rq = Volley.newRequestQueue(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et.getText().toString();
                peticionUsuario(input);
                }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Calendario.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void peticionUsuario(String tipoPeticion)
    {
        String url = "http://10.0.2.2:8080/" + tipoPeticion;
        String[] partes = tipoPeticion.split("\\?");
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                deleteTable();
                                titulo.setText(partes[0]);
                                JSONArray mJsonArray = response.getJSONArray("contents"); //nombre de la lista
                                if(mJsonArray.length() != 0){
                                    List<Map<String, Object>> lista = null;
                                    lista = jsonToList(mJsonArray.toString(1));
                                    int cols = lista.get(0).keySet().size();
                                    int filas = lista.size();
                                    createTable(filas, cols, lista);
                                 } else {
                                    Toast.makeText(Calendario.this, "No existe esa tabla", Toast.LENGTH_SHORT).show();
                                 }
                        } catch (JSONException | JsonProcessingException e) {
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

    public List<Map<String, Object>> jsonToList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> myObjects = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        return myObjects;
    }

    public void createTable(int filas, int cols, List<Map<String, Object>> list) throws JSONException, JsonProcessingException {
        Iterator keys = list.get(0).keySet().iterator();
        TableRow header = new TableRow(this);
        header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        while (keys.hasNext()) {
            TextView tv = new TextView(this);
            tv.setText(keys.next().toString());
            header.addView(tv,newTableRowParams());
        }
        tl.addView(header);
        for (int i = 0; i < filas; i++) {
            Iterator values = list.get(i).values().iterator();
            TableRow fila = new TableRow(this);
            fila.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            while (values.hasNext()) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tv.setTextSize(14);
                tv.setText(values.next().toString());
                fila.addView(tv,newTableRowParams());
            }
            tl.addView(fila);
        }
    }

    public void deleteTable() {
        if (tl.getChildCount() != 0) {
            tl.removeAllViews();
            //titulo.setText("");
        }
    }

    public TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }

}