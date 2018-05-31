package fr.nelfdesign.topquiz.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Nelfdesign at 29/05/2018
 * fr.nelfdesign.topquiz.model
 */
public class User implements Comparator<User>, Comparable<User>, Serializable {

    private int mScore;
    private String mFirstname;

    public User() {}

    public User(String firstName, int score) {
        mFirstname = firstName;
        mScore = score;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public Integer getScore() {
        return mScore;
    }

    public void setScore(Integer score) {
        mScore = score;
    }

    @Override
    public int compare(User user1, User user2) {
        int result;
        result = user1.getScore().compareTo(user2.getScore());
        if (result == 0) result = user1.getFirstname().compareTo(user2.getFirstname());

        return result;
    }

    @Override
    public int compareTo(@NonNull User user) {
        return this.mFirstname.compareTo(user.getFirstname());
    }
}
