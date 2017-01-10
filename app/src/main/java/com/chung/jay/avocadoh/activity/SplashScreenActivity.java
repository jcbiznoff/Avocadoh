package com.chung.jay.avocadoh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
            goToSignInPage();
        else
            goToDashboard();

        finish();
    }

    private void goToDashboard() {
        startActivity(new Intent(this, BooksActivity.class));
    }

    private void goToSignInPage() {
        startActivity(new Intent(this, SigninActivity.class));
    }

}
