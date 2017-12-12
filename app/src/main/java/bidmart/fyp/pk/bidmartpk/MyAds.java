package bidmart.fyp.pk.bidmartpk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MyAds extends AppCompatActivity {

    private RecyclerView myAdsList;
    private DatabaseReference mRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        myAdsList = (RecyclerView) findViewById(R.id.myAdsList);
        myAdsList.setHasFixedSize(true);
        myAdsList.setLayoutManager(new LinearLayoutManager(this));
        mRef = FirebaseDatabase.getInstance().getReference().child("Ads").child(user.getUid());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");




    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();

        FirebaseRecyclerAdapter<AdInformation, MyAds.AdInformationViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AdInformation, MyAds.AdInformationViewHolder>(
                AdInformation.class,
                R.layout.custom_ads_view,
                MyAds.AdInformationViewHolder.class,
                mRef

        ) {
            @Override
            protected void populateViewHolder(MyAds.AdInformationViewHolder viewHolder, AdInformation model, int position) {


                final String mProductId = getRef(position).getKey();

                viewHolder.setPname(model.getPname());
                viewHolder.setPprice(model.getPprice());
                viewHolder.setAdDate(model.getAdDate());

                viewHolder.setImage(getApplicationContext(), model.getImageUri());

                progressDialog.dismiss();
                viewHolder.mView.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleAdIntent = new Intent(MyAds.this, SingleMyAd.class);
                        singleAdIntent.putExtra("ProductID", mProductId);


                        startActivity(singleAdIntent);

                        //Toast.makeText(getApplicationContext(), mProductId, Toast.LENGTH_LONG).show();
                    }
                }));

            }



        };

        myAdsList.setAdapter(firebaseRecyclerAdapter);



    }

    public static class AdInformationViewHolder extends RecyclerView.ViewHolder{

        View mView;



        public AdInformationViewHolder(View itemView) {
            super(itemView);


            mView = itemView;
        }

        public void setPname(String pname){

            TextView prname = (TextView)mView.findViewById(R.id.r_ad_pName);

            prname.setText(pname);
        }
        public void setPprice(String pprice){
            TextView prprice = (TextView)mView.findViewById(R.id.r_ad_pPrice);

            prprice.setText("Rs. "+ pprice);

        }
        public void setAdDate(String adDate){

            TextView pdate = (TextView)mView.findViewById(R.id.r_ad_pDate);

            pdate.setText(adDate);
        }

        public void setSellerName(String sname){

            TextView AdsSellerName = (TextView)mView.findViewById(R.id.AdsSellerName);

            AdsSellerName .setText(sname);
        }

        public void setImage(Context ctx, String img){

            ImageView image = (ImageView) mView.findViewById(R.id.viewAdImage);
            Picasso.with(ctx).load(img).into(image);



        }


    }
}
