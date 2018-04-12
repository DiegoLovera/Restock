package com.lovera.diego.restock;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lovera.diego.restock.common.ImageRoundCorners;
import com.lovera.diego.restock.common.Serializer;
import com.lovera.diego.restock.models.Country;
import com.lovera.diego.restock.models.Product;
import com.squareup.picasso.Picasso;

public class ProductDialogFragment extends DialogFragment {

    private Product mProduct;
    private Country mCountry;
    private ImageView mImageCountry;
    public ProductDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_dialog, container, false);
        TextView textName = view.findViewById(R.id.text_view_name_fragment_product);
        TextView textBrand = view.findViewById(R.id.text_view_brand_fragment_product);
        TextView textDescription = view.findViewById(R.id.text_view_description_fragment_product);
        TextView textGoes = view.findViewById(R.id.text_view_goes_fragment_product);
        TextView textGrape = view.findViewById(R.id.text_view_grape_fragment_product);
        TextView textYear = view.findViewById(R.id.text_view_year_fragment_product);
        mImageCountry = view.findViewById(R.id.image_origin_place_fragment_product);
        ImageView imageProduct = view.findViewById(R.id.image_product_fragment_product);
        WebView webView = view.findViewById(R.id.web_view_model);

        if (getArguments() != null) {
            mProduct = Serializer.Deserialize(getArguments().getString("productJson"), Product.class);
        }
        if (mProduct.getModel() == null || mProduct.getModel().isEmpty()){
            webView.setVisibility(View.GONE);
        } else {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(mProduct.getModel());
        }

        textName.setText(mProduct.getName());
        textBrand.setText(mProduct.getBrand());
        textDescription.setText(mProduct.getDescription());
        textGoes.setText(mProduct.getGoesWith());
        textGrape.setText(mProduct.getGrape());
        textYear.setText(mProduct.getYear());
        Picasso.get()
                .load(mProduct.getImage())
                .error(R.drawable.side_nav_bar)
                .into(imageProduct);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Country");
        Query query = reference
                .child(mProduct.getPlaceOfOrigin());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mCountry = dataSnapshot.getValue(Country.class);
                    Picasso.get()
                            .load(mCountry.getImage())
                            .resize(80, 80)
                            .transform(new ImageRoundCorners())
                            .error(R.drawable.side_nav_bar)
                            .into(mImageCountry);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

}
