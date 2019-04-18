package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
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

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private ProductPictureAdapter adapter;
    private SharedPreferences pref;
    private Customer customer;
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
        setContentView(R.layout.activity_profile);

        tableLayout = (TableLayout)findViewById(R.id.addressesTable);
        initData();
    }

    private void initData() {

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // get customer info
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/customer/"
                + getIntent().getExtras().getString("login");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            customer = new Customer(
                                    response.getInt("id"),
                                    response.getString("login"),
                                    response.getString("email"),
                                    response.isNull("lastName")?null:response.getString("lastName"),
                                    response.isNull("firstName")?null:response.getString("firstName"),
                                    response.isNull("phoneNumber")?null:response.getString("phoneNumber"),
                                    response.getString("avatar"),
                                    response.getString("createdDate")
                                    );

                            TextView login = (TextView) findViewById(R.id.login);
                            TextView firstName = (TextView) findViewById(R.id.firstName);
                            TextView lastName = (TextView) findViewById(R.id.lastName);
                            TextView email = (TextView) findViewById(R.id.email);
                            TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
                            ImageView avatar = (ImageView) findViewById(R.id.profilePicture);
                            TextView createdDate = (TextView) findViewById(R.id.createdDate);

                            login.setText(customer.getLogin());
                            firstName.setText(customer.getFirstName());
                            lastName.setText(customer.getLastName());
                            email.setText(customer.getEmail());
                            phoneNumber.setText(customer.getPhoneNumber());
                            createdDate.setText(customer.getCreatedDate());

                            Picasso.get()
                                    .load(pref.getString(
                                            "img_server_host",
                                            getResources().getString(R.string.pref_default_img_server_host)
                                            ) + "/clients/" + customer.getAvatar()
                                    )
                                    .error(R.drawable.default_product_img)
                                    .into(avatar);
                            initTableData();
                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Http connexion error: " + error.getMessage(),
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

    private void initTableData() {
        // get customer addresses
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/customer/"
                + customer.getId() + "/addresses";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            JSONArray addresses = response.getJSONArray("addresses");
                            for (int i=0;i<addresses.length();i++){
                                JSONObject a = addresses.getJSONObject(i);

                                View tableRow = LayoutInflater.from(getApplicationContext()).inflate(R.layout.address_table_item,null,false);
                                TextView way  = (TextView) tableRow.findViewById(R.id.way);
                                TextView complement  = (TextView) tableRow.findViewById(R.id.complement);
                                TextView postalCode  = (TextView) tableRow.findViewById(R.id.postalCode);
                                TextView city  = (TextView) tableRow.findViewById(R.id.city);

                                way.setText(a.getString("way"));
                                complement.setText(a.isNull("complement")?null:a.getString("complement"));
                                postalCode.setText(a.getString("postalCode"));
                                city.setText(a.getString("city"));
                                tableLayout.addView(tableRow);
                            }

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

    public void showOrders(View view) {
        Intent intent = new Intent(this, OrdersActivity.class); // instantiate Intent with an new activity
        Bundle b = new Bundle();
        b.putInt("id", customer.getId());
        intent.putExtras(b);
        startActivity(intent);
    }
}
