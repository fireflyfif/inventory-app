package com.example.root.inventory_app.data;

import android.content.ClipData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.root.inventory_app.data.ItemContract.ItemEntry;

/**
 * {@link ContentProvider} for Inventory App.
 */
public class ItemProvider extends ContentProvider {

    public static final String LOG_TAG = ItemProvider.class.getSimpleName();

    private static final int ITEMS = 100;

    private static final int ITEM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    private ItemDbHelper mItemDbHelper;

    @Override
    public boolean onCreate() {
        mItemDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments,
     * and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mItemDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // For the ITEMS case, query the items table directly with the given
                // projection, selection, selection arguments, and sort order.
                // The cursor could contain multiple rows of the pets table.
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                // For the ITEM_ID case, extract out the ID from the URI.
                // For an example URI such as
                // "content://com.example.root.inventory_app.items/items/3",
                // the selection will be "_id=?" and the selection argument will be a String array
                // containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, there is an element in the selection
                // arguments that will fill the "?".
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Perform a query on the items table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor.
        // so that the content URI the Cursor was created.
        // If the data at this URI changes, then the Cursor has to be updated.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert an item into the database with the given content values.
     *
     * @return the new content URI for the specific row in the database.
     */
    private Uri insertItem(Uri uri, ContentValues values) {
        // Get writable database.
        SQLiteDatabase database = mItemDbHelper.getWritableDatabase();

        // Check that the picture is not null.
//        String itemPicture = values.getAsString(ItemEntry.COLUMN_ITEM_PICTURE);
//        if (itemPicture == null) {
//            throw new IllegalArgumentException("Item requires a picture.");
//        }

        // Check that the name is not null
        String itemName = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
        if (itemName == null) {
            throw new IllegalArgumentException("Item requires a name.");
        }

        // Check that the type is not null
        Integer itemType = values.getAsInteger(ItemEntry.COLUMN_ITEM_TYPE);
        if (itemType == null || !ItemEntry.isValidType(itemType)) {
            Log.v(LOG_TAG, "Current integer for the category " + itemType);
            throw new IllegalArgumentException("Item requires valid type.");
        }

        // Check that the quantity is provided, check that it's greater than or equal to 0
        Integer itemQuantity = values.getAsInteger(ItemEntry.COLUMN_ITEM_QUANTITY);
        if (itemQuantity != null && itemQuantity < 0) {
            throw new IllegalArgumentException("Item requires valid quantity.");
        }

        // Check that the supplier is not null
//        String itemSupplier = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER);
//        if (itemSupplier == null) {
//            throw new IllegalArgumentException("Item requires a supplier.");
//        }

        // Check that the price is provided, check that it's greater then 0
        Integer itemPrice = values.getAsInteger(ItemEntry.COLUMN_ITEM_PRICE);
        if (itemPrice != null && itemPrice < 0) {
            throw new IllegalArgumentException("Item requires valid price.");
        }

        // No need to check the item information, any value is valid (including null).

        // Insert the new pet with the given values
        long id = database.insert(ItemEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the item content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, values, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link ItemEntry#COLUMN_ITEM_PICTURE} key is present,
        // check that the picture value is not null.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_PICTURE)) {
            String itemPicture = values.getAsString(ItemEntry.COLUMN_ITEM_PICTURE);
            if (itemPicture == null) {
                throw new IllegalArgumentException("Item requires a picture.");
            }
        }

        // If the {@link ItemEntry#COLUMN_ITEM_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_NAME)) {
            String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name.");
            }
        }

        // If the {@link ItemEntry#COLUMN_ITEM_TYPE} key is present,
        // check that the type value is not null.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_TYPE)) {
            Integer itemType = values.getAsInteger(ItemEntry.COLUMN_ITEM_TYPE);
            if (itemType == null || !ItemEntry.isValidType(itemType)) {
                throw new IllegalArgumentException("Item requires valid type.");
            }
        }

        // If the {@link ItemEntry#COLUMN_ITEM_QUANTITY} key is present,
        // check that it's greater than or equal to 0.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_QUANTITY)) {
            Integer itemQuantity = values.getAsInteger(ItemEntry.COLUMN_ITEM_QUANTITY);
            if (itemQuantity != null && itemQuantity < 0) {
                throw new IllegalArgumentException("Item requires valid quantity.");
            }
        }

        // If the {@link ItemEntry#COLUMN_ITEM_SUPPLIER} key is present,
        // check that the type value is not null.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_SUPPLIER)) {
            String itemSupplier = values.getAsString(ItemEntry.COLUMN_ITEM_SUPPLIER);
            if (itemSupplier == null) {
                throw new IllegalArgumentException("Item requires a supplier.");
            }
        }

        // If the {@link ItemEntry#COLUMN_ITEM_PRICE} key is present,
        // check that it's greater then 0.
        if (values.containsKey(ItemEntry.COLUMN_ITEM_PRICE)) {
            Integer itemPrice = values.getAsInteger(ItemEntry.COLUMN_ITEM_PRICE);
            if (itemPrice != null || itemPrice <= 0) {
                throw new IllegalArgumentException("Item requires valid price.");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Get writable database
        SQLiteDatabase database = mItemDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the given
        // URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mItemDbHelper.getWritableDatabase();

        // track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
