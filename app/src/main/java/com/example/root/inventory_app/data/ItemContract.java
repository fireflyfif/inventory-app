package com.example.root.inventory_app.data;

import android.content.ContentResolver;
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

        public static boolean isValidType(int type) {
            if (type == ITEM_TYPE_SOFAS || type == ITEM_TYPE_CHAIRS ||
                    type == ITEM_TYPE_TABLES || type == ITEM_TYPE_BEDS ||
                    type == ITEM_TYPE_DESKS || type == ITEM_TYPE_CABINETS ||
                    type == ITEM_TYPE_WARDROBES || type == ITEM_TYPE_TEXTILES ||
                    type == ITEM_TYPE_DECORATION || type == ITEM_TYPE_OTHER) {
                return true;
            }
            return false;
        }

        /** The content URI to access the item data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /** Name of the database table for items */
        public static final String TABLE_NAME = "items";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

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
        public static final String COLUMN_ITEM_TYPE = "item_type";

        /**
         * Name of the item
         *
         * Type: TEXT
         */
        public static final String COLUMN_ITEM_NAME = "item_name";

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
        public static final String COLUMN_ITEM_SUPPLIER = "supplier_name";

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
        public static final String COLUMN_ITEM_INFORMATION = "item_information";

        /**
         * Price of the item
         *
         * Type: FLOAT or REAL
         */
        public static final String COLUMN_ITEM_PRICE = "price";

        /**
         * Possible values for the type of the items
         */
        public static final int ITEM_TYPE_OTHER = 0;
        public static final int ITEM_TYPE_SOFAS = 1;
        public static final int ITEM_TYPE_CHAIRS = 2;
        public static final int ITEM_TYPE_TABLES = 3;
        public static final int ITEM_TYPE_BEDS = 4;
        public static final int ITEM_TYPE_DESKS = 5;
        public static final int ITEM_TYPE_CABINETS = 6;
        public static final int ITEM_TYPE_WARDROBES = 7;
        public static final int ITEM_TYPE_TEXTILES = 8;
        public static final int ITEM_TYPE_DECORATION = 9;
    }
}
