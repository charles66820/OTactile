package fr.magicorp.OTactile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView login = (TextView) v.findViewById(R.id.login);
        Button button = (Button) v.findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("login", String.valueOf(login.getText()));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return v;
    }
}
