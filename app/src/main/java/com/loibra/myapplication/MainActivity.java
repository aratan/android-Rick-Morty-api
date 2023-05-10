package com.loibra.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnFetchData;
    private TextView txtName;
    private ImageView imageView;

    private int n = 816;
    private String cadena = "https://rickandmortyapi.com/api/character/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // objetos a variables
        btnFetchData = findViewById(R.id.btnFetchData);
        txtName = findViewById(R.id.txtName);
        imageView = findViewById(R.id.imageView);

        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchDataAsyncTask fetchDataAsyncTask = new FetchDataAsyncTask();
                n++;

                fetchDataAsyncTask.execute(cadena + n);
            }
        });
    }

    private class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    result += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResult = new JSONObject(result);

                // sacamos el nombre
                String name = jsonResult.getString("name");
                txtName.setText(name);

                // ... etc ...

                // añadimos la imagen
                String imagen = jsonResult.getString("image");
                Picasso.get().load(imagen).into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
