package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {
    private ProductPictureAdapter adapter;
    private ArrayList<HashMap<String, String>> picturesList;
    private SharedPreferences pref;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
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

        // product pictures list
        picture = findViewById(R.id.product_picture);

        picturesList = new ArrayList<>();
        adapter = new ProductPictureAdapter(getBaseContext(), picturesList);
        GridView gv = findViewById(R.id.product_pictures);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                HashMap<String, String> pic = picturesList.get(position);
                Picasso.get()
                        .load(pref.getString(
                                "img_server_host",
                                getResources().getString(R.string.pref_default_img_server_host)
                                ) + "/products/" + pic.get("fileName")
                        )
                        .error(R.drawable.default_product_img)
                        .into(picture);
            }
        });

        initData();
    }

    private void initData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = pref.getString("api_server_host", getResources().getString(R.string.pref_default_api_server_host)) + "/products/"
                +Integer.toString(getIntent().getExtras().getInt("productId"));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            TextView title = findViewById(R.id.product_title);
                            TextView price = findViewById(R.id.product_price);
                            TextView ref = findViewById(R.id.product_reference);
                            TextView quantity = findViewById(R.id.product_quantity);
                            RatingBar stars = findViewById(R.id.product_stars);
                            TextView description = findViewById(R.id.product_description);

                            title.setText(response.getString("title"));
                            price.setText(NumberFormat.getInstance(Locale.getDefault()).format(response.getDouble("priceTTC"))+"€");
                            ref.setText(response.getString("reference"));
                            quantity.setText(response.getString("quantity"));
                            stars.setRating((float)response.getDouble("opinionAVG"));
                            description.setText(response.getString("description"));

                            JSONArray pictures = response.getJSONArray("pictures");

                            if (pictures.length() <= 0) {
                                picture.setImageResource(R.drawable.default_product_img);
                            } else {
                                Picasso.get()
                                        .load(pref.getString(
                                                "img_server_host",
                                                getResources().getString(R.string.pref_default_img_server_host)
                                                ) + "/products/" + pictures.getJSONObject(0).getString("fileName")
                                        )
                                        .error(R.drawable.default_product_img)
                                        .into(picture);
                            }

                            for (int i=0; i < pictures.length(); i++) {
                                HashMap<String, String> pic = new HashMap<>();
                                pic.put("fileName", pictures.getJSONObject(i).getString("fileName"));
                                picturesList.add(pic);
                            }

                            adapter.notifyDataSetChanged();

                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.error_server,
                                    Toast.LENGTH_LONG).show();
                            //Log.e("ProductActivity",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_network_connexion,
                                Toast.LENGTH_LONG).show();
                        //Log.e("ProductActivity",error.getMessage());
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
