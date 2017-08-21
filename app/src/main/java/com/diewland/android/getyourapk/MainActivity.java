package com.diewland.android.getyourapk;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lv ;
    private ArrayAdapter<String> listAdapter ;
    private HashMap<String, String> appHash = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ignore detect file URI exposure
        // https://stackoverflow.com/a/40674771/466693
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Find the ListView resource.
        lv = (ListView) findViewById( R.id.list_row );

        // Collect all apps
        PackageManager pm = getPackageManager();
        for (ApplicationInfo app : pm.getInstalledApplications(0)) {
            String app_name = (String)pm.getApplicationLabel(app);
            appHash.put(app.publicSourceDir, app_name);
        }

        // Sort by app_name
        ArrayList appNames = new ArrayList<String>(appHash.values());
        Collections.sort(appNames, String.CASE_INSENSITIVE_ORDER);

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, appNames);

        // Set the ArrayAdapter as the ListView's adapter.
        lv.setAdapter( listAdapter );

        // Save APK when click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int pos, long l) {
                String curAppName = ((TextView)v).getText().toString();
                for(String apkPath : appHash.keySet()){
                    if(appHash.get(apkPath).equals(curAppName)){
                        File file = new File(apkPath);
                        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intent.setType("file/*");
                        startActivity(intent);
                    }
                }
            }
        });

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
