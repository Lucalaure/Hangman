package com.example.w4_p5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    // THERE IS NO HINT BUTTON IN PORTRAIT MODE, BE CAREFUL WHEN REFERENCING IT

    ArrayList<Button> btns = new ArrayList<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random random = new Random();
        String[] words = getResources().getStringArray(R.array.words);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeFragment landscape = new LandscapeFragment();
            transaction.replace(R.id.frameLayout, landscape);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            transaction.add(R.id.frameLayout, new PortraitFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void clickMe(View view) {
        Button ltr = ((Button) view);
        btns.add(ltr);
        ltr.setVisibility(View.INVISIBLE);
        ltr.setClickable(false);
        String letter = ltr.getText().toString();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeFragment landscape = (LandscapeFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
            landscape.clickMe(letter.toLowerCase(Locale.ROOT));
        } else {
            PortraitFragment portrait = (PortraitFragment) getFragmentManager().findFragmentById(R.id.frameLayout);
            portrait.clickMe(letter.toLowerCase(Locale.ROOT));
        }

    }

}