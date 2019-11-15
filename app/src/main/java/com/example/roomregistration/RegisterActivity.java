package com.example.roomregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private EditText RegisterEmail;
    private EditText RegisterPass;
    private EditText RegisterRePass;


    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        RegisterEmail = (EditText) findViewById(R.id.RegisterEnterEmail);
        RegisterPass = (EditText) findViewById(R.id.RegisterEnterPass);
        RegisterRePass = (EditText) findViewById(R.id.RegisterReEnterPass);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.registerProgressBar);

        gestureDetector = new GestureDetector(this, this);
    }


    public void registerUser()
    {
        String email = RegisterEmail.getText().toString().trim();
        String pass = RegisterPass.getText().toString().trim();
        String repass = RegisterRePass.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Enter password", Toast.LENGTH_SHORT).show();
            return;

        }
        if (pass.equals(repass))
        {

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginScreenActivity.class));
                            }
                            else  {
                                Toast.makeText(RegisterActivity.this,"Could not register, please try again", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else{
            Toast.makeText(this,"Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }


    public void RegisteredClick(View view){

        registerUser();

    }

    public void BacktoSignInClick(View view) {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        boolean swipeLeft = e1.getX() < e2.getX();
        if (swipeLeft) {

            Log.d("SWIPE", "I SWIPED LEFT!");
            finish();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);


        }
        return true;
    }
}
