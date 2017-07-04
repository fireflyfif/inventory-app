package com.example.root.inventory_app.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by root on 7/3/17.
 */

public class ItemContract {

    public static final String CONTENT_AUTHORITY = "com.example.root.inventory_app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";

    private ItemContract() {}

    /**
     * Inner class that defines constant values for the items database table.
     * Each entry in the table represents a single item.
     */
    public static abstract class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        public static final String TABLE_NAME = "items";

        /**
         * Unique ID number for the item (only for use in the database table).
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Type of the item
         *
         * Type: TEXT
         */
        public static final String COLUMN_ITEM_TYPE = "item type";

        /**
         * Name of the item
         *
         * Type: TEXT
         */
        public static final String COLUMN_ITEM_NAME = "item name";

        /**
         * Quantity of the item
         *
         * Type: INTEGER
         */
        public static final String COLUMN_ITEM_QUANTITY = "quantity";

        /**
         * Supplier of the item
         *
         * Type: TEXT
         */
        public static final String COLUMN_ITEM_SUPPLIER = "supplier";

        /**
         * Picture of the item
         *
         * Type: BLOB ??
         */
        public static final String COLUMN_ITEM_PICTURE = "picture";

        /**
         * Information of the item
         *
         * Type: TEXT
         */
        public static final String COLUMN_ITEM_INFORMATION = "item information";

        /**
         * Price of the item
         *
         * Type: INTEGER or REAL ??
         */
        public static final String COLUMN_ITEM_PRICE = "price";
    }
}
