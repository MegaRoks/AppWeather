package com.example.megar.appweather;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewLocalTime;
    private TextView mTextViewRegionName;
    private TextView mTextViewCountryName;
    private TextView mTextViewlastUpdated;
    private TextView mTextViewTemp;
    private TextView mTextViewState;
    private TextView mTextViewWind_kph;
    private ImageView imageIcon;
    private EditText EditTextCity;
    private RequestQueue mQueue;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DatabaseHelper(this);
        mTextViewLocalTime = findViewById(R.id.text_view_localTime);
        mTextViewRegionName = findViewById(R.id.text_view_regionName);
        mTextViewCountryName = findViewById(R.id.text_view_countryName);
        mTextViewlastUpdated = findViewById(R.id.text_view_last_updated);
        mTextViewTemp = findViewById(R.id.text_view_temp);
        mTextViewWind_kph = findViewById(R.id.text_view_wind_kph);
        mTextViewState = findViewById(R.id.text_view_state);
        EditTextCity = findViewById(R.id.editTextCity);
        imageIcon = findViewById(R.id.image_icon);
        Button buttonParse = findViewById(R.id.button_parse);
        mQueue = Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {
        String url = "https://api.apixu.com/v1/current.json?key=6edd1acd6a374c9a8b4114508180311&q=" + EditTextCity.getText().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject location = response.getJSONObject("location");
                            String localTime = location.getString("localtime");
                            String regionName = location.getString("region");
                            String countryName = location.getString("country");
                            mTextViewLocalTime.setText(localTime);
                            mTextViewRegionName.setText(regionName);
                            mTextViewCountryName.setText(countryName);

                            JSONObject current = response.getJSONObject("current");
                            String lastUpdated = current.getString("last_updated");
                            String temp = current.getString("temp_c");
                            String wind_kph = current.getString("wind_kph");
                            mTextViewlastUpdated.setText(lastUpdated);
                            mTextViewTemp.setText(temp);
                            mTextViewWind_kph.setText(wind_kph);

                            JSONObject condition = current.getJSONObject("condition");
                            String text = condition.getString("text");
                            String icon = condition.getString("icon");

                            Picasso.get().load("http:" + icon).resize(250, 250).into(imageIcon);

                            mTextViewState.setText(text);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}