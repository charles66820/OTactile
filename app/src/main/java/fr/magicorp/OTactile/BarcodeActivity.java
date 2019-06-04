package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BarcodeActivity extends AppCompatActivity {

    IntentIntegrator qrScan;
    Button btnQuantity;
    EditText productQuantity;
    SharedPreferences pref;
    Integer productId = -1;
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
        setContentView(R.layout.activity_barcode);

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        btnQuantity = findViewById(R.id.btnProductQuantity);
        productQuantity = findViewById(R.id.productQuantity);

        qrScan = new IntentIntegrator(this);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        qrScan.setPrompt(getResources().getString(R.string.barcode_product_scan_barcode));
        qrScan.setCameraId(0);
        qrScan.setOrientationLocked(false);
        qrScan.setBeepEnabled(false);
    }

    public void scanBarcode(View view){
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents().isEmpty()) {
                Toast.makeText(this, R.string.barcode_result_not_found, Toast.LENGTH_LONG).show();
            } else {
                productQuantity.setEnabled(true);
                btnQuantity.setEnabled(true);
                btnQuantity.setClickable(true);
                loadProduct(result.getContents());
            }
        }
    }

    private void loadProduct(String barcode) {

        // get customer addresses
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/products/barcode/"
                + barcode;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {
                            productId = response.getInt("id");

                            TextView title = findViewById(R.id.product_title);
                            TextView ref = findViewById(R.id.product_description);
                            ImageView picture = findViewById(R.id.product_first_picture);

                            title.setText(response.getString("title"));
                            ref.setText(response.getString("reference"));
                            productQuantity.setText(response.getString("quantity"));

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

                        } catch (final JSONException e) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.error_server,
                                    Toast.LENGTH_LONG).show();
                            //Log.e("BarcodeActivity", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                R.string.barcode_not_found,
                                Toast.LENGTH_LONG).show();
                        //Log.e("BarcodeActivity","toto"+error.getMessage());
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeQuantity(View view) {

        // get customer addresses
        String url = pref.getString(
                "api_server_host",
                getResources().getString(R.string.pref_default_api_server_host)) + "/products/"
                + productId + "/quantity";

        // PATCH parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("quantity", productQuantity.getText().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url,  new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        Toast.makeText(getApplicationContext(),
                                R.string.msg_quantity_success,
                                Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                R.string.error_network_connexion,
                                Toast.LENGTH_LONG).show();
                        //Log.e("BarcodeActivity", error.getMessage());
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
