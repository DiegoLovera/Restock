package com.lovera.diego.restock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.lovera.diego.restock.adapters.CategoryItemAdapter;
import com.lovera.diego.restock.adapters.ProductItemAdapter;
import com.lovera.diego.restock.adapters.TypeItemAdapter;
import com.lovera.diego.restock.common.ImageRoundCorners;
import com.lovera.diego.restock.models.Category;
import com.lovera.diego.restock.models.Product;
import com.lovera.diego.restock.models.Type;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CategoryItemAdapter.ItemClickListener,
        TypeItemAdapter.ItemClickListener
{

    //region Fields
    private RecyclerView mRecyclerView;
    private CategoryItemAdapter mCategoryItemAdapter;
    private TypeItemAdapter mTypeItemAdapter;
    private ProductItemAdapter mProductItemAdapter;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ArrayList<Category> mCategories = new ArrayList<>();
    private ArrayList<Type> mTypes = new ArrayList<>();
    private ArrayList<Product> mProducts = new ArrayList<>();
    private Category mCategorySelected = new Category();
    private Type mTypeSelected = new Type();
    //endregion

    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //region Toolbar setup
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //endregion
        //region FloatingActionButton setup
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!RestockApp.ACTUAL_ORDER_CONTENT.isEmpty()) {
                    startActivity(new Intent(view.getContext(), CartActivity.class));
                } else {
                    Snackbar.make(view, R.string.message_cart_empty, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        //endregion
        //region RecyclerView setup
        mRecyclerView = findViewById(R.id.recycler_view_main_activity_content);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //endregion
        //region RecyclerView CategoryAdapter
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategories.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()){
                        Category category = categorySnapshot.getValue(Category.class);
                        if (category != null) {
                            category.setUuid(categorySnapshot.getKey());
                            mCategories.add(category);
                        }
                    }
                    setUpCategoriesAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_out_of_stock,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //endregion
        //region DrawerLayout setup
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //endregion
        //region NavigationView setup
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        //endregion

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null){
            //insert to the database
            if (RestockApp.ACTUAL_USER != null){
                FirebaseDatabase.getInstance().getReference("User")
                        .child(RestockApp.ACTUAL_USER.getUid())
                        .child("Token").setValue(refreshedToken);
            }
        }
    }
    //endregion
    //region onStart
    @Override
    protected void onStart() {
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
        super.onStart();
    }
    //endregion
    //region onBackPressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mRecyclerView.getAdapter() == mCategoryItemAdapter){
            //TODO: Remplazar por una confirmación de querer salir y cerrar sesión
        } else if (mRecyclerView.getAdapter() == mTypeItemAdapter){
            mToolbar.setTitle(R.string.activity_main_title);
            mRecyclerView.setAdapter(mCategoryItemAdapter);
        } else if (mRecyclerView.getAdapter() == mProductItemAdapter){
            mToolbar.setTitle(mCategorySelected.getName());
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setAdapter(mTypeItemAdapter);
        }
    }
    //endregion
    //region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        ImageView mNavHeaderProfilePicture = findViewById(R.id.NavHeaderProfilePicture);
        Picasso.get()
                .load(RestockApp.ACTUAL_USER.getPhotoUrl())
                .resize(150, 150)
                .transform(new ImageRoundCorners())
                .error(R.drawable.ic_launcher_background)
                .into(mNavHeaderProfilePicture);

        TextView mNavHeaderProfileName = findViewById(R.id.NavHeaderProfileName);
        mNavHeaderProfileName.setText(RestockApp.ACTUAL_USER.getDisplayName());
        TextView mNavHeaderProfileEmail = findViewById(R.id.NavHeaderProfileEmail);
        mNavHeaderProfileEmail.setText(RestockApp.ACTUAL_USER.getEmail());
        return true;
    }
    //endregion
    //region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
    //region onNavigationItemSelected
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileInformationActivity.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            RestockApp.ACTUAL_USER = null;
            startActivity(new Intent(this, LandingActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_privacy) {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://momcare.online/Privacy/terminosycondiciones.html"));
            startActivity(i);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //endregion
    //region onCategoryItemClick
    @Override
    public void onCategoryItemClick(View view, int position) {
        //TODO: Aqui hacer el filtrado de los productos pasandole parametros segun la categoria que se haya elegido al siguiente adapter
        mCategorySelected = mCategoryItemAdapter.getItem(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Type")
                .orderByChild("Category")
                .equalTo(mCategorySelected.getUuid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTypes.clear();
                if (dataSnapshot.exists()) {
                    mToolbar.setTitle(mCategorySelected.getName());
                    for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                        Type type = typeSnapshot.getValue(Type.class);
                        if (type != null) {
                            type.setUuid(typeSnapshot.getKey());
                        }
                        mTypes.add(type);
                    }
                    setUpTypesAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_out_of_stock,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //endregion
    //region onTypeItemClick
    @Override
    public void onTypeItemClick(View view, int position) {
        mTypeSelected = mTypeItemAdapter.getItem(position);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Product")
                .orderByChild("Type")
                .equalTo(mTypeSelected.getUuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProducts.clear();
                if (dataSnapshot.exists()) {
                    mToolbar.setTitle(mTypeSelected.getName());
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null) {
                            product.setUuid(productSnapshot.getKey());
                        }
                        mProducts.add(product);
                    }
                    setUpProductsAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_out_of_stock,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //endregion

    //region setUpCategoriesAdapter
    private void setUpCategoriesAdapter(){
        mCategoryItemAdapter = new CategoryItemAdapter(this, mCategories);
        mCategoryItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mCategoryItemAdapter);
    }
    //endregion
    //region setUpTypesAdapter
    private void setUpTypesAdapter(){
        mTypeItemAdapter = new TypeItemAdapter(this, mTypes);
        mTypeItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mTypeItemAdapter);
    }
    //endregion
    //region setuUpProductsAdapter
    private void setUpProductsAdapter(){
        mProductItemAdapter = new ProductItemAdapter(this, mProducts);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRecyclerView.setAdapter(mProductItemAdapter);
    }
    //endregion
}
