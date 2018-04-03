package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lovera.diego.restock.adapters.MainItemAdapter;
import com.lovera.diego.restock.adapters.ProductItemAdapter;
import com.lovera.diego.restock.adapters.TypeItemAdapter;
import com.lovera.diego.restock.models.Category;
import com.lovera.diego.restock.models.Product;
import com.lovera.diego.restock.models.Type;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainItemAdapter.ItemClickListener,
        TypeItemAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private MainItemAdapter mMainItemAdapter;
    private TypeItemAdapter mTypeItemAdapter;
    private ProductItemAdapter mProductItemAdapter;
    private ImageView mNavHeaderProfilePicture;
    private TextView mNavHeaderProfileName;
    private TextView mNavHeaderProfileEmail;
    private ArrayList<Category> mCategories = new ArrayList<>();
    private ArrayList<Type> mTypes = new ArrayList<>();
    private ArrayList<Product> mProducts = new ArrayList<>();

    private Category mCategorySelected = new Category();
    private Type mTypeSelected = new Type();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.ContentMainRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                mCategories.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        Category category = categorySnapshot.getValue(Category.class);
                        category.setUuid(categorySnapshot.getKey());
                        mCategories.add(category);
                    }
                    setUpCategoriesAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "No categories",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpCategoriesAdapter(){
        mMainItemAdapter = new MainItemAdapter(this, mCategories);
        mMainItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mMainItemAdapter);
    }
    private void setUpTypesAdapter(){
        mTypeItemAdapter = new TypeItemAdapter(this, mTypes);
        mTypeItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mTypeItemAdapter);
    }

    private void setUpProductsAdapter(){
        mProductItemAdapter = new ProductItemAdapter(this, mProducts);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(mProductItemAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCategoryItemClick(View view, int position) {
        //TODO: Aqui hacer el filtrado de los productos pasandole parametros segun la categoria que se haya elegido al siguiente adapter
        mCategorySelected = mMainItemAdapter.getItem(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Type")
                .orderByChild("Category")
                .equalTo(mCategorySelected.getUuid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTypes.clear();
                if (dataSnapshot.exists()) {
                    getSupportActionBar().setTitle(mCategorySelected.getName());
                    for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                        Type type = typeSnapshot.getValue(Type.class);
                        type.setUuid(typeSnapshot.getKey());
                        mTypes.add(type);
                    }
                    setUpTypesAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "No types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onTypeItemClick(View view, int position) {
        mTypeSelected = mTypeItemAdapter.getItem(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Product").orderByChild("Type").equalTo(mTypeSelected.getUuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProducts.clear();
                if (dataSnapshot.exists()) {
                    getSupportActionBar().setTitle(mTypeSelected.getName());
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        product.setUuid(productSnapshot.getKey());
                        mProducts.add(product);
                    }
                    setUpProductsAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "No products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mRecyclerView.getAdapter() == mMainItemAdapter){
            super.onBackPressed();

        } else if (mRecyclerView.getAdapter() == mTypeItemAdapter){
            getSupportActionBar().setTitle("Stock");
            mRecyclerView.setAdapter(mMainItemAdapter);

        } else if (mRecyclerView.getAdapter() == mProductItemAdapter){
            getSupportActionBar().setTitle(mCategorySelected.getName());
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setAdapter(mTypeItemAdapter);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mNavHeaderProfilePicture = findViewById(R.id.NavHeaderProfilePicture);
        Picasso.get()
                .load(RestockApp.ACTUAL_USER.getPhotoUrl())
                .error(R.drawable.logo_dummy_restock)
                .into(mNavHeaderProfilePicture);

        mNavHeaderProfileName = findViewById(R.id.NavHeaderProfileName);
        mNavHeaderProfileName.setText(RestockApp.ACTUAL_USER.getDisplayName());
        mNavHeaderProfileEmail = findViewById(R.id.NavHeaderProfileEmail);
        mNavHeaderProfileEmail.setText(RestockApp.ACTUAL_USER.getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            RestockApp.ACTUAL_USER = null;
            startActivity(new Intent(this, LandingActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
