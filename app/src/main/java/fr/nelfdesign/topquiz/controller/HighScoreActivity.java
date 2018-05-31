package fr.nelfdesign.topquiz.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.nelfdesign.topquiz.R;
import fr.nelfdesign.topquiz.model.HighScore;
import fr.nelfdesign.topquiz.model.User;


public class HighScoreActivity extends AppCompatActivity {

    private TextView mPlayer1, mPlayer2, mPlayer3, mPlayer4, mPlayer5;
    private Button btn_score, btn_alpha;
    private HighScore mHighScore;
    private List<TextView> textViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        setTitle("Top Quiz High Score Player");

        //Init components
        mPlayer1 = findViewById(R.id.rank1);
        mPlayer2 = findViewById(R.id.rank2);
        mPlayer3 = findViewById(R.id.rank3);
        mPlayer4 = findViewById(R.id.rank4);
        mPlayer5 = findViewById(R.id.rank5);
        btn_score = findViewById(R.id.btn_score);
        btn_alpha = findViewById(R.id.btn_apha);

        // Stock les lignes de score dans un tableau
        textViewList = new ArrayList<>();
        textViewList.add(mPlayer1);
        textViewList.add(mPlayer2);
        textViewList.add(mPlayer3);
        textViewList.add(mPlayer4);
        textViewList.add(mPlayer5);

        // on récupère l'extra score
        Intent i = getIntent();
        mHighScore = (HighScore) i.getSerializableExtra("Scores");

        // Creation d'une table des 5 meilleurs
        ArrayList<User> scoresFive = Utilities.tableau5Scores(mHighScore);
        Collections.sort(scoresFive,new User());
        Collections.reverse(scoresFive);

        // on update la table highScore
        if (mHighScore != null){
           Utilities.updateTab(mHighScore.getScoresTab(), textViewList);
        }

        // Bouton permettant le tri par rapport au score
        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mHighScore != null) {
                    // Creation de la table des 5 meilleurs
                    ArrayList<User> scoresFive;
                    scoresFive = Utilities.tableau5Scores(mHighScore);

                    // Sort out the scores of the biggest in the smallest
                    Collections.sort(scoresFive,new User());
                    Collections.reverse(scoresFive);

                    // Display the scores tab
                   Utilities.updateTab(scoresFive, textViewList);
                }
            }
        });

        // Bouton permettant le tri par rapport au nom
        btn_alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mHighScore != null) {
                    // Creation de la table des 5 meilleurs scores
                    ArrayList<User> scoresFive;
                    scoresFive = Utilities.tableau5Scores(mHighScore);
                    // Sort the scores tab by name
                    Collections.sort(scoresFive);
                    // Display the scores tab
                    Utilities.updateTab(scoresFive, textViewList);
                }
            }
        });
    }
}
