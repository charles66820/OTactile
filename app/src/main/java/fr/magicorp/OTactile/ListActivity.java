package fr.magicorp.OTactile;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ListActivity extends AppCompatActivity {
    ListView list;
    ArrayList<HashMap<String, String>> listsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = findViewById(R.id.list);
        listsItems = new ArrayList<>();
        initData();
    }

    private void initData() {

        for (int i = 0; i < 12; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("id",  Integer.toString((int)Math.random()*10000)+1);
            item.put("name",  "toto"+i);
            item.put("description",  "une desciption que toto "+i+" a écrit");

            listsItems.add(item);
        }

        ListAdapter adapter = new SimpleAdapter(this, listsItems,
                R.layout.list_item, new String[]{ "id","name", "description"},
                new int[]{R.id.id, R.id.name, R.id.description});
        list.setAdapter(adapter);
    }
}
