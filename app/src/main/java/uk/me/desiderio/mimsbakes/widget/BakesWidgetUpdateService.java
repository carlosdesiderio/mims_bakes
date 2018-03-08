package uk.me.desiderio.mimsbakes.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import uk.me.desiderio.mimsbakes.R;

/**
 * Intent Service to update widget when ingredients
 * are added or remove from shopping list
 */

public class BakesWidgetUpdateService extends IntentService {

    public static final String WIDGET_UPDATE_SERVICE_NAME =
            "BakesWidgetUpdateService_name";
    public static final String ACTION_INGREDIENT_UPDATE =
            "action_bakes";
    private static final String TAG =
            BakesWidgetUpdateService.class.getSimpleName();

    public BakesWidgetUpdateService() {
        super(WIDGET_UPDATE_SERVICE_NAME);
    }

    public static void startActionUpdateShoppingList(Context context) {
        Intent intent = new Intent(context, BakesWidgetUpdateService.class);
        intent.setAction(ACTION_INGREDIENT_UPDATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INGREDIENT_UPDATE.equals(action)) {
                handleActionIngredientUpdate();
            }
        }
    }

    private void handleActionIngredientUpdate() {
        Log.d(TAG, "handleActionIngredientUpdate: ");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, BakesAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,
                                                        R.id.widget_list_view);
    }
}