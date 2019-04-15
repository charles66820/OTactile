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

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ProductPictureAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<HashMap<String, String>> pictures;
    final SharedPreferences pref;

    public ProductPictureAdapter(Context context, ArrayList<HashMap<String, String>>  pictures) {
        this.mContext = context;
        this.pictures = pictures;
        this.pref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public int getCount() {
        return pictures.size();
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

        final HashMap<String, String> pic = pictures.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.product_pictures_grid_item, null);
        }

        final ImageView picture = (ImageView)convertView.findViewById(R.id.picture);

        StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build()); // show error message and debug for api 25 and below
        if (pic.get("fileName") == null) {
            picture.setImageResource(R.drawable.default_product_img);
        } else {
            try {
                URL url = new URL(pref.getString("imp_server_host","") + "/products/"+pic.get("fileName"));
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                picture.setImageBitmap(bmp);
            } catch (Exception e) {
                picture.setImageResource(R.drawable.default_product_img);
                Log.e("ProductPictureAdapter",e.toString());
            }
        }

        return convertView;
    }
}