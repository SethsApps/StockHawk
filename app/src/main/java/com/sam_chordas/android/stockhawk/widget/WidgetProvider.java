package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.LineGraphActivity;

public class WidgetProvider extends AppWidgetProvider {

    public static final String GRAPH_STOCK_ACTION = "com.sam_chordas.android.stockhawk.widget.MyStocksWidgetProvider.GRAPH_STOCK_ACTION";
    public static final String EXTRA_STOCK        = "com.sam_chordas.android.stockhawk.widget.MyStocksWidgetProvider.EXTRA_STOCK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.widget_my_stocks);

        // Here we setup the intent which points to the WidgetRemoteViewsService which will
        // provide the views for this collection.
        Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(widget, serviceIntent);
        } else {
            setRemoteAdapterV11(widget, appWidgetId, serviceIntent);
        }

        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        widget.setEmptyView(R.id.widget_stock_list, R.id.empty_stock_list);

        Intent        clickIntent = new Intent(context, LineGraphActivity.class);
        PendingIntent clickPI     = PendingIntent.getActivity(context,
                                                              0,
                                                              clickIntent,
                                                              PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setPendingIntentTemplate(R.id.widget_stock_list, clickPI);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widget);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(@NonNull final RemoteViews views, @NonNull final Intent intent) {
        views.setRemoteAdapter(R.id.widget_stock_list, intent);
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(@NonNull final RemoteViews views, @NonNull final int appWidgetId, @NonNull final Intent intent) {
        views.setRemoteAdapter(appWidgetId, R.id.widget_stock_list, intent);
    }
}