package com.blogspot.mrodpg.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout reg_display_name;
    private TextInputLayout reg_email_id;
    private TextInputLayout reg_password;
    private Button reg_button;
    private Toolbar mTooldbar;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    private TextView already;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mProgressDialog = new ProgressDialog(this);

        mTooldbar = (Toolbar)findViewById(R.id.reg_app_bar);
        setSupportActionBar(mTooldbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reg_display_name = (TextInputLayout)findViewById(R.id.reg_display_name);
        reg_email_id = (TextInputLayout)findViewById(R.id.reg_email);
        reg_password = (TextInputLayout)findViewById(R.id.reg_password);
        reg_button = (Button) findViewById(R.id.reg_btn);

        already = (TextView)findViewById(R.id.textViewSignup);





        mFirebaseAuth = FirebaseAuth.getInstance();

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });


        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String displayname = reg_display_name.getEditText().getText().toString();
                    String email = reg_email_id.getEditText().getText().toString();
                    String password = reg_password.getEditText().getText().toString();
                    mProgressDialog.setTitle("Creating");
                    mProgressDialog.setMessage("Please wait......");
                    mProgressDialog.setCanceledOnTouchOutside(false);


                    if(!email.isEmpty() && !password.isEmpty() && !displayname.isEmpty() ) {
                        mProgressDialog.show();

                        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = firebaseUser.getUid();
                                    HashMap<String, String> userkey = new HashMap<String, String>();
                                    userkey.put("image", "empty");
                                    userkey.put("status", "Hey there,I'm using Whatsapp");
                                    userkey.put("name", displayname);
                                    userkey.put("thumb_image","empty");
                                    userkey.put("privacy","friends");
                                    mDatabase.child("Users").child(uid).setValue(userkey);


                                    Toast.makeText(getApplicationContext(), "Welcome to LapitChat !", Toast.LENGTH_LONG).show();
                                    mProgressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(getApplicationContext(), "Please check the form and try again !", Toast.LENGTH_LONG).show();
                                    mProgressDialog.dismiss();
                                }
                            }
                        });

                    }
                }catch (Exception e)
                {

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please fill all the content",Toast.LENGTH_LONG).show();
                }



            }
        });
    }
}
