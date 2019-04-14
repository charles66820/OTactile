package fr.magicorp.OTactile;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

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
        final TextView title = (TextView)convertView.findViewById(R.id.product_title);
        final TextView price = (TextView)convertView.findViewById(R.id.product_price);
//"http://ppe3.net/img/products/"+product.getMainPicture()
//        picture.setImageURI();
        title.setText(product.getTitle());
        price.setText(Double.toString(product.getPriceTTC()));

        return convertView;
    }
}