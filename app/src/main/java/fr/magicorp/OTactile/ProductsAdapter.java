package fr.magicorp.OTactile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductsAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Product> products;

    public ProductsAdapter(Context context, ArrayList<Product>  products) {
        this.mContext = context;
        this.products = products;
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
            convertView = layoutInflater.inflate(R.layout.product_grid_item, null);
        }

        final ImageView picture = (ImageView)convertView.findViewById(R.id.product_picture);
        final RatingBar stars = (RatingBar)convertView.findViewById(R.id.stars);
        final TextView title = (TextView)convertView.findViewById(R.id.product_title);
        final TextView price = (TextView)convertView.findViewById(R.id.product_price);

        StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build()); // show error message and debug for api 25 and below
        if (product.getMainPicture() == null) {
            picture.setImageResource(R.drawable.default_product_img);
        } else {
            try {
                URL url = new URL("http://ppe3.net/img/products/"+product.getMainPicture());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                picture.setImageBitmap(bmp);
            } catch (Exception e) {
                picture.setImageResource(R.drawable.default_product_img);
                Log.e("MainActivity",e.toString());
            }
        }
        stars.setRating(product.getOpinionsAvg());
        title.setText(product.getTitle());
        price.setText(NumberFormat.getInstance(Locale.getDefault()).format(product.getPriceTTC())+"€");

        return convertView;
    }
}