package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private ExpandableListView listView;
    private SharedPreferences pref;
    private ExpandableListAdapter listAdapter;
    private List<Order> listDataOrder;
    private HashMap<Order,List<OrderProduct>> listHashOrderProducts;
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
        setContentView(R.layout.activity_orders);

        listView = (ExpandableListView) findViewById(R.id.orderExpList);
        listDataOrder = new ArrayList<>();
        listHashOrderProducts = new HashMap<>();
        listAdapter = new ExpandableListAdapter(this,listDataOrder, listHashOrderProducts);
        listView.setAdapter(listAdapter);
        initData();
    }

    private void initData() {

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // get customer orders
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/customer/"
                + String.valueOf(getIntent().getExtras().getInt("id")) + "/orders";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            JSONArray orders = response.getJSONArray("orders");
                            for (int i=0;i<orders.length();i++){
                                JSONObject o = orders.getJSONObject(i);

                                // get customer full order
                                String url = pref.getString(
                                        "api_server_host",
                                        getResources().getString(R.string.pref_default_api_server_host)) + "/orders/"
                                        + o.getInt("id");

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(final JSONObject response) {
                                                Order order;
                                                List<OrderProduct> orderedProducts;
                                                try {
                                                    // order
                                                    order = new Order(
                                                            response.getInt("id"),
                                                            response.getDouble("total"),
                                                            response.getDouble("shipping"),
                                                            response.getString("deliveryAddress"),
                                                            response.getInt("status")
                                                    );

                                                    // ordered products
                                                    orderedProducts = new ArrayList<>();
                                                    JSONArray oPs = response.getJSONArray("orderedProducts");
                                                    for (int j=0;j<oPs.length();j++){
                                                        JSONObject oP = oPs.getJSONObject(j);

                                                        orderedProducts.add(new OrderProduct(
                                                                oP.getInt("id"),
                                                                oP.getString("title"),
                                                                oP.getString("reference"),
                                                                oP.getDouble("priceTTC"),
                                                                oP.getInt("quantity")));

                                                    }
                                                    if (!orderedProducts.isEmpty()) {
                                                        listHashOrderProducts.put(order, orderedProducts);
                                                    }

                                                    listDataOrder.add(order);
                                                    listAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
