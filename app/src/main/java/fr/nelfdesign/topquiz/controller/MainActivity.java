package fr.nelfdesign.topquiz.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import fr.nelfdesign.topquiz.R;
import fr.nelfdesign.topquiz.model.HighScore;
import fr.nelfdesign.topquiz.model.User;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText edit;
    private Button btn, btn_score;
    private User mUser;
    private HighScore mHighScore;

    private SharedPreferences mSharedPreferences;

    public static final int GAME_REQUEST_CODE = 0;
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";
    public static final String PREF_KEY_SCORETAB = "PREF_KEY_SCORETAB";
    public static final String BUNDLE_SCORES = "BUNDLE_SCORES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv);
        edit = findViewById(R.id.edit);
        btn = findViewById(R.id.btn);
        btn_score = findViewById(R.id.btn_score);

        btn.setEnabled(false);

        mSharedPreferences = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(PREF_KEY_SCORETAB,null);
        if (json != null) {
            mHighScore = gson.fromJson(json,HighScore.class);
        } else {
            mHighScore = new HighScore();
            btn_score.setEnabled(false);
        }
        out.println("Tableau des scores dans les préférences");
        for (User user : mHighScore.getScoresTab()){
            out.println(user.getFirstname()+" "+user.getScore());
        }

        // On crée un nouvel objet User qui contiendra
        // les informations concernant le précédent joueur
        // ==> celui sauvegardé dans le téléphone
        mUser = new User();
        // On récupère le nom du joueur
        mUser.setFirstname(mSharedPreferences.getString(PREF_KEY_FIRSTNAME, null));
        // Si un nom de joueur existe
        if (null != mUser.getFirstname()) {
            // On récupère le score du joueur
            mUser.setScore(mSharedPreferences.getInt(PREF_KEY_SCORE, 0));

            // On affiche le nom et le score du joueur
            JoueurConnu();

            // Active le bouton de jeu
            btn.setEnabled(true);
        }


        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn.setEnabled(s.toString().length() !=0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivityForResult(intent,GAME_REQUEST_CODE);
            }
        });


        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScoreIntent = new Intent(getApplicationContext(), HighScoreActivity.class);
                // Put the scores tab to ScoresActivity
                highScoreIntent.putExtra("Scores", mHighScore);
                startActivity(highScoreIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GAME_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            int score = data.getIntExtra(GameActivity.BUNDLE_REQUEST_CODE, 0);
            // On crée un nouvel objet User qui contiendra les informations
            // du nouveau joueur : son nom et son score
            mUser = new User();

            // Save the name of the player
            mUser.setFirstname(edit.getText().toString().toLowerCase());
            // Put the player's name in the preferences
            mSharedPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstname()).apply();

            // Save the score of the player in the User Object
            mUser.setScore(score);
            // Put the score of the player in the preferences
            mSharedPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            // Add the new player and his score in table scores
            mHighScore.addScore(mUser);

            // Put the table scores in the preferences
            //------------------------------------------------------------
            final Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .create();
            String json = gson.toJson(mHighScore);

            mSharedPreferences.edit().putString(PREF_KEY_SCORETAB, json).apply();
            //------------------------------------------------------------

            // Un joueur et son score sont maintenant disponible, on active le bouton
            // permettant de visualiser le tableau des scores
            btn_score.setEnabled(true);

            // On affiche les informations sur le joueur actuel
            JoueurConnu();
        }
    }

    private void JoueurConnu() {

        // On affiche le message récapitulatif
        String fulltext = "Bon retour, " + mUser.getFirstname()
                + "!\nTon dernier score était de : " + mUser.getScore()
                + ", Feras-tu mieux cette fois-ci ?";
        textView.setText(fulltext);
        edit.setText(mUser.getFirstname());
        edit.setSelection(mUser.getFirstname().length());
        btn.setEnabled(true);
    }
}
