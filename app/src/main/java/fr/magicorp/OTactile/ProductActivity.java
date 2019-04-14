package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {
// Toast.makeText(getActivity(), "Product id : "+product.getId(), Toast.LENGTH_LONG).show();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getBoolean("night_mode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = pref.getString("api_server_host","") + "/products/"
                +Integer.toString(getIntent().getExtras().getInt("productId"));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            TextView title = (TextView) findViewById(R.id.product_title);
                            TextView price = (TextView) findViewById(R.id.product_price);
                            TextView ref = (TextView) findViewById(R.id.product_reference);
                            TextView quantity = (TextView) findViewById(R.id.product_quantity);
                            RatingBar stars = (RatingBar) findViewById(R.id.product_stars);
                            TextView description = (TextView) findViewById(R.id.product_description);
                            final ImageView picture = (ImageView) findViewById(R.id.product_picture);

                            title.setText(response.getString("title"));
                            price.setText(NumberFormat.getInstance(Locale.getDefault()).format(response.getDouble("priceTTC"))+"€");
                            ref.setText(response.getString("reference"));
                            quantity.setText(response.getString("quantity"));
                            stars.setRating((float)response.getDouble("opinionAVG"));
                            description.setText(response.getString("description"));
                            picture.setImageResource(R.drawable.default_product_img);// (response.isNull("mainPicture"))?null:response.getString("mainPicture"),

                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Http connexion error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
