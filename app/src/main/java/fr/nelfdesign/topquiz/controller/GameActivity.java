package fr.nelfdesign.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import fr.nelfdesign.topquiz.R;
import fr.nelfdesign.topquiz.model.Question;
import fr.nelfdesign.topquiz.model.QuestionBank;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1, btn2, btn3, btn4;
    private TextView tv_question;
    private Question mCurrentQuestion;
    private QuestionBank mQuestionBank;
    private int mScore;
    private int mQuestion;

    public static final String BUNDLE_REQUEST_CODE = "Score";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setTitle("TOP QUIZ GAME");

        mQuestionBank = this.generateQuestions();

        if (savedInstanceState != null){
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mQuestion = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        }else {
            mScore = 0;
            mQuestion = 4;
        }

        mEnableTouchEvent = true;
        //widget
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        tv_question = findViewById(R.id.tv_question);

        //use the tag property to 'name' the button
        btn1.setTag(0);
        btn2.setTag(1);
        btn3.setTag(2);
        btn4.setTag(3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);

    }

    private void displayQuestion(final Question question){
        tv_question.setText(question.getQuestion());
        btn1.setText(question.getChoiceList().get(0));
        btn2.setText(question.getChoiceList().get(1));
        btn3.setText(question.getChoiceList().get(2));
        btn4.setText(question.getChoiceList().get(3));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //memorisation du score et du nombre de questions restantes
        outState.putInt(BUNDLE_STATE_QUESTION,mQuestion);
        outState.putInt(BUNDLE_STATE_SCORE,mScore);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int resultIndex = (int) v.getTag();
        
        if (resultIndex == mCurrentQuestion.getAnswerIndex()){
            mScore++;
            Toast.makeText(this, "Bonne réponse !!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Mauvaise Réponse !!", Toast.LENGTH_SHORT).show();
        }

        //desactivation du touch sur l'écran
        mEnableTouchEvent=false;
        //pose de 2sec
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //reactivation du touch
                mEnableTouchEvent=true;
                if (--mQuestion == 0){
                    endGame();//si c'est la dernière question fin du jeu
                }else {
                    //sinon on passe à la question suivante
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); //delai du toast
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvent && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Bien joué !!")
                .setMessage("Votre score est de : " + mScore)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //end of game
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_REQUEST_CODE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private QuestionBank generateQuestions() {

        Question question1 = new Question("Who is the creator of Android?",
                Arrays.asList("Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"),
                0);

        Question question2 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958",
                        "1962",
                        "1967",
                        "1969"),
                3);

        Question question3 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42",
                        "101",
                        "666",
                        "742"),
                3);

        Question question4 = new Question("How many Tomb Raider movies were made?",
                Arrays.asList("2",
                        "3",
                        "4",
                        "5"),
                1);

        Question question5 = new Question("What color is the French wine Beaujolais?",
                Arrays.asList("Red",
                        "White",
                        "Purple",
                        "Pink"),
                0);

        Question question6 = new Question("Who did paint the famous painting Guernica?",
                Arrays.asList("Dali",
                        "Matis",
                        "Leonard de vinci",
                        "Picasso"),
                3);

        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6));
    }
}
