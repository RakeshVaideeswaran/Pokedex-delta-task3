package com.example.android.pokedex;

import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    String Stringrequrl;
    Handler mHandler;
    Runnable r;
    EditText namebox;
    ImageView I;
    TextView N,T,W,H;
    LinearLayout n,t,w,h;
    Button search;
    String NAME,TYPE,IMAGE;
    int HEIGHT,WEIGHT;
    StringBuilder typecomp;
    boolean var = true;
    SQLiteDatabase mDb;
    public static final int DELETEHISTORY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (Button) findViewById(R.id.searchButton);
        namebox = (EditText) findViewById(R.id.pokemonName);
        N = (TextView) findViewById(R.id.name);
        H = (TextView) findViewById(R.id.height);
        W = (TextView) findViewById(R.id.weight);
        T = (TextView) findViewById(R.id.type);
        n = (LinearLayout) findViewById(R.id.Nlay);
        t = (LinearLayout) findViewById(R.id.Tlay);
        w = (LinearLayout) findViewById(R.id.Wlay);
        h = (LinearLayout) findViewById(R.id.Hlay);
        I = (ImageView) findViewById(R.id.IMAGE);

        PokedexDbHelper helper = new PokedexDbHelper(this);
        mDb = helper.getWritableDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.historymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
        startActivityForResult(intent,DELETEHISTORY);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DELETEHISTORY && resultCode == RESULT_OK)
        {
            Toast T = Toast.makeText(this,"YOUR SEARCH HISTORY WAS CLEARED",Toast.LENGTH_LONG);
            T.setGravity(Gravity.CENTER,0,0);
            T.show();
        }

    }

    public void ExecuteAsynctask(View view)
    {
        if(namebox.getText().toString().equals(""))
            Toast.makeText(this,"Enter pokemon name",Toast.LENGTH_LONG).show();

        else
        {

            Stringrequrl = "http://pokeapi.co/api/v2/pokemon/";
            String s = namebox.getText().toString();
            Stringrequrl+=s;
            Stringrequrl+="/";
            namebox.setText("");
           // Toast.makeText(this,Stringrequrl,Toast.LENGTH_LONG).show();
            PokedexTask P = new PokedexTask();
            P.execute();
        }


    }

    public String StreamReader(InputStream inputStream) throws IOException
    {
        StringBuilder builder = new StringBuilder();

        if(inputStream!=null)
        {
            InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);

            try {
                String l = reader.readLine();

                while(l!=null)
                {
                    builder.append(l);
                    l=reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return builder.toString();
    }

    public String httpRequest(URL url) throws IOException
    {

        String jsonResponse = "";

        if(url == null)
            return jsonResponse;


        HttpURLConnection urlco = null;
        InputStream inputStream = null;


        try {
            urlco = (HttpURLConnection) url.openConnection();
            urlco.setRequestMethod("GET");
            urlco.connect();

            if (urlco.getResponseCode() == 200)
            {

                inputStream = urlco.getInputStream();
                jsonResponse = StreamReader(inputStream);
            }

            else
            {
                var = false;
            }

        } catch (IOException e) {

        } finally {
            if(urlco!=null)
                urlco.disconnect();

            if(inputStream!=null)
                inputStream.close();
        }

        return jsonResponse;
    }


    public void ExtractfromJSON(String jsonpokemon)
    {

        typecomp = new StringBuilder();

        try
        {
            JSONObject root = new JSONObject(jsonpokemon);
            JSONArray typesarray = root.getJSONArray("types");
            String temp;
            for(int i=0;i<typesarray.length();i++)
            {
               temp = typesarray.getJSONObject(i).getJSONObject("type").getString("name");
                typecomp.append(temp);

                if(i!=typesarray.length()-1)
                typecomp.append(", ");

            }
            NAME = root.getString("name");
            WEIGHT = root.getInt("weight");
            HEIGHT = root.getInt("height");
            IMAGE = root.getJSONObject("sprites").getString("front_default");
            TYPE = typecomp.toString();

            AddItemToDatabase();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddItemToDatabase()
    {

        ContentValues cv = new ContentValues();
        cv.put(PokedexContract.PokedexEntry.NAME_COLUMN,NAME);
        cv.put(PokedexContract.PokedexEntry.IMAGE_COLUMN,IMAGE);
        mDb.insert(PokedexContract.PokedexEntry.TABLENAME,null,cv);
    }


    public void ErrorToast()
    {
        Toast T = Toast.makeText(this,"REQUEST UNSUCCESSFUL",Toast.LENGTH_LONG);
        T.setGravity(Gravity.CENTER,0,0);
        T.show();
    }

    public void UpdateScreen()
    {
        t.setVisibility(View.VISIBLE);
        n.setVisibility(View.VISIBLE);
        h.setVisibility(View.VISIBLE);
        w.setVisibility(View.VISIBLE);

        Picasso.with(this).load(IMAGE).into(I);
        T.setText(TYPE.toUpperCase());
        W.setText(String.valueOf(WEIGHT));
        H.setText(String.valueOf(HEIGHT));
        N.setText(NAME.toUpperCase());

        //Toast.makeText(this,NAME + " " + HEIGHT + " " + WEIGHT + " " + TYPE ,Toast.LENGTH_LONG).show();
    }


    public class PokedexTask extends AsyncTask<URL,Void,Void>{

        @Override
        protected void onPreExecute() {
            var = true;
        }

        @Override
        protected Void doInBackground(URL... urls) {


                URL url = null;
                String jsonResponse = "";
                try {
                    url = new URL(Stringrequrl);
                } catch (MalformedURLException e) {
                }

                try {
                        jsonResponse = httpRequest(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    ExtractfromJSON(jsonResponse);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(var == true)
                UpdateScreen();

            else
                ErrorToast();
        }
    }
}
