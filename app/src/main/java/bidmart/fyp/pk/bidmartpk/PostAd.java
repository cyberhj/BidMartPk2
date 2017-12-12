package bidmart.fyp.pk.bidmartpk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public class PostAd extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PostAd.class.getSimpleName();
    public static final int GALLERY_INTENT = 2;

    EditText pname, pprice, pdesc, sname;
    Spinner pcategory;
    Button psubmit;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef, AllAdsRef;
    private FirebaseDatabase database;
    private StorageReference mStorage;
    String producTD;
    Uri downloadUri;
    ImageButton selectImg;
    ImageView adImg1;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);

        myRef =  FirebaseDatabase.getInstance().getReference().child("Ads");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();



        progressDialog= new ProgressDialog(this);


        pname = (EditText)findViewById(R.id.p_name);
        pprice = (EditText)findViewById(R.id.p_price);
        pdesc = (EditText)findViewById(R.id.p_desc);
        sname = (EditText)findViewById(R.id.p_sname);

        selectImg = (ImageButton)findViewById(R.id.selectAdImg);

        pcategory = (Spinner)findViewById(R.id.p_category);



        psubmit = (Button)findViewById(R.id.ad_submit);

        FirebaseUser user = mAuth.getCurrentUser();
        sname.setText(user.getEmail());

        psubmit.setOnClickListener(this);
        selectImg.setOnClickListener(this);

    }



    private void SubmitAd(){



    }

    public void SetImage(){

                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = mAuth.getCurrentUser();


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            selectImg.setImageURI(uri);
            StorageReference filePath = mStorage.child(user.getUid()).child("image1");
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            });

            }
        }



        public void PostProducAd(){

            Toast.makeText(getApplicationContext(), "Posting Ad", Toast.LENGTH_SHORT).show();
            progressDialog.setMessage("Posting Ad, Please Wait...");
            progressDialog.show();

            user = mAuth.getCurrentUser();


            String adname = pname.getText().toString().trim();
            String adprice = pprice.getText().toString().trim();
            String addesc = pdesc.getText().toString().trim();
            String adcat = String.valueOf(pcategory.getSelectedItem());

            String adsname = sname.getText().toString().trim();

            producTD = UUID.randomUUID().toString();





            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            final  String date = df.format(Calendar.getInstance().getTime());

         //   final AdInformation ainfo = new AdInformation(adname,adprice,addesc,adcat, adsname, producTD, date);

            myRef =  FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid());



            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            DatabaseReference users = root.child("Ads");
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child(user.getUid()).exists()) {


                        myRef.push().child(producTD);

                        //     if(snapshot.child(user.getUid()).child(producTD).exists())

                        myRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid()).child(producTD);

                       // myRef.setValue(ainfo);


                        //For All Ads


                        AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds");
                        AllAdsRef.push().child(producTD);
                        AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds").child(producTD);

                       // AllAdsRef.setValue(ainfo);

                        progressDialog.dismiss();

                        startActivity(new Intent(PostAd.this, AdsView.class));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            progressDialog.cancel();


        }

    @Override
    public void onClick(View view) {


     if(view.getId() == psubmit.getId()){

         PostProducAd();

     }


        if (view == selectImg){

            SetImage();
        }



    }


}
