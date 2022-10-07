package com.example.w4_p5;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class PortraitFragment extends android.app.Fragment {

    private TextView guess_word;
    private ImageView hangman;
    private Button newgame;

    private String word;
    private String hint;
    private int presses = 0;
    private int lives = 6;
    ArrayList<String> letters = new ArrayList<String>();
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_portrait, container, false);

        for (Button x : ((MainActivity) getActivity()).btns) {
            Button b = (Button) v.findViewById(x.getId());
            b.setVisibility(View.INVISIBLE);
            b.setClickable(false);
        }


        Random random = new Random();

        Bundle bundle = this.getArguments();

        guess_word = (TextView) v.findViewById(R.id.word_guess2);
        hangman = (ImageView) v.findViewById(R.id.hang_image);
        newgame = (Button) v.findViewById(R.id.ng_button);

        if (bundle == null) {
            String[] words = getResources().getStringArray(R.array.words);
            String[] hints = getResources().getStringArray(R.array.hints);

            int index = random.nextInt(words.length);
            hint = hints[index];
            word = words[index];
            int wrdlen = word.length();
            for (int i = 0; i < wrdlen; i++) {
                guess_word.append("_");
            }

        } else {
            presses = bundle.getInt("presses");
            word = bundle.getString("word");
            lives = bundle.getInt("lives");
            lostLife();
            hint = bundle.getString("hint");
            String g_word = bundle.getString("guess_word");
            guess_word.setText(g_word);
            letters = bundle.getStringArrayList("letters");
            alphabet = bundle.getString("alphabet");
        }

        newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).btns.clear();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                PortraitFragment potrait = new PortraitFragment();
                transaction.replace(R.id.frameLayout, potrait);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeFragment landscape = new LandscapeFragment();

            Bundle new_bundle = new Bundle();
            new_bundle.putString("word", word);
            new_bundle.putString("hint", hint);
            new_bundle.putInt("presses", presses);
            new_bundle.putInt("lives", lives);
            new_bundle.putString("guess_word", guess_word.getText().toString());
            new_bundle.putStringArrayList("letters", letters);
            new_bundle.putString("alphabet", alphabet);
            landscape.setArguments(new_bundle);

            transaction.replace(R.id.frameLayout, landscape);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }
    }

    public void clickMe(String letter) {
        if (!letters.contains(letter)) {
            alphabet.replace(letter, "");
            letters.add(letter);
            if (word.contains(letter)) {
                int index = word.indexOf(letter);
                String o_string = guess_word.getText().toString();
                String n_string = o_string.substring(0,index) + letter + o_string.substring(index+1);
                guess_word.setText(n_string);
                while (index != -1) {
                    index = word.indexOf(letter, index+1);
                    if (index != -1) {
                        o_string = guess_word.getText().toString();
                        n_string = o_string.substring(0,index) + letter + o_string.substring(index+1);
                        guess_word.setText(n_string);
                    }
                }
            } else {
                lives--;
                lostLife();
            }

            String o_string = guess_word.getText().toString();
            if (!o_string.contains("_")) {
                gameWon();
            }
        }
    }

    public void lostLife() {
        switch(lives) {
            case 6:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.one));
                break;
            case 5:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.two));
                break;
            case 4:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.three));
                break;
            case 3:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.four));
                break;
            case 2:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.five));
                break;
            case 1:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.six));
                break;
            case 0:
                hangman.setImageDrawable(getResources().getDrawable(R.drawable.fail));
                gameLost();
                break;
        }
    }

    public void gameLost() {
        ViewGroup layout = (ViewGroup) getView().findViewById(R.id.choice_grid);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if(child instanceof Button) {
                child.setVisibility(View.INVISIBLE);
                child.setClickable(false);
            }
        }
    }

    public void gameWon() {
        hangman.setImageDrawable(getResources().getDrawable(R.drawable.win));
        gameLost();
    }
}