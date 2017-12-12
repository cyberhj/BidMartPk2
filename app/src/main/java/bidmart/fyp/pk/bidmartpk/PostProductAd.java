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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public class PostProductAd extends AppCompatActivity implements View.OnClickListener{
    public static final int GALLERY_INTENT = 2;
    EditText p_name, p_desc, p_price,p_sellerName;
    Spinner p_category;
    Button p_submit;
    ImageButton selectImg;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference myRef, AllAdsRef, sellerRef;
    private StorageReference mStorage;
    private String producTD;
    private Uri downloadUri;

    private String sellerNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product_ad);
        myRef =  FirebaseDatabase.getInstance().getReference();


        p_name = (EditText)findViewById(R.id.newProductName);
        p_price= (EditText)findViewById(R.id.newProductPrice);
        p_desc = (EditText)findViewById(R.id.newProductDesc);
        p_sellerName = (EditText)findViewById(R.id.newSellerName);

        p_category = (Spinner) findViewById(R.id.newProductCategory);

        selectImg = (ImageButton)findViewById(R.id.product_image);

        producTD = UUID.randomUUID().toString();

        p_submit = (Button)findViewById(R.id.new_ProductSubmit);

        p_submit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        user = mAuth.getCurrentUser();

        progressDialog= new ProgressDialog(this);

        selectImg.setOnClickListener(this);

        sellerRef = FirebaseDatabase.getInstance().getReference().child("Users");
        sellerRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map)dataSnapshot.getValue();

                String sellerName = map.get("name");
                sellerNo = map.get("phoneNo");




                p_sellerName.setText(sellerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public void PostAd() {

        progressDialog.setMessage("Posting Ad");
        progressDialog.show();

        String adname = p_name.getText().toString().trim();
        String adprice = p_price.getText().toString().trim();
        String addesc = p_desc.getText().toString().trim();
        String adcat = String.valueOf(p_category.getSelectedItem());

        String adsname = p_sellerName.getText().toString().trim();





        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        final  String date = df.format(Calendar.getInstance().getTime());

        final AdInformation ainfo = new AdInformation(adname,adprice,addesc,adcat, adsname, producTD, date, downloadUri.toString(),sellerNo );


        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("Ads");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).exists()) {


                    myRef.push().child(producTD);


                    myRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid()).child(producTD);
                    myRef.setValue(ainfo);

                    AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds");
                    AllAdsRef.push().child(producTD);
                    AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds").child(producTD);

                    AllAdsRef.setValue(ainfo);
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(PostProductAd.this, AdsView.class));
                }

                else {

                    DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid());

                    root.push().child(producTD);

                    myRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid()).child(producTD);
                    myRef.setValue(ainfo);

                    AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds");
                    AllAdsRef.push().child(producTD);
                    AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds").child(producTD);

                    AllAdsRef.setValue(ainfo);
                    progressDialog.dismiss();

                    finish();
                    startActivity(new Intent(PostProductAd.this, AdsView.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == p_submit){

            PostAd();
        }
        
        if (view == selectImg){
            
            
            SetImage();
        }
    }

    private void SetImage() {

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_INTENT);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = mAuth.getCurrentUser();


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            final Uri uri = data.getData();
            progressDialog.setMessage("Loading Image");
            progressDialog.show();
            StorageReference filePath = mStorage.child(user.getUid()).child(producTD).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUri = taskSnapshot.getDownloadUrl();
                    selectImg.setImageURI(uri);
                    progressDialog.dismiss();
                }
            });

        }
    }
}
