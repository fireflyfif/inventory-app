package com.example.root.inventory_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.inventory_app.data.ItemContract;
import com.example.root.inventory_app.data.ItemContract.ItemEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Identifier for the item data loader
     */
    private static final int ITEM_LOADER = 0;

    ItemCursorAdapter mCursorAdapter;

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

        // Setup an Adapter to create a list for each row of item data in the Cursor.
        // There is no item data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ItemCursorAdapter(this, null);
        itemsList.setAdapter(mCursorAdapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                Log.v(LOG_TAG, "Current Item URI is " + currentItemUri);

                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });

        // Prepare the loader.
        getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);
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
                insertItem();
                return true;
            case R.id.action_delete_all_items:
                // Delete all items
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertItem() {
        // Create a ContentValues object where column names are the keys,
        // and Chair "FRANKLIN" is the new item with its values.
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_PICTURE, getString(R.string.dummy_picture_uri));
        values.put(ItemEntry.COLUMN_ITEM_TYPE, ItemEntry.ITEM_TYPE_CHAIRS);
        values.put(ItemEntry.COLUMN_ITEM_NAME, "FRANKLIN");
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 10);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER, "IKEA");
        values.put(ItemEntry.COLUMN_ITEM_INFORMATION, "Modern Chair from Ikea.");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 29.50);

        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
        Log.v(LOG_TAG, "Table items: " + newUri);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all items?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllItems();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllItems() {
        if (ItemEntry.CONTENT_URI != null) {
            int rowsDeleted = getContentResolver().delete(
                    ItemEntry.CONTENT_URI,
                    null,
                    null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting all items", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Items deleted", Toast.LENGTH_SHORT).show();
            }
        }
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
                ItemEntry.COLUMN_ITEM_PRICE};

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
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
