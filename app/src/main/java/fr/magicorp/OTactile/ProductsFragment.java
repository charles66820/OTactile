package fr.magicorp.OTactile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class ProductsFragment extends Fragment {

    ArrayList<Product> products;
    private GridView gv;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_products, container, false);

        // init list
        products = new ArrayList<Product>();
        gv = (GridView) v.findViewById(R.id.list);

        // set adapter
        final ProductsAdapter adapter = new ProductsAdapter(getContext(), products);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Product product = products.get(position);

                Toast.makeText(getActivity(), "Product id : "+product.getId(), Toast.LENGTH_LONG).show();

                adapter.notifyDataSetChanged();
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
//        String url ="http://api.magicorp.fr/batrenis/v1/servers";
        String url ="http://api.ppe3.net/products";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
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
                                        p.getString("mainPicture"),
                                        p.getString("opinionAVG"));

                                products.add(product);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (final JSONException e) {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),
                                "Http connexion error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        try {
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return v;
    }
}
