package com.example.bdo.androidbdoleave;

/**
 * Created by suhe on 13/02/16.
 */
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentTransaction;

import com.example.bdo.androidbdoleave.helpers.Auth;

public class MainActivity extends AppCompatActivity {
    //defining variabel
    private Toolbar toolbar;
    private NavigationView nav;
    private DrawerLayout drawerLayout;
    private Auth auth;
    public TextView tvEmail,tvName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /**
         * Declaration of Component
         */
        auth = new Auth(getApplicationContext());
        tvEmail = (TextView) findViewById(R.id.email);
        tvEmail.setText(auth.getName().toString());
        tvName.setText(auth.getName().toString());
        /**
         *  Check Auth Login
         */

        if(!auth.isLogin()){
            toPageLogin();
        }
        /**
         * Android First Layout Launcher
         * Default Fragment
         */
        MyLeaveActivity fragment = new MyLeaveActivity();
        FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
        fragmentTrans.replace(R.id.frame, fragment);
        fragmentTrans.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.navigation_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.myleave :
                        Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
                        MyLeaveActivity fragment = new MyLeaveActivity();
                        FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
                        fragmentTrans.replace(R.id.frame, fragment);
                        fragmentTrans.commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionDrawerToogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionDrawerToogle);
        actionDrawerToogle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_logout :
                toPageLogin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toPageLogin() {
        auth.logout();
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
