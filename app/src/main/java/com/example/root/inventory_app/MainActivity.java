package com.example.root.inventory_app;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.root.inventory_app.data.ItemContract;
import com.example.root.inventory_app.data.ItemContract.ItemEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the item data loader */
    private static final int ITEM_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Open the DetailActivity by clicking on the FAB Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the items data
        ListView itemsList = (ListView) findViewById(R.id.list_view);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items
        View emptyView = findViewById(R.id.empty_view);
        itemsList.setEmptyView(emptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                // Do insertion
                return true;
            case R.id.action_delete_all_items:
                // Delete all items
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the items table.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_PICTURE,
                ItemEntry.COLUMN_ITEM_TYPE,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ItemCursorAdapter} with this new cursor containing updated items data
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
