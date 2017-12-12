package bidmart.fyp.pk.bidmartpk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AccountInfo extends AppCompatActivity implements View.OnClickListener{


    private Button email, pass, phoneNo, name, logout, pad, viewAds, myAdsBt;

    private FirebaseAuth mAuth;
    private static final String TAG = AccountInfo.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);




        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() ==  null){

            finish();
            startActivity(new Intent(this, Login.class));
        }


        FirebaseUser user = mAuth.getCurrentUser();

        //checkVerifiedUser();

        name = (Button)findViewById(R.id.edit_name);
        email = (Button) findViewById(R.id.edit_email);
        pass = (Button)findViewById(R.id.edit_password);
        phoneNo = (Button)findViewById(R.id.edit_phoneNo);
        pad = (Button)findViewById(R.id.postAd);

        viewAds = (Button)findViewById(R.id.viewAdsButton);

        myAdsBt = (Button)findViewById(R.id.myAdsButton);
        viewAds.setOnClickListener(this);

        logout = (Button)findViewById(R.id.acc_logout);


        logout.setOnClickListener(this);
        phoneNo.setOnClickListener(this);
        pass.setOnClickListener(this);
        email.setOnClickListener(this);

        pad.setOnClickListener(this);

        myAdsBt.setOnClickListener(this);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowUpdateDialog();
            }
        });


    }


    private void Logout(){
        finish();
        mAuth.signOut();

        startActivity(new Intent(getApplicationContext(), Login.class));

    }

    private void checkVerifiedUser(){

        FirebaseUser user = mAuth.getInstance().getCurrentUser();



                if (user.isEmailVerified() == false ) {
                    Logout();
                    
                    Toast.makeText(getApplicationContext(), "Please verify Your Email", Toast.LENGTH_SHORT).show();
                }
            }
        ;


    private void ShowUpdateDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText newValue = (EditText) dialogView.findViewById(R.id.editDialog);

        final Button update = (Button)dialogView.findViewById(R.id.button_update);


        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map <String, String> map = (Map)dataSnapshot.getValue();

                String nameVal = map.get("name");
                newValue.setHint(nameVal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        final AlertDialog b = dialogBuilder.create();
        b.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String name = newValue.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {

                    Toast.makeText(getApplicationContext(),"Name Cannot be Empty", Toast.LENGTH_SHORT).show();


                }
                else {


                try {
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    myRef.child("name").setValue(name);
                    Toast.makeText(getApplicationContext(),"Name Updated", Toast.LENGTH_SHORT).show();
                    b.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}
        });



    }

    void changeEmail(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText newValue = (EditText) dialogView.findViewById(R.id.editDialog);

        final Button update = (Button)dialogView.findViewById(R.id.button_update);





        newValue.setHint("Enter New Email");



        final AlertDialog b = dialogBuilder.create();
        b.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedEmail = newValue.getText().toString().trim();


                FirebaseUser user = mAuth.getCurrentUser();



                user.updateEmail(updatedEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                                    myRef.child("email").setValue(user.getEmail());

                                    Toast.makeText(getApplicationContext(), "Email Changed!", Toast.LENGTH_SHORT).show();
                                    b.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Changing Email Requires Recent Login", Toast.LENGTH_SHORT).show();
                                    b.dismiss();
                                }
                            }
                        });

            }
        });

    }

    void changePhoneNo(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText newValue = (EditText) dialogView.findViewById(R.id.editDialog);

        final Button update = (Button)dialogView.findViewById(R.id.button_update);


        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map <String, String> map = (Map)dataSnapshot.getValue();

                String nameVal = map.get("phoneNo");
                newValue.setHint(nameVal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        final AlertDialog b = dialogBuilder.create();
        b.show();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = newValue.getText().toString().trim();


                 if (TextUtils.isEmpty(phoneNo)) {

                     Toast.makeText(getApplicationContext(),"Phone No cannot be Empty", Toast.LENGTH_SHORT).show();

                 return;
                 }
                 else

                try {
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    myRef.child("phoneNo").setValue(phoneNo);
                    Toast.makeText(getApplicationContext(),"Phone No changed", Toast.LENGTH_SHORT).show();
                    b.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Error!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }


public void changePassword(){

    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    LayoutInflater inflater = getLayoutInflater();
    final View dialogView = inflater.inflate(R.layout.update_dialog, null);
    dialogBuilder.setView(dialogView);

    final EditText newValue = (EditText) dialogView.findViewById(R.id.editDialog);

    final Button update = (Button)dialogView.findViewById(R.id.button_update);





    newValue.setHint("Enter New Password");



    final AlertDialog b = dialogBuilder.create();
    b.show();





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedPass = newValue.getText().toString().trim();


                FirebaseUser user = mAuth.getCurrentUser();



                user.updatePassword(updatedPass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {



                                    Toast.makeText(getApplicationContext(), "Password is updated!", Toast.LENGTH_SHORT).show();
                                    b.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Changing Password Requires Recent Login", Toast.LENGTH_SHORT).show();
                                    b.dismiss();
                                }
                            }
                        });

                    }
            });





    }

    public void DeleteUser(){

         final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

                          user.delete();
    }




    @Override
    public void onClick(View view) {
        if (view == logout){

            Logout();
        }

        if (view == phoneNo){
            changePhoneNo();
        }

        if (view == pad){


            startActivity(new Intent(this, PostProductAd.class));
        }



        if (view ==pass){

            changePassword();
        }

        if (view == email){

            changeEmail();
        }

        if(view == viewAds){


            startActivity(new Intent(this, AdsView.class));

        }

        if(view == myAdsBt){

            startActivity(new Intent(this, MyAds.class));
        }
    }
}