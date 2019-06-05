package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import fr.magicorp.OTactile.entity.Customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class statsActivity extends AppCompatActivity {

    private SharedPreferences pref;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        initData();
    }

    private void initData() {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // get customer info
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/stats/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {

                            TextView date = findViewById(R.id.date);
                            TextView nbOrders = findViewById(R.id.nbOrders);
                            TextView totalTTC = findViewById(R.id.totalTTC);
                            TextView avgCoust = findViewById(R.id.avgCoust);
                            TextView nbClients = findViewById(R.id.nbClients);
                            TextView nbClientOrdered = findViewById(R.id.nbClientOrdered);


                            nbOrders.setText((response.getInt("nbOrders"));

                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.error_server,
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_bad_authentication,
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
