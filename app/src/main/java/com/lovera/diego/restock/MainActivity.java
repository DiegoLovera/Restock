package com.lovera.diego.restock;

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

import com.lovera.diego.restock.adapters.MainItemAdapter;
import com.lovera.diego.restock.adapters.ProductItemAdapter;
import com.lovera.diego.restock.adapters.TypeItemAdapter;
import com.lovera.diego.restock.models.Category;
import com.lovera.diego.restock.models.Product;
import com.lovera.diego.restock.models.Type;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainItemAdapter.ItemClickListener, TypeItemAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private MainItemAdapter mMainItemAdapter;
    private TypeItemAdapter mTypeItemAdapter;
    private ProductItemAdapter mProductItemAdapter;
    private ImageView mNavHeaderProfilePicture;
    private TextView mNavHeaderProfileName;
    private TextView mNavHeaderProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region llenado de categorias
        Category category1 = new Category();
        category1.setName("Beer");
        Category category2 = new Category();
        category2.setName("Wine");
        Category category3 = new Category();
        category3.setName("Ice");

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        //endregion


        mRecyclerView = findViewById(R.id.ContentMainRecyclerView);
        int numberOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mMainItemAdapter = new MainItemAdapter(this, categories);
        mMainItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mMainItemAdapter);



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

    @Override
    public void onCategoryItemClick(View view, int position) {
        //TODO: Aqui hacer el filtrado de los productos pasandole parametros segun la categoria que se haya elegido al siguiente adapter
        mMainItemAdapter.getItem(position);
        Type type1 = new Type();
        type1.setName("Handcraft");
        Type type2 = new Type();
        type2.setName("Oscura");
        Type type3 = new Type();
        type3.setName("Clara");
        ArrayList<Type> types = new ArrayList<>();
        types.add(type1);
        types.add(type2);
        types.add(type3);
        mTypeItemAdapter = new TypeItemAdapter(this, types);
        mTypeItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mTypeItemAdapter);
    }

    @Override
    public void onTypeItemClick(View view, int position) {
        mTypeItemAdapter.getItem(position);
        //region llenado de productos
        Product product1 = new Product();
        product1.setName("Sol");
        product1.setPrice("300");
        product1.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus porta enim vitae enim luctus iaculis. Donec pretium placerat volutpat. ");
        Product product2 = new Product();
        product2.setName("Corona");
        product2.setPrice("200");
        product2.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus porta enim vitae enim luctus iaculis. Donec pretium placerat volutpat. ");
        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        //endregion
        mProductItemAdapter = new ProductItemAdapter(this, products);
        mRecyclerView.setAdapter(mProductItemAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mRecyclerView.getAdapter() == mMainItemAdapter){
            super.onBackPressed();
            mRecyclerView.setAdapter(mMainItemAdapter);
        } else if (mRecyclerView.getAdapter() == mTypeItemAdapter){
            mRecyclerView.setAdapter(mMainItemAdapter);
        } else if (mRecyclerView.getAdapter() == mProductItemAdapter){
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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
