package bidmart.fyp.pk.bidmartpk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class SingleMyAd extends AppCompatActivity implements View.OnClickListener {

    TextView productName, productPrice, productDesc, productCategory,sellerName;

    String mProductId = null;

    ImageView productImage;
    private DatabaseReference mRef;
    private String sellerPhone;
    FirebaseUser user;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    Button updateAd, deleteAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_my_ad);

        mAuth = FirebaseAuth.getInstance();

        mProductId = getIntent().getStringExtra("ProductID");

        mRef = FirebaseDatabase.getInstance().getReference().child("AllAds");

        productName = (TextView)findViewById(R.id.mysingleProductName);
        productPrice = (TextView)findViewById(R.id.mysingleProductPrice);
        productDesc = (TextView)findViewById(R.id.mysingleProductDesc);
        sellerName = (TextView)findViewById(R.id.mysingleProductSellerName);
        productCategory = (TextView)findViewById(R.id.mysingleProductCategory);

        productImage = (ImageView)findViewById(R.id.mysingleProductImage);
        progressDialog= new ProgressDialog(this);

        updateAd = (Button)findViewById(R.id.updateMyAd);
        deleteAd = (Button)findViewById(R.id.deleteMyAd);
        deleteAd.setOnClickListener(this);

        updateAd.setOnClickListener(this);


        mRef.child(mProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map)dataSnapshot.getValue();

                String pName = map.get("pname");
                String pPrice = map.get("pprice");
                String pDesc = map.get("pdesc");
                String pCategory = map.get("pcategory");
                String imageUri = map.get("imageUri");

                String seller = map.get("sname");

                sellerPhone = map.get("sellerNumber");

                productName.setText("Product Name: " + pName);
                productPrice.setText("Product Price: Rs. " + pPrice);
                productCategory.setText("Product Category: " + pCategory);
                productDesc.setText("Product Description: " + pDesc);
                sellerName.setText("Sold By: " + seller);


                sellerName.setVisibility(TextView.INVISIBLE);

                Picasso.with(SingleMyAd.this).load(imageUri).into(productImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void DeleteAd(){

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(SingleMyAd.this);

        confirmDialog.setMessage("Confirm Deletion").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.show();



                user = mAuth.getCurrentUser();

                DatabaseReference delRef = FirebaseDatabase.getInstance().getReference();
                delRef.child("AllAds").child(mProductId).setValue(null);

                DatabaseReference delRef2 = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid());
                delRef2.child(mProductId).setValue(null);

                productName.setText("Product Name: " );
                productPrice.setText("Product Price: ");
                productCategory.setText("Product Category: " );
                productDesc.setText("Product Description: " );
                sellerName.setText("Sold By: ");
                Picasso.with(SingleMyAd.this).load("").into(productImage);


                sellerName.setVisibility(TextView.INVISIBLE);

                finish();
                startActivity(new Intent(getApplicationContext(), AccountInfo.class));
                progressDialog.dismiss();
            }
        }).setNegativeButton("Cancel", null);


        AlertDialog alert = confirmDialog.create();
        alert.show();


    }




    @Override
    public void onClick(View view) {

        if(view== updateAd){

            Intent singleAdIntent = new Intent(SingleMyAd.this, UpdateAd.class);
            singleAdIntent.putExtra("ProductID", mProductId);


            startActivity(singleAdIntent);

        }

        if(view == deleteAd){


            DeleteAd();
            progressDialog.setMessage("Deleting Ad");


        }
    }
}
