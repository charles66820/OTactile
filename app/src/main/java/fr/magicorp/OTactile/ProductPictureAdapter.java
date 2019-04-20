package fr.magicorp.OTactile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductPictureAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<HashMap<String, String>> pictures;
    private final SharedPreferences pref;

    ProductPictureAdapter(Context context, ArrayList<HashMap<String, String>> pictures) {
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

        final ImageView picture = convertView.findViewById(R.id.picture);

        StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build()); // show error message and debug for api 25 and below

        Picasso.get()
                .load(pref.getString(
                        "img_server_host",
                        convertView.getResources().getString(R.string.pref_default_img_server_host)
                        ) + "/products/" + pic.get("fileName")
                )
                .error(R.drawable.default_product_img)
                .into(picture);
        return convertView;
    }
}