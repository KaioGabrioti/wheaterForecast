package br.com.usjt_ads3anmca_weatherforecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView weatherRecyclerView;
    private WeatherAdapter adapter;
    private List<Weather> previsoes;
    private EditText locationEditText;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestQueue = Volley.newRequestQueue(this);
        locationEditText = findViewById(R.id.locationEditText);

        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        previsoes = new ArrayList<>();
        adapter = new WeatherAdapter(previsoes, this);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cidade = locationEditText.getEditableText().toString();
                String endereco =
                        getString(
                                R.string.web_service_url,
                                cidade,
                                getString(
                                        R.string.api_key
                                )
                        );
                obtemPrevisoesV5(endereco);
            }
        });
    }

    public void obtemPrevisoesV5 (String endereco){
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                endereco,
                null,
                (response) ->{
                    lidaComJSON(response);
                },
                (error) ->{
                    Toast.makeText(
                            this,
                            getString(R.string.read_error),
                            Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(req);
    }

    public void lidaComJSON (JSONObject json){
        lidaComJSON(json.toString());
    }

    public void lidaComJSON (String resultado){
        try {
            previsoes.clear();
            JSONObject json = new JSONObject(resultado);
            JSONArray list = json.getJSONArray("list");
            for (int i = 0; i < list.length(); i++){
                JSONObject previsaoDaVez = list.getJSONObject(i);
                long dt = previsaoDaVez.getLong("dt");
                JSONObject main = previsaoDaVez.getJSONObject("main");
                double temp_min =
                        main.getDouble("temp_min");
                double temp_max =
                        main.getDouble("temp_max");
                double humidity =
                        main.getDouble("humidity");
                double lat =
                        main.getDouble("lat");
                double lon =
                        main.getDouble("lon");
                JSONArray weather =
                        previsaoDaVez.getJSONArray("weather");
                JSONObject unico =
                        weather.getJSONObject(0);
                String description =
                        unico.getString("description");
                String icon =
                        unico.getString("icon");
                Weather w =
                        new Weather(
                                dt,
                                temp_min,
                                temp_max,
                                humidity,
                                description,
                                icon,
                                lat,
                                lon
                        );
                previsoes.add(w);
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Toast.makeText(
                    this,
                    getString(R.string.read_error),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void obtemPrevisoesV1(String endereco) {
        try {
            URL url = new URL(endereco);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String linha = null;
            StringBuilder resultado = new StringBuilder("");
            while ((linha = reader.readLine()) != null) {
                resultado.append(linha);
            }
            String json = resultado.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void obtemPrevisoesV2(String endereco) {
        new Thread(() -> {
            try {
                URL url = new URL(endereco);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String linha = null;
                StringBuilder resultado = new StringBuilder("");
                while ((linha = reader.readLine()) != null) {
                    resultado.append(linha);
                }
                String json = resultado.toString();
                Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void obtemPrevisoesV3(String endereco) {
        new Thread(() -> {
            try {
                URL url = new URL(endereco);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String linha = null;
                StringBuilder resultado = new StringBuilder("");
                while ((linha = reader.readLine()) != null) {
                    resultado.append(linha);
                }
                String json = resultado.toString();
                runOnUiThread(() -> {
                    Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
                    lidaComJSON(json);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    class ObtemPrevisoes extends AsyncTask <String, Void, String>{
        @Override
        protected String doInBackground(String... enderecos) {
            try {
                URL url = new URL(enderecos[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String linha = null;
                StringBuilder resultado = new StringBuilder("");
                while ((linha = reader.readLine()) != null) {
                    resultado.append(linha);
                }
                String json = resultado.toString();
                return json;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            lidaComJSON(s);
        }
    }


    public void obtemPrevisoesV4 (String endereco){
        new ObtemPrevisoes().execute(endereco);
    }


}