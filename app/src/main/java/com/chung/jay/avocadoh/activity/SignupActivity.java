package com.chung.jay.avocadoh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.TextView;

import com.chung.jay.avocadoh.R;
import com.chung.jay.avocadoh.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jaychung on 5/20/16.
 */
public class SignupActivity extends BaseActivity {
    private static final String TAG = "SplashScreenActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Bind(R.id.email)
    TextView tvEmail;

    @Bind(R.id.password)
    TextView tvPassword;
    @Bind(R.id.repassword)
    TextView tvRePassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAuth();
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.btn_sign_up)
    public void pressedSignUp() {
        String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        String repass = tvRePassword.getText().toString();

        if (isInputValid(email, password, repass))


            signup(email, password);
    }

    private boolean isInputValid(String email, String password, String repass) {

        if (email.isEmpty() || password.isEmpty() || repass.isEmpty())
            return false;

        if (!password.equals(repass)) {
            Snackbar.make(getCurrentFocus(), "Passwords do not match", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Snackbar.make(getCurrentFocus(), "Authentication failed.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            onAuthSuccesful(task.getResult().getUser());
                        }
                    }


                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String message = e.getMessage();
                        Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void onAuthSuccesful(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(), username, user.getEmail());
    }

    private void writeNewUser(String uid, String username, String email) {
        User user = new User(username, email);

        mDatabase.child("users").child(uid).setValue(user);

        startActivity(new Intent(SignupActivity.this, BooksActivity.class));
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void setupAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

}
