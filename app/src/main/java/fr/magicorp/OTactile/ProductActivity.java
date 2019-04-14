package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {
// Toast.makeText(getActivity(), "Product id : "+product.getId(), Toast.LENGTH_LONG).show();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        TextView t = (TextView) findViewById(R.id.product_title);
        t.setText(Integer.toString(getIntent().getExtras().getInt("productId")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
