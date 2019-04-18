package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<Order> listDataOrder;
    private HashMap<Order,List<OrderProduct>> listHashOrderProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        listView = (ExpandableListView) findViewById(R.id.orderExpList);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataOrder, listHashOrderProducts);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataOrder = new ArrayList<>();
        listHashOrderProducts = new HashMap<>();

        listDataOrder.add(new Order(22314,40,20,"2, test truc et chose pour test, 66000 Perpignan",0));
        listDataOrder.add(new Order(46545,10,10,"2, test truc et chose pour test1, 66000 Perpignan",0));
        listDataOrder.add(new Order(4298,400,5,"2, test truc et chose pour test2, 66000 Perpignan",0));
        listDataOrder.add(new Order(94956,50,42,"2, test truc et chose pour test3, 66000 Perpignan",0));

        List<OrderProduct> ltest1 = new ArrayList<>();
        ltest1.add(new OrderProduct(0, "produit 1", "dzef efzfzef e", 21.54, 62));
        ltest1.add(new OrderProduct(564, "produit 2", "dzef fere e", 245, 4545));
        ltest1.add(new OrderProduct(5465, "produit 3", "dzef ef e", 345, 4));
        ltest1.add(new OrderProduct(24, "produit 4", "dzef fe e", 5, 54));
        ltest1.add(new OrderProduct(984, "produit 5", "dzef zefze e", 53, 7));

        listHashOrderProducts.put(listDataOrder.get(0), ltest1);
        listHashOrderProducts.put(listDataOrder.get(1), ltest1);
        listHashOrderProducts.put(listDataOrder.get(2), ltest1);
        listHashOrderProducts.put(listDataOrder.get(3), ltest1);
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
