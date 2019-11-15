package com.example.roomregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {


    private EditText MainEmail;
    private  EditText MainPass;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainEmail = (EditText) findViewById(R.id.MainEnterEmail);
        MainPass = (EditText) findViewById(R.id.MainEnterPass);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

        gestureDetector = new GestureDetector(this, this);

    }


    public void loginUser()
    {
        String email = MainEmail.getText().toString().trim();
        String pass = MainPass.getText().toString().trim();



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
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful())
                     {
                         finish();
                         startActivity(new Intent(getApplicationContext(), LoginScreenActivity.class));
                     }
                     else{
                         progressBar.setVisibility(View.INVISIBLE);
                         Toast.makeText(MainActivity.this,"Sign in not valid", Toast.LENGTH_SHORT).show();
                     }
                    }
                });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }


    public void SignInClick(View view) {
        loginUser();
    }



    public void onClickRegisterPage(View view) {
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }


    public void onClickGuest(View view) {
        finish();
        startActivity(new Intent(this, RoomActivity.class));
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        boolean swipeRight = e1.getX() > e2.getX();
        if (swipeRight) {

            Log.d("SWIPE", "I SWIPED RIGHT!");
            finish();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);


        }
        return true;
    }

}
