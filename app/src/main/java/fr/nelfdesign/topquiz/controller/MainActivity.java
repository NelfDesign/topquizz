package fr.nelfdesign.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.nelfdesign.topquiz.R;
import fr.nelfdesign.topquiz.model.HighScore;
import fr.nelfdesign.topquiz.model.User;

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

        mUser = new User();
        // On récupère le nom du joueur
        mUser.setFirstname(mSharedPreferences.getString(PREF_KEY_FIRSTNAME, null));
        // Si un nom de joueur existe
        if (null != mUser.getFirstname()) {
            // On récupère le score du joueur
            mUser.setScore(mSharedPreferences.getInt(PREF_KEY_SCORE, 0));

            // On affiche le nom et le score du joueur
            Utilities.JoueurConnu(mUser, textView, edit, btn);
        }

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn.setEnabled(s.toString().length() !=0);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivityForResult(intent, GAME_REQUEST_CODE);
            }
        });

        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScoreIntent = new Intent(getApplicationContext(), HighScoreActivity.class);
                // on envoie les scores à l'application
                highScoreIntent.putExtra("Scores", mHighScore);
                startActivity(highScoreIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GAME_REQUEST_CODE == requestCode && RESULT_OK == resultCode){
            int score = data.getIntExtra(GameActivity.BUNDLE_REQUEST_CODE, 0);
            // On crée un nouveau  User pour stocker le nom et le score du joueur
            mUser = new User();
            //on attribut le nom
            mUser.setFirstname(edit.getText().toString().toLowerCase());
            // on le met dans les preferences
            mSharedPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstname()).apply();
            //idem score
            mUser.setScore(score);
            mSharedPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();
            //on ajoute le tout dans highScore
            mHighScore.addScore(mUser);

            // on crée un Json pour pouvoir stocker les données dansles prefs
            final Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .create();
            String json = gson.toJson(mHighScore);

            mSharedPreferences.edit().putString(PREF_KEY_SCORETAB, json).apply();

            // On affiche les informations sur le joueur actuel
            Utilities.JoueurConnu(mUser, textView, edit, btn_score);
        }
    }
}
