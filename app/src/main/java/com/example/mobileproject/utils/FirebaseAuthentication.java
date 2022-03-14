package com.example.mobileproject.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FirebaseAuthentication {
    private FirebaseAuth mAuth;
    FirebaseAuthentication(){
        mAuth = FirebaseAuth.getInstance();
    }
    public FirebaseUser createAccount(String email, String password) {
        final FirebaseUser[] user = new FirebaseUser[1];
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user[0] = mAuth.getCurrentUser();
                        }
                    }
                }
        );
        return user[0];
    }
    public FirebaseUser loginWithEmailAndPassword(String email, String password){
        final FirebaseUser[] user = new FirebaseUser[1];
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user[0] = mAuth.getCurrentUser();
                }
            }
        });
        return user[0];
    }
    public void logout(){
        mAuth.signOut();
    }
}
