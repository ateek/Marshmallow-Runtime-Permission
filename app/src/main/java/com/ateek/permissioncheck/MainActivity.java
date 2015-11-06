package com.ateek.permissioncheck;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private List<String> names = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private static final String permission = Manifest.permission.READ_CONTACTS;


    @Bind(R.id.list)
    ListView listView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, names);
        listView.setAdapter(adapter);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.btn_reload)
    void onClick() {
        if (Utils.checkPermission(this, permission)) {
            names = Utils.readContacts(this);
            adapter.addAll(names);
            adapter.notifyDataSetChanged();
        } else {
            this.requestPermissions(new String[]{permission},
                    REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted!
                    // Perform the action
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    names = Utils.readContacts(this);
                    adapter.addAll(names);
                    adapter.notifyDataSetChanged();
                } else {
                    // Permission was denied
                    // :(
                    // Gracefully handle the denial
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // Add additional cases for other permissions you may have asked for
        }
    }

}
