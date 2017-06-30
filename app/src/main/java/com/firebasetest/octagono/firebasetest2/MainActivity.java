package com.firebasetest.octagono.firebasetest2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebasetest.octagono.firebasetest2.Fragments.MainActivityFragments.LogInFragment;
import com.firebasetest.octagono.firebasetest2.Fragments.MainActivityFragments.RegisterFragment;
import com.firebasetest.octagono.firebasetest2.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FragmentManager fm = getSupportFragmentManager();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.fragment_container,LogInFragment.newInstance()).commit();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent newActivity = new Intent(MainActivity.this, Home.class);
                    startActivity(newActivity);
                    finish();
                }
            }
        };


    }

    public void gotoRegister(View view){
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, RegisterFragment.newInstance())
                .commit();
    }
    public void gotoLogIn(View view){
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, LogInFragment.newInstance())
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void RegisterAccount(View view){
        TextView username = (TextView) findViewById(R.id.txtRegisterUsername);
        TextView password = (TextView) findViewById(R.id.txtRegisterPassword);
        TextView repeatPassword = (TextView) findViewById(R.id.txtRegisterRepeatPassword);
        if (
                username.getText().toString()!= null &&
                password.getText().toString() != null &&
                repeatPassword.getText().toString() !=null
                ){
            if (password.getText().toString().equals(repeatPassword.getText().toString())){
                mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "No se pudo registrar esa cuenta. Intentelo nuevamente.",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null){

                                        User newuser = new User(user.getEmail(),user.getDisplayName());
                                        myRef.child(getUsername(user.getEmail())).setValue(newuser);

                                        Intent newActivity = new Intent(MainActivity.this, Home.class);
                                        startActivity(newActivity);
                                        finish();
                                    }
                                }
                            }
                        });
            }
        }
    }
    private String getUsername(String mail){
        String[] username = mail.split("@");
        return username[0];
    }

    public void LoginAccount(View view){
        TextView username = (TextView) findViewById(R.id.txtLoginEmail);
        TextView password = (TextView) findViewById(R.id.txtLoginPassword);
        if (username.getText().toString()!= null && password.getText().toString() != null){
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Email o Password Incorrecto. Intente Nuevamente",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null){
                                    Intent newActivity = new Intent(MainActivity.this, Home.class);
                                    startActivity(newActivity);
                                    finish();
                                }
                            }
                        }
                    });
        }

    }

}
