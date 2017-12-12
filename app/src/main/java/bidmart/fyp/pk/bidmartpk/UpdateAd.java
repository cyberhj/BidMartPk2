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
import android.widget.TextView;
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

public class UpdateAd extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY_INTENT = 2;
    EditText p_name, p_desc, p_price,p_sellerName;
    Spinner p_category;
    Button p_submit;
    ImageButton selectImg;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mRef, AllAdsRef, sellerRef;
    private StorageReference mStorage;
    private String producTD;
    private Uri downloadUri;
    String mProductId = null;
    private String sellerNo;
    String number, category, pdate;
    String imageUri;
    String mimageUri;
    String pID =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ad);

        mProductId = getIntent().getStringExtra("ProductID");

        //Toast.makeText(getApplicationContext(), mProductId, Toast.LENGTH_LONG).show();

        p_name = (EditText)findViewById(R.id.update_newProductName);
        p_price= (EditText)findViewById(R.id.update_newProductPrice);
        p_desc = (EditText)findViewById(R.id.update_newProductDesc);
        p_sellerName = (EditText)findViewById(R.id.update_newSellerName);



        p_category = (Spinner) findViewById(R.id.update_newProductCategory);

        selectImg = (ImageButton)findViewById(R.id.update_product_image);

        producTD = UUID.randomUUID().toString();

        mRef = FirebaseDatabase.getInstance().getReference().child("AllAds");


        p_submit = (Button)findViewById(R.id.update_new_ProductSubmit);

        p_submit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        user = mAuth.getCurrentUser();

        progressDialog= new ProgressDialog(this);

        selectImg.setOnClickListener(this);


        mRef.child(mProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map)dataSnapshot.getValue();

                String pName = map.get("pname");
                String pPrice = map.get("pprice");
                String pDesc = map.get("pdesc");
                pdate = map.get("adDate");
                category = map.get("pcategory");
                imageUri = map.get("imageUri");
                number = map.get("sellerNumber");
                String seller = map.get("sname");
                pID = map.get("productID");

                p_name.setText(pName);
                p_price.setText(pPrice);

                p_desc.setText(pDesc);

                p_sellerName.setText(seller);
                p_sellerName.setEnabled(false);

                Picasso.with(getApplicationContext()).load(imageUri).into(selectImg);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                    imageUri = taskSnapshot.getDownloadUrl().toString();
                    selectImg.setImageURI(uri);
                    progressDialog.dismiss();
                }
            });

        }
    }

    public void SubmitUpdate(){



        String adname = p_name.getText().toString().trim();
        String adprice = p_price.getText().toString().trim();
        String addesc = p_desc.getText().toString().trim();
        String adcat = String.valueOf(p_category.getSelectedItem());

        String adsname = p_sellerName.getText().toString().trim();









        final AdInformation ainfo = new AdInformation(adname,adprice,addesc,adcat, adsname, pID, pdate, imageUri,sellerNo );


        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("Ads").child(user.getUid());
        users.child(pID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                    // Update in Seller Database
                    sellerRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid()).child(pID);
                    //sellerRef.setValue(null);
                   // sellerRef.push().setValue(pID);
               // sellerRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid()).child(pID);

                sellerRef.setValue(ainfo);



                    //update in All Ads Database
                    AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds").child(pID);
                    //AllAdsRef.setValue(null);
                    //AllAdsRef.push().setValue(pID);
                   // AllAdsRef = FirebaseDatabase.getInstance().getReference().child("AllAds").child(pID);
                    AllAdsRef.setValue(ainfo);
                    progressDialog.dismiss();


                    finish();
                    startActivity(new Intent(getApplicationContext(), MyAds.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error Updating Ad..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {


        if (view == p_submit){


            SubmitUpdate();
            progressDialog.setMessage("Updating Ad");
            progressDialog.show();
        }
        if (view == selectImg){


            SetImage();
        }
    }


}
