package fr.magicorp.OTactile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import fr.magicorp.OTactile.entity.Product;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private ArrayList<Product> products;
    private RequestQueue queue;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_products, container, false);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(requireActivity());

        // init list
        products = new ArrayList<>();
        GridView gv = v.findViewById(R.id.list);

        // set adapter
        final ProductsAdapter adapter = new ProductsAdapter(getActivity(), products);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Product product = products.get(position);

                Intent intent = new Intent(getActivity(), ProductActivity.class);
                Bundle b = new Bundle();
                b.putInt("productId", product.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        adapter.notifyDataSetChanged();

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(getActivity());
        String url = pref.getString("api_server_host", getResources().getString(R.string.pref_default_api_server_host)) + "/products";

        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        try {

                            // navigate in json
                            JSONArray ps = response.getJSONArray("products");
                            for (int i = 0; i < ps.length(); i++) {
                                JSONObject p = ps.getJSONObject(i);

                                Product product = new Product(
                                        p.getInt("id"),
                                        p.getString("title"),
                                        p.getDouble("priceTTC"),
                                        p.getInt("quantity"),
                                        (p.isNull("mainPicture"))?null:p.getString("mainPicture"),
                                        (float)p.getDouble("opinionAVG"));

                                products.add(product);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (final JSONException e) {
                            Toast.makeText(getActivity(),
                                    R.string.error_server,
                                    Toast.LENGTH_LONG).show();
                            Log.e("ProductsFragment",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),
                                R.string.error_network_connexion,
                                Toast.LENGTH_LONG).show();
                        Log.e("ProductsFragment",error.getMessage());
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        jsonObjectRequest.cancel();
        queue.stop();
        super.onDestroyView();
    }
}
