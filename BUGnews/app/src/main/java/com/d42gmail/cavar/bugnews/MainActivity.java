package com.d42gmail.cavar.bugnews;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    DatabaseHelper dbHelper=new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.listView);

        if(internetConnection(MainActivity.this)==true) {
            GetBugTask mt = new GetBugTask();
            mt.execute("http://www.bug.hr/rss/vijesti/");
        }
        else{

            bugArray=dbHelper.getResultDB();
            adapter=new BugAdapter(MainActivity.this,bugArray);
            for(Bug bug:bugArray)
            {
                Log.i(" ucitano: ", " Naslov: " + bug.getTitle() + " Opis: " + bug.getDescription() + " kategorija: " + bug.getCategory() + " Link: " + bug.getLink() + " Slika: " + bug.getImageurl());

            }
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Url = adapter.getItem(position).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);


            }
        });
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private class GetBugTask extends AsyncTask<String, Integer, ArrayList<Bug>> {

        int count=0;

        @Override
        protected ArrayList<Bug> doInBackground(String... params) {
            String url = params[0];
            ArrayList<Bug> list=new ArrayList<Bug>();

            try {
                URL u = new URL(url);
                HttpURLConnection connection =
                        (HttpURLConnection) u.openConnection();
                connection.setConnectTimeout(16000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                XmlPullParserFactory XmlFactory = XmlPullParserFactory.newInstance();
                XmlPullParser XmlParser = XmlFactory.newPullParser();
                Bug bug = null;
                XmlParser.setInput(input,null);
                int eventType = XmlParser.getEventType();
                boolean new_item = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {

                        case XmlPullParser.START_TAG:
                            String name = XmlParser.getName();
                            if (name.equals("item")) {
                                bug = new Bug();
                                new_item=true;
                                Log.i("se","item tag kreiraj novi objekt");
                            }
                            if(new_item==true) {
                                if (name.equals("title")) {
                                    bug.setTitle(XmlParser.nextText());
                                    Log.i("se", "title tag");

                                }
                                if (name.equals("category")) {
                                    bug.setCategory(XmlParser.nextText());
                                    Log.i("se", "category tag");
                                }
                                if (name.equals("description")) {
                                    bug.setDescription(XmlParser.nextText());
                                    Log.i("se", "description tag");
                                }

                                if (name.equals("link")) {
                                    bug.setLink(XmlParser.nextText());
                                    Log.i("se", "link tag");
                                }
                                if (name.equals("enclosure")) {
                                    Log.i("se","enclosure tag");


                                    bug.setImageurl(XmlParser.getAttributeValue(null, "url"));

                                }


                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = XmlParser.getName();
                            Log.i("se","provjeri end tag");
                            if (name.equals("item"))
                            {
                                list.add(bug);

                                new_item=false;
                            }
                            break;
                    }
                    Log.i("se","čitaj sljedeći tag");
                    eventType = XmlParser.next();
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;


        }

        protected void onPostExecute(ArrayList<Bug> result) {
            bugArray=result;
            adapter=new BugAdapter(MainActivity.this,bugArray);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            dbHelper.deleteDatabase(bugArray);
            setProgressBarVisibility(false);

            for(Bug bug : bugArray) {

                dbHelper.addData(bug, MainActivity.this);
            }
            Log.i("a", "radi");

            super.onPostExecute(result);
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

        if (id == R.id.home) {
            list.setAdapter(null);
            // Handle the camera action
                    adapter = new BugAdapter(getApplicationContext(),bugArray);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();




        } else if (id == R.id.hardver) {
         ucitaj("Hardver");


        } else if (id == R.id.softver) {
       ucitaj("Softver");

        } else if (id == R.id.mimapp) {
         ucitaj1("Mobiteli","Mobilne aplikacije");

        } else if (id == R.id.tig) {

           ucitaj1("Tehnologije","Gadgeti");

        }
        else if (id == R.id.biznis) {

            ucitaj("Biznis");

        }
        else if (id == R.id.zabava) {
            ucitaj("Zabava");

        }
        else if (id == R.id.obrazovanje) {
            ucitaj("Obrazovanje");

        }
        else if (id == R.id.Hakeri) {
            ucitaj("Hakeri");

        }
        else if (id == R.id.ostalo) {

            ostalo();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ostalo() {

        list.setAdapter(null);
        bugArray1.clear();
        for(Bug bugara:bugArray)
        {
            if((bugara.getCategory().equals("Hakeri"))||(bugara.getCategory().equals("Obrazovanje"))||(bugara.getCategory().equals("Zabava"))||(bugara.getCategory().equals("Biznis"))||
            (bugara.getCategory().equals("Tehnologije"))||(bugara.getCategory().equals("Gadgeti"))||(bugara.getCategory().equals("Mobiteli"))||(bugara.getCategory().equals("Mobilne aplikacije"))
            ||(bugara.getCategory().equals("Softver"))||(bugara.getCategory().equals("Hardver"))){

            }
            else{
            Log.i("ii", "" + bugara.getCategory());
            bugArray1.add(bugara);
            adapter = new BugAdapter(getApplicationContext(),bugArray1);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        }


    }

    private void ucitaj(String name) {
        list.setAdapter(null);
        bugArray1.clear();
        for(Bug bugara:bugArray)
        {
            if(bugara.getCategory().equals(name)){
                Log.i("ii", "" + bugara.getCategory());
                bugArray1.add(bugara);
                adapter = new BugAdapter(getApplicationContext(),bugArray1);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void ucitaj1(String name, String name1){

        list.setAdapter(null);
        bugArray1.clear();
        for(Bug bugara:bugArray)
        {
            if(((bugara.getCategory().equals(name)))||((bugara.getCategory().equals(name1)))){
                Log.i("ii", "" + bugara.getCategory());
                bugArray1.add(bugara);
                adapter = new BugAdapter(getApplicationContext(),bugArray1);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public boolean internetConnection( Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
