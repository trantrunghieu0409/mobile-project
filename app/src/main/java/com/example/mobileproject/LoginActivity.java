package com.example.mobileproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileproject.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {


    Button loginBtn;
    String email,password;
    EditText emailEditText,passwordEditText;
    TextView signUpRefence;
    private FirebaseAuth mAuth;
    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"You login successfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            String accountID = account.getUid();
            final Account[] accountList = {new Account("example@gmail.com", "password")};
            DocumentReference documentReference = Account.getDataFromFirebase(accountID);
            if (documentReference != null) {
                documentReference.get().addOnSuccessListener(documentSnapshot -> {
                    accountList[0] = documentSnapshot.toObject(Account.class);
                    if (accountList[0] != null) {
                        accountList[0].online();
                    }
                    intent.putExtra("account", (Serializable) accountList[0]);
                    startActivity(intent);
                    finish();
                });
            }

        }else {
            Toast.makeText(this,"Login failed",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUpRefence=(TextView) findViewById((R.id.signUpReference));
        emailEditText=(EditText)findViewById(R.id.editTextRegsiterEmail);
        passwordEditText=(EditText) findViewById(R.id.editTextRegsiterPassword);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Please wait white we're setting up for you...");
                dialog.setCancelable(false);
                dialog.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

            }
        });

        signUpRefence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(newIntent);
        finish();
    }
}