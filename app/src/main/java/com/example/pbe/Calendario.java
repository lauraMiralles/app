package com.example.pbe;

import static java.util.logging.Level.parse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableLayout;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    Button button;
    private RequestQueue rq;
    JSONArray jsonArray;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        tl = (TableLayout) findViewById(R.id.tableLayout);
        et = (EditText) findViewById(R.id.et);
        button = (Button) findViewById(R.id.button);
        rq = Volley.newRequestQueue(this);
        jsonArray = null;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et.getText().toString();
                peticionUsuario(input);

                switch (input) {
                    case "tasks": {
                        deleteTable();
                        List<Map<String, Object>> lista = null;
                        try {
                            //lista = jsonToList(loadJSONFromAsset("taula.json"));
                            lista = jsonArrayToList(jsonArray);
                            System.out.println(loadJSONFromAsset("taula.json"));
                            int cols = lista.get(1).keySet().size();
                            int filas = lista.size();
                            createTable(filas, cols, lista);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "timetables": {
                        deleteTable();
                        List<Map<String, Object>> lista = null;
                        try {
                            //lista = jsonToList(loadJSONFromAsset("taula2.json"));
                            lista = jsonArrayToList(jsonArray);
                            int cols = lista.get(1).keySet().size();
                            int filas = lista.size();
                            createTable(filas, cols, lista);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });
    }

    public void peticionUsuario(String tipoPeticion)
    {
        String Port = "localhost";
        String Host = "8080";
        String url = "http://" + Port + ":" + Host + "/" + tipoPeticion;
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray mJsonArray = response.getJSONArray("contents"); //nombre de la lista
                            jsonArray = mJsonArray;
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



    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public List<Map<String, Object>> jsonArrayToList (JSONArray jsonArray) throws JSONException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("array", jsonArray);
        String json = jsonObject.toString();
        List<Map<String, Object>> jsonList = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        return jsonList;
    }

    public List<Map<String, Object>> jsonToList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> myObjects = mapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        return myObjects;
    }

    public void createTable(int filas, int cols, List<Map<String, Object>> list) throws JSONException, JsonProcessingException {
        Iterator keys = list.get(1).keySet().iterator();
        TableRow header = new TableRow(this);
        header.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        while (keys.hasNext()) {
            TextView tv = new TextView(this);
            tv.setText(keys.next().toString());
            header.addView(tv);
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
                fila.addView(tv);
            }
            tl.addView(fila);
        }
    }

    public void deleteTable() {
        if (tl.getChildCount() != 0) {
            tl.removeAllViews();
        }
    }

}