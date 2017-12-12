package bidmart.fyp.pk.bidmartpk;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class SingleAd extends AppCompatActivity implements View.OnClickListener {

    TextView productName, productPrice, productDesc, productCategory,sellerName;

    String mProductId = null;

    ImageView productImage;
    private DatabaseReference mRef;
    private String sellerPhone;

    Button contactSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_ad);

        mProductId = getIntent().getStringExtra("ProductID");

        mRef = FirebaseDatabase.getInstance().getReference().child("AllAds");

        productName = (TextView)findViewById(R.id.singleProductName);
        productPrice = (TextView)findViewById(R.id.singleProductPrice);
        productDesc = (TextView)findViewById(R.id.singleProductDesc);
        sellerName = (TextView)findViewById(R.id.singleProductSellerName);
        productCategory = (TextView)findViewById(R.id.singleProductCategory);

        productImage = (ImageView)findViewById(R.id.singleProductImage);

        contactSeller = (Button)findViewById(R.id.contactSeller);

        contactSeller.setOnClickListener(this);


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

                Picasso.with(SingleAd.this).load(imageUri).into(productImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == contactSeller){

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sellerPhone));
            startActivity(intent);
        }
    }
}
