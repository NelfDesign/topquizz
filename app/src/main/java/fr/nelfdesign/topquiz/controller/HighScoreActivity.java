package fr.nelfdesign.topquiz.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import static java.lang.System.out;

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

        // Get back the table of score
        Intent i = getIntent();
        mHighScore = (HighScore) i.getSerializableExtra("Scores");

        // Sort out the scores of the biggest in the smallest
        // Creation of the table of 5 better scores
        ArrayList<User> scoresFive = createFiveTab();
        Collections.sort(scoresFive,new User());
        Collections.reverse(scoresFive);

        // Updates the display of the table of the scores
        if (mHighScore != null) updateScoresTab(mHighScore.getScoresTab());

        // Bouton permettant le tri par rapport au score
        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mHighScore != null) {

                    // Creation of the table of 5 better scores
                    ArrayList<User> scoresFive;
                    scoresFive = createFiveTab();

                    // Sort out the scores of the biggest in the smallest
                    Collections.sort(scoresFive,new User());
                    Collections.reverse(scoresFive);

                    // Display the scores tab
                    updateScoresTab(scoresFive);
                }
            }
        });

        // Bouton permettant le tri par rapport au nom
        btn_alpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mHighScore != null) {

                    // Creation of the table of 5 better scores
                    ArrayList<User> scoresFive;
                    scoresFive = createFiveTab();

                    // Sort the scores tab by name
                    Collections.sort(scoresFive);

                    // Display the scores tab
                    updateScoresTab(scoresFive);
                }
            }
        });
    }

    // Method for the creation of the table of 5 better scores
    private ArrayList<User> createFiveTab() {

        ArrayList<User> scoresFive = new ArrayList<User>();

        // On trie préalablement la liste afin d'avoir le score le plus élevé en début
        // de list et le score le moins élevé en fin de liste
        // Sort out the scores of the biggest in the smallest
        Collections.sort(mHighScore.getScoresTab(),new User());
        Collections.reverse(mHighScore.getScoresTab());

        // On conserve uniquement les 5 premiers postes du tableau de score
        for (int i = 0;i<mHighScore.getScoresTab().size();i++){
            out.println("index = "+i);
            scoresFive.add(mHighScore.getScoresTab().get(i));
        }
        return scoresFive;
    }

    // Methode qui met à jour l'affichage du tableau de scores
    private void updateScoresTab(List<User> userList) {

        String name;
        int score, index = 0;

        for (TextView textView : textViewList) {
            if (index < userList.size() ) {
                if (userList.get(index) != null) {
                    name = userList.get(index).getFirstname();
                    score = userList.get(index).getScore();

                    textView.setText(name.toUpperCase() + " => " + score + " points");
                }
            } else {
                textView.setText("    " + "-" + "  " + "unknown");

                textView.setTextColor(Color.GRAY);
                textView.setTypeface(Typeface.DEFAULT,Typeface.ITALIC);
            }
            index++;
        }
    }
}
