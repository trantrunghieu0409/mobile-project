package com.example.mobileproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText registerEmail,registerPassword,registerRetypePassword;
    private FirebaseAuth mAuth;
    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"You Signed Up successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,LoginActivity.class));

        }else {
            Toast.makeText(this,"Signed up failed",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerEmail=(EditText) findViewById(R.id.editTextRegsiterEmail);
        registerPassword=(EditText) findViewById(R.id.editTextRegsiterPassword);
        registerRetypePassword=(EditText) findViewById((R.id.editTextRegsiterPasswordRetype));
        Intent HomeIntent = new Intent(RegisterActivity.this, HomeActivity.class);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String email=registerEmail.getText().toString();
                    String password=registerPassword.getText().toString();
                    if(email.equals(currentUser.getEmail().toString())) {
                        Toast.makeText(RegisterActivity.this, "User already existed", Toast.LENGTH_SHORT).show();
                        System.out.println("Already exists");
                        System.out.println("Name" + currentUser.getEmail().toString());
                        startActivity (HomeIntent);
                    }
                    else if (registerPassword.getText().toString().equals(registerRetypePassword.getText().toString())) {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            System.out.println("createUserWithEmail:success");
                                            updateUI(user);
                                        } else {

                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            try {
                                                throw task.getException();
                                            } catch(FirebaseAuthWeakPasswordException e) {
                                                Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                                Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();

                                            } catch(FirebaseAuthUserCollisionException e) {
                                                Toast.makeText(RegisterActivity.this, "User already existed", Toast.LENGTH_SHORT).show();

                                            } catch(Exception e) {
                                                Log.e(TAG, e.getMessage());
                                            }
//                                            updateUI(null);
                                        }
                                    }
                                });
                    }





            }
        });


    }

}