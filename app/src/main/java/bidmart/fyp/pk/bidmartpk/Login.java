package bidmart.fyp.pk.bidmartpk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText l_email, l_pass;
    private Button login, signup, forgotPass;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        progressDialog= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null){

            //Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), AccountInfo.class));

        }


        l_email = (EditText)findViewById(R.id.login_email);
        l_pass = (EditText)findViewById(R.id.login_pass);

        login = (Button)findViewById(R.id.bt_login);
        signup = (Button)findViewById(R.id.login_signup);

        forgotPass = (Button)findViewById(R.id.btn_reset_pass);
        signup.setOnClickListener(this);

        login.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    private void Signin(){

        String email = l_email.getText().toString().trim();
        String pass = l_pass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Signing In, Please Wait...");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {

                            progressDialog.cancel();



                         //   checkVerifiedUser();
                            startActivity(new Intent(getApplicationContext(), AccountInfo.class));
                            Toast.makeText(Login.this, "logged in Successfully",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        else {
                            progressDialog.cancel();
                            Toast.makeText(Login.this, "login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }



                    }
                });

    }


        private void checkVerifiedUser(){

            FirebaseUser user = mAuth.getInstance().getCurrentUser();



            if (user.isEmailVerified() == true ) {


                startActivity(new Intent(getApplicationContext(), AccountInfo.class));
                Toast.makeText(Login.this, "logged in Successfully",
                        Toast.LENGTH_SHORT).show();
                finish();

            }

            else {

                Toast.makeText(getApplicationContext(), "Please verify Your Email", Toast.LENGTH_SHORT).show();

            }


        }


    @Override
    public void onClick(View view) {
        if(view == login){

            Signin();
        }

        if(view == signup){

            finish();
            startActivity( new Intent(getApplicationContext(), Signup.class));
        }

        if(view == forgotPass){


            startActivity( new Intent(getApplicationContext(), ResetPassword.class));
        }
    }
}
