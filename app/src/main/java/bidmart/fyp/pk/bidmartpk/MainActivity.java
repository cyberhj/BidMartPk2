package bidmart.fyp.pk.bidmartpk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button login;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        if(mAuth.getCurrentUser() !=null){

            //Profile Activity
            finish();
            startActivity(new Intent(getApplicationContext(), AccountInfo.class));

        }

        login = (Button)findViewById(R.id.main_login);

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == login){

            finish();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }
}
