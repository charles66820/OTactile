package fr.magicorp.OTactile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductsAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Product> products;
    final SharedPreferences pref;

    public ProductsAdapter(Context context, ArrayList<Product>  products) {
        this.mContext = context;
        this.products = products;
        this.pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = products.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.products_grid_item, null);
        }

        final ImageView picture = (ImageView)convertView.findViewById(R.id.product_picture);
        final RatingBar stars = (RatingBar)convertView.findViewById(R.id.stars);
        final TextView title = (TextView)convertView.findViewById(R.id.product_title);
        final TextView price = (TextView)convertView.findViewById(R.id.product_price);

        StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build()); // show error message and debug for api 25 and below

        Picasso.get()
                .load(pref.getString(
                        "img_server_host",
                        convertView.getResources().getString(R.string.pref_default_img_server_host)
                        ) + "/products/" + product.getMainPicture()
                )
                .error(R.drawable.default_product_img)
                .into(picture);

        stars.setRating(product.getOpinionsAvg());
        title.setText(product.getTitle());
        price.setText(NumberFormat.getInstance(Locale.getDefault()).format(product.getPriceTTC())+"€");

        return convertView;
    }
}