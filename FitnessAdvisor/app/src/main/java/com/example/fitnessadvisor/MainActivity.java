package com.example.fitnessadvisor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.ComponentActivity;

import android.content.pm.PackageManager;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.ACTIVITY_RECOGNITION;
import static android.Manifest.permission.INTERNET;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create_account = findViewById(R.id.create_acc);
        create_account.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Register button clicked");
                mAuth = FirebaseAuth.getInstance();

                // Get text from text fields
                EditText etext = findViewById(R.id.email);
                EditText ptext = findViewById(R.id.password);

                email = etext.getText().toString();
                password = ptext.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    navigate(UserRegisterDataActivity.class);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Button login = findViewById(R.id.switch_to_login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Sign In button clicked");
                navigate(LogIn.class);
            }
        });

        Button buttonRequest = findViewById(R.id.permissions);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED){
                    // Permission is not granted
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACTIVITY_RECOGNITION)){
                        // explain to the user
                        Toast.makeText(MainActivity.this,"ACTIVITY permission Required!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
                    } else{
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);
                    }
                } else {
                    // Permission has already been granted
                    Toast.makeText(MainActivity.this,"You already have ACTIVITY permission!", Toast.LENGTH_SHORT).show();
                }

                // get permission for fine location
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    // Permission is not granted
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                        // explain to the user
                        Toast.makeText(MainActivity.this,"LOCATION permission Required!", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    } else{
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                } else {
                    // Permission has already been granted
                    Toast.makeText(MainActivity.this,"You already have LOCATION permission!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    void navigate(Object o) {
        Intent intent = new Intent(MainActivity.this, (Class<?>) o);
        startActivity(intent);
    }
}
