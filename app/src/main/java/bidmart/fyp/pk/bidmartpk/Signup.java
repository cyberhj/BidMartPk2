package bidmart.fyp.pk.bidmartpk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Signup.class.getSimpleName();

    private   EditText s_email, s_pass, c_pass, s_name, s_phoneNo;
    private CheckBox chkTos;
    private   Button signup, login;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef =  FirebaseDatabase.getInstance().getReference().child("Users");


        if(mAuth.getCurrentUser() !=null){

            //Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), AccountInfo.class));

        }


        progressDialog= new ProgressDialog(this);

        s_email = (EditText)findViewById(R.id.signup_email);
        s_pass = (EditText)findViewById(R.id.signup_pass);
        c_pass =  (EditText)findViewById(R.id.c_password);
        s_phoneNo = (EditText)findViewById(R.id.signup_phoneNo);
        s_name = (EditText)findViewById(R.id.signup_name);
        chkTos = (CheckBox)findViewById(R.id.check_tos);

        signup = (Button)findViewById(R.id.bt_signup);

        login = (Button)findViewById(R.id.main_login);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        chkTos.setOnClickListener(this);

        signup.setEnabled(false);

    }


    private void SaveInfo()
    {

        String name = s_name.getText().toString().trim();
        String phone = s_phoneNo.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail().toString().trim();
        UserInformation userInformation = new UserInformation(name,phone,email);



        myRef.child(user.getUid()).setValue(userInformation);



    }
    private void Signup(){

        String email = s_email.getText().toString().trim();
        String pass = s_pass.getText().toString().trim();
        String cpass = c_pass.getText().toString().trim();
        String phone = s_phoneNo.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();

            return;
        }

        else {
            if ( android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() == false){

                Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();

                return;
            }
        }
        if(TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }



        if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(cpass)){

            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pass.equals(cpass) ){
            Toast.makeText(Signup.this, "Password Doesn't match",
                    Toast.LENGTH_SHORT).show();
            return;

        }
        else{
            progressDialog.setMessage("Signing Up, Please Wait...");
            progressDialog.show();
        }


        myRef = FirebaseDatabase.getInstance().getReference("Users");





        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {


                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                            }

                        }

                        else if (task.isSuccessful()) {

                            progressDialog.cancel();
                            Toast.makeText(Signup.this, "Registered Succesfully!!",
                                    Toast.LENGTH_SHORT).show();




                            s_email.setText(null);
                            s_pass.setText(null);

                            finish();
                            SaveInfo();
                            //sendVerificationemail();
                            startActivity(new Intent(getApplicationContext(), AccountInfo.class));
                        }

                        else{

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            Toast.makeText(Signup.this, "An error occured while registering!!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });
    }

    public void sendVerificationemail(){

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i("Success", "Yes");
                }
                else{
                    Log.i("Success", "No");


                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== chkTos.getId()) {

            if (chkTos.isChecked()== true){
            signup.setEnabled(true);

            }

            else
                signup.setEnabled(false);

        }


        if(view.getId() == signup.getId()){

            Signup();
        }


        if(view == login){

            startActivity(new Intent(this, Login.class));
        }
    }
}
