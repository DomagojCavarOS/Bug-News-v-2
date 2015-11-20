package com.d42gmail.cavar.bugnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView list;
    ArrayList<Bug> bugArray;
    ArrayList<Bug> bugArray1=new ArrayList<Bug>();
    BugAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list= (ListView) findViewById(R.id.listView);
        adapter=new BugAdapter(MainActivity.this,bugArray);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        GetBugTask mt = new GetBugTask();
        mt.execute("http://www.bug.hr/rss/vijesti/");



    }

    private class GetBugTask extends AsyncTask<String, Integer, ArrayList<Bug>> {

        @Override
        protected ArrayList<Bug> doInBackground(String... params) {
            String url = params[0];
            ArrayList<Bug> list = getDataFromWeb(url);
            return list;
        }
        protected void onPostExecute(ArrayList<Bug> result) {
            bugArray=result;
            adapter=new BugAdapter(MainActivity.this,bugArray);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.i("a", "radi");

            super.onPostExecute(result);
        }

        private ArrayList<Bug> getDataFromWeb(String url) {
            ArrayList<Bug> list = new ArrayList<Bug>();
            try {
                URL u = new URL(url);
                HttpURLConnection connection =
                        (HttpURLConnection) u.openConnection();
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                XmlPullParserFactory XmlFactory = XmlPullParserFactory.newInstance();
                XmlPullParser XmlParser = XmlFactory.newPullParser();
                Bug n = null;
                XmlParser.setInput(input,null);
                int eventType = XmlParser.getEventType();
                boolean new_item = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {



                        case XmlPullParser.START_TAG:
                            String name = XmlParser.getName();
                            if (name.equals("item")) {
                                n = new Bug();
                                new_item=true;
                            }
                            if(new_item) {
                                if (name.equals("title")) {
                                    //Log.d("TAG_Title",XmlParser.nextText());
                                    n.setTitle(XmlParser.nextText());

                                }
                                if (name.equals("category")) {
                                    //Log.d("TAG_Category",XmlParser.nextText());
                                    n.setCategory(XmlParser.nextText());
                                }
                                if (name.equals("description")) {
                                    //Log.d("TAG_Description",XmlParser.nextText());
                                    n.setDescription(XmlParser.nextText());
                                }

                                if (name.equals("link")) {
                                    //Log.d("TAG_link", XmlParser.nextText());
                                    n.setLink(XmlParser.nextText());
                                }
                                if (name.equals("enclosure")) {
                                    //Log.d("TAG_image", XmlParser.nextText());


                                    n.setImageurl(XmlParser.getAttributeValue(null, "url"));

                                }


                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = XmlParser.getName();
                            if (name.equals("item"))
                            {
                                list.add(n);
                                new_item=false;
                            }
                            break;
                    }
                    eventType = XmlParser.next();
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            list.setAdapter(null);
            // Handle the camera action
                    adapter = new BugAdapter(getApplicationContext(),bugArray);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();



        } else if (id == R.id.nav_gallery) {
            list.setAdapter(null);
            bugArray1.clear();
            for(Bug bugara:bugArray)
            {Log.i("ii", "" + bugara.getCategory());
                if(bugara.getCategory().equals("Hardver")){
                    Log.i("ii", "" + bugara.getCategory());
                    bugArray1.add(bugara);
                    adapter = new BugAdapter(getApplicationContext(),bugArray1);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }


        } else if (id == R.id.nav_slideshow) {
        list.setAdapter(null);
            bugArray1.clear();
            for(Bug bugara:bugArray)
            {
                if(bugara.getCategory().equals("Softver")){
                    Log.i("ii", "" + bugara.getCategory());
                    bugArray1.add(bugara);
                    adapter = new BugAdapter(getApplicationContext(),bugArray1);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

        } else if (id == R.id.nav_manage) {
            list.setAdapter(null);
            bugArray1.clear();
            for(Bug bugara:bugArray)
            {
                if(((bugara.getCategory().equals("Mobiteli")))||((bugara.getCategory().equals("Mobilne aplikacije")))){
                    Log.i("ii", "" + bugara.getCategory());
                    bugArray1.add(bugara);
                    adapter = new BugAdapter(getApplicationContext(),bugArray1);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
