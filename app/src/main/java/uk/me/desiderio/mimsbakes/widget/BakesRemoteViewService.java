package uk.me.desiderio.mimsbakes.widget;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import uk.me.desiderio.mimsbakes.R;
import uk.me.desiderio.mimsbakes.data.BakesContentProvider;
import uk.me.desiderio.mimsbakes.data.BakesContract.IngredientEntry;
import uk.me.desiderio.mimsbakes.data.BakesContract.ShoppingEntry;
import uk.me.desiderio.mimsbakes.view.StringUtils;

import static uk.me.desiderio.mimsbakes.MainActivity.EXTRA_RECIPE_ID;

/**
 * Provides {@link RemoteViews} to the application's widget
 */

public class BakesRemoteViewService extends RemoteViewsService {
    private static final String TAG = BakesRemoteViewService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakesRemoteViewFactory(this.getApplicationContext(), intent);
    }

    class BakesRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        Context applicationContext;
        Cursor cursor;

        public BakesRemoteViewFactory(Context applicationContext, Intent intent) {
            this.applicationContext = applicationContext;
        }

        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate: ");
            getContentResolver().registerContentObserver(ShoppingEntry.CONTENT_URI,
                    false,
                    objectObserver);
        }

        @Override
        public void onDataSetChanged() {
            if(cursor != null) {
                cursor.close();
            }

            String selection = BakesContentProvider.SELECTION_SHOPPING_LIST_INGREDIENTS;

            cursor = getContentResolver().query(
                    IngredientEntry.CONTENT_URI,
                    null,
                    selection,
                    null,
                    null);
        }

        private ContentObserver objectObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                Log.d(TAG, "onChange: ");
                super.onChange(selfChange);
                onDataSetChanged();
            }
        };

        @Override
        public void onDestroy() {
            Log.d(TAG, "onDestroy: ");
            getContentResolver().unregisterContentObserver(objectObserver);
        }

        @Override
        public int getCount() {
            if(cursor == null) {
                return 0;
            }
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "getViewAt: " + position);
            if(cursor == null || cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToPosition(position);

            String name = cursor.getString(cursor.getColumnIndex
                    (IngredientEntry.COLUMN_NAME_INGREDIENT_NAME));
            float quantity = cursor.getFloat(cursor.getColumnIndex
                    (IngredientEntry.COLUMN_NAME_INGREDIENT_QUANTITY));
            String meassure = cursor.getString(cursor
                    .getColumnIndex(IngredientEntry.COLUMN_NAME_INGREDIENT_MEASURE));
            int recipeId = cursor.getInt(cursor
                    .getColumnIndex(IngredientEntry.COLUMN_RECIPE_FOREING_KEY));

            String ingredientFormattedString = StringUtils.getFormatedIngredientStringForShopping(name,
                    quantity, meassure);

            RemoteViews views = new RemoteViews(applicationContext.getPackageName(),
                    R.layout.widget_ingredient_list_item_layout);


            views.setTextViewText(R.id.widget_ingredient_list_item_text_view, ingredientFormattedString);

            Bundle extras = new Bundle();
            extras.putInt(EXTRA_RECIPE_ID, recipeId);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.widget_ingredient_list_item_text_view,fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
