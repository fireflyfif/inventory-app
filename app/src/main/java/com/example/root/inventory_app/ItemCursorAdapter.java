package com.example.root.inventory_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.inventory_app.data.ItemContract;
import com.example.root.inventory_app.data.ItemContract.ItemEntry;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 7/5/17.
 */

public class ItemCursorAdapter extends CursorAdapter {

    private static final Uri DUMMY_PICTURE_URI = Uri.parse("android.resource://com.example.root.inventory_app/drawable/news_image_2;\n");
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
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();

        // Find the columns of item attributes
        final long id = cursor.getLong(cursor.getColumnIndex(ItemEntry._ID));
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
        final int quantity = cursor.getInt(quantityColumnIndex);
        holder.itemQuantity.setText(Integer.toString(quantity));

        // Set the text to "In stock" when quantity is more then 0 and
        // "Out of stock" if it's 0
        if (quantity == 0) {
            holder.itemInStock.setText(R.string.out_of_stock);
            holder.itemInStock.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.itemInStock.setText(R.string.in_stock);
            holder.itemInStock.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        // Update the TextViews and ImageView with the attributes for the current item
        // Use dummy picture as a placeholder when there is no image provided
        // Not working
        holder.itemPicture.setImageURI(pictureUri);
        // What does this do?
        holder.itemPicture.invalidate();
        if (holder.itemPicture == null) {
            holder.itemPicture.setImageURI(DUMMY_PICTURE_URI);
        }
        holder.itemName.setText(name);
        holder.itemType.setText(Integer.toString(type));
        if (type == ItemEntry.ITEM_TYPE_OTHER) {
            holder.itemType.setText(R.string.spinner_other);
        } else if (type == ItemEntry.ITEM_TYPE_SOFAS) {
            holder.itemType.setText(R.string.spinner_sofas);
        } else if (type == ItemEntry.ITEM_TYPE_CHAIRS) {
            holder.itemType.setText(R.string.spinner_chairs);
        } else if (type == ItemEntry.ITEM_TYPE_TABLES) {
            holder.itemType.setText(R.string.spinner_tables);
        } else if (type == ItemEntry.ITEM_TYPE_BEDS) {
            holder.itemType.setText(R.string.spinner_beds);
        } else if (type == ItemEntry.ITEM_TYPE_DESKS) {
            holder.itemType.setText(R.string.spinner_desks);
        } else if (type == ItemEntry.ITEM_TYPE_CABINETS) {
            holder.itemType.setText(R.string.spinner_cabinets);
        } else if (type == ItemEntry.ITEM_TYPE_WARDROBES) {
            holder.itemType.setText(R.string.spinner_wardrobes);
        } else if (type == ItemEntry.ITEM_TYPE_TEXTILES) {
            holder.itemType.setText(R.string.spinner_textiles);
        } else {
            holder.itemType.setText(R.string.spinner_decoration);
        }
        holder.itemPrice.setText(Double.toString(price));

        // Decrease the quantity of the items with 1 when click the Sell Button
        holder.sellItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    int newItemQuantity = quantity - 1;

                    Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                    ContentValues values = new ContentValues();
                    values.put(ItemEntry.COLUMN_ITEM_QUANTITY, newItemQuantity);
                    context.getContentResolver().update(currentItemUri, values, null, null);

                    holder.itemQuantity.setText(Integer.toString(newItemQuantity));
                }
            }
        });
    }

    private class ViewHolder {
        ImageView itemPicture;
        TextView itemName;
        TextView itemType;
        TextView itemPrice;
        TextView itemInStock;
        TextView itemQuantity;
        Button sellItemButton;

        private ViewHolder(View view) {
            itemPicture = (ImageView) view.findViewById(R.id.item_picture);
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemType = (TextView) view.findViewById(R.id.item_type);
            itemPrice = (TextView) view.findViewById(R.id.item_price);
            itemInStock = (TextView) view.findViewById(R.id.item_in_stock);
            itemQuantity = (TextView) view.findViewById(R.id.item_quantity);
            sellItemButton = (Button) view.findViewById(R.id.sell_button);
        }
    }
}
