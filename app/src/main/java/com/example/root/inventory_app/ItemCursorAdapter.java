package com.example.root.inventory_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.inventory_app.data.ItemContract;
import com.example.root.inventory_app.data.ItemContract.ItemEntry;

import org.w3c.dom.Text;

/**
 * Created by root on 7/5/17.
 */

public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved
     *                to the correct position.
     * @param parent  The parent to which the new view is attached to.
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        ImageView itemPicture = (ImageView) view.findViewById(R.id.item_picture);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView itemType = (TextView) view.findViewById(R.id.item_type);
        TextView itemPrice = (TextView) view.findViewById(R.id.item_price);
        TextView itemInStock = (TextView) view.findViewById(R.id.item_in_stock);
        TextView itemQuantity = (TextView) view.findViewById(R.id.item_quantity);

        // Find the columns of item attributes
        int pictureColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PICTURE);
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int typeColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_TYPE);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

        // Read the item attributes from the Cursor for the current item
        String pictureString = cursor.getString(pictureColumnIndex);
        Uri pictureUri = Uri.parse(pictureString);
        String name = cursor.getString(nameColumnIndex);
        int type = cursor.getInt(typeColumnIndex);
        double price = cursor.getDouble(priceColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        // If the item type string is empty of null, then use some default text
        // that says "unknown type", so the TextView isn't blank.
//        if (TextUtils.isEmpty(type)) {
//            type = context.getString(R.string.unknown_type);
//        }

        // Update the TextViews with the attributes for the current item
        itemPicture.setImageURI(pictureUri);
        // What does this do?
        itemPicture.invalidate();
        itemName.setText(name);
        itemType.setText(Integer.toString(type));
        if (type == ItemEntry.ITEM_TYPE_OTHER) {
            itemType.setText(R.string.spinner_other);
        } else if (type == ItemEntry.ITEM_TYPE_SOFAS) {
            itemType.setText(R.string.spinner_sofas);
        } else if (type == ItemEntry.ITEM_TYPE_CHAIRS) {
            itemType.setText(R.string.spinner_chairs);
        } else if (type == ItemEntry.ITEM_TYPE_TABLES) {
            itemType.setText(R.string.spinner_tables);
        } else if (type == ItemEntry.ITEM_TYPE_BEDS) {
            itemType.setText(R.string.spinner_beds);
        } else if (type == ItemEntry.ITEM_TYPE_DESKS) {
            itemType.setText(R.string.spinner_desks);
        } else if (type == ItemEntry.ITEM_TYPE_CABINETS) {
            itemType.setText(R.string.spinner_cabinets);
        } else if (type == ItemEntry.ITEM_TYPE_WARDROBES) {
            itemType.setText(R.string.spinner_wardrobes);
        } else if (type == ItemEntry.ITEM_TYPE_TEXTILES) {
            itemType.setText(R.string.spinner_textiles);
        } else {
            itemType.setText(R.string.spinner_decoration);
        }
        itemPrice.setText(Double.toString(price));
        itemQuantity.setText(quantity);
    }
}
