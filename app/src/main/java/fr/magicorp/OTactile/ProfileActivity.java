package fr.magicorp.OTactile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // theme
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TableLayout tableLayout = (TableLayout)findViewById(R.id.addressesTable);

        for (int i=0;i<6;i++){
            View tableRow = LayoutInflater.from(this).inflate(R.layout.address_table_item,null,false);
            TextView history_display_no  = (TextView) tableRow.findViewById(R.id.way);
            TextView history_display_date  = (TextView) tableRow.findViewById(R.id.complement);
            TextView history_display_orderid  = (TextView) tableRow.findViewById(R.id.postalCode);
            TextView history_display_quantity  = (TextView) tableRow.findViewById(R.id.city);

            history_display_no.setText(""+(i+1));
            history_display_date.setText("2014-02-05");
            history_display_orderid.setText("S0"+(i+1));
            history_display_quantity.setText(""+(20+(i+1)));
            tableLayout.addView(tableRow);
        }
    }
}
