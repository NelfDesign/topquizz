package fr.nelfdesign.topquiz.controller;

import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.nelfdesign.topquiz.model.HighScore;
import fr.nelfdesign.topquiz.model.Question;
import fr.nelfdesign.topquiz.model.QuestionBank;
import fr.nelfdesign.topquiz.model.User;

/**
 * Created by Nelfdesign at 31/05/2018
 * fr.nelfdesign.topquiz.controller
 */
public class Utilities {
    //methode pour generer les questions
    public static QuestionBank generateQuestions() {

        Question question1 = new Question("Who is the creator of Android?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question2 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question3 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        Question question4 = new Question("How many Tomb Raider movies were made?",
                Arrays.asList("2", "3", "4", "5"),
                1);

        Question question5 = new Question("What color is the French wine Beaujolais?",
                Arrays.asList("Red", "White","Purple", "Pink"),
                0);

        Question question6 = new Question("Who did paint the famous painting Guernica?",
                Arrays.asList("Dali", "Matis", "Leonard de vinci", "Picasso"),
                3);

        Question question7 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question8 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question9 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question10 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question11 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question12 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6,
                question7, question8, question9, question10, question11, question12));
    }

    //methode pour renvoyer un message et activer le bouton jeu
    public static void JoueurConnu(User mUser, TextView textView, EditText edit, Button btn) {
        // On affiche le message récapitulatif
        String fulltext = "Bon retour, " + mUser.getFirstname()
                + "!\nTon dernier score était de : " + mUser.getScore() + "/8"
                + ", \nFeras-tu mieux cette fois-ci ?";
        textView.setText(fulltext);
        edit.setText(mUser.getFirstname());
        edit.setSelection(mUser.getFirstname().length());
        btn.setEnabled(true);
    }

    //methode pour créer un tableau des 5 meilleurs scores
    public static ArrayList<User> tableau5Scores(HighScore mHighScore) {
        ArrayList<User> scoresFive = new ArrayList<User>();
        // On trie préalablement la liste afin d'avoir le score le plus élevé en début
        Collections.sort(mHighScore.getScoresTab(),new User());
        Collections.reverse(mHighScore.getScoresTab());
        // On conserve uniquement les 5 premiers postes du tableau de score
        for (int i = 0;i<mHighScore.getScoresTab().size();i++){
            scoresFive.add(mHighScore.getScoresTab().get(i));
        }
        return scoresFive;
    }

    // Methode qui met à jour l'affichage du tableau de scores
    public static void updateTab(List<User> userList, List<TextView> textViewList) {

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
