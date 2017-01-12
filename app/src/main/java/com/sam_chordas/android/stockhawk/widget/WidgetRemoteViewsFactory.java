package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.model.Stock;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.ui.LineGraphActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the RemoteViews object to the collection widget through the getViewAt method.
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = WidgetRemoteViewsFactory.class.getSimpleName();

    private Context     mContext     = null;
    private int         mAppWidgetId;
    private Cursor      mStockCursor = null;
    private List<Stock> mStocks;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext     = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                          AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mStocks = new ArrayList<>(0);
    }

    @Override
    public void onDestroy() {
        mStocks = null;
    }

    @Override
    public void onDataSetChanged() {
        final long token = Binder.clearCallingIdentity();
        try {
            if (mStockCursor != null)
            {
                mStockCursor.close();
            }

            mStockCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);

            mStocks = new ArrayList<>(mStockCursor.getCount());
            String stockQuoteContentDescription = mContext.getResources().getString(R.string.select_for_stock_history);
            if (mStockCursor.moveToFirst()) {
                do {
                    mStocks.add(new Stock(
                            mStockCursor.getString(mStockCursor.getColumnIndex(QuoteColumns.SYMBOL)),
                            mStockCursor.getString(mStockCursor.getColumnIndex(QuoteColumns.BIDPRICE)),
                            Utils.showPercent ? mStockCursor.getString(mStockCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)) : mStockCursor.getString(mStockCursor.getColumnIndex(QuoteColumns.CHANGE)),
                            mStockCursor.getInt(mStockCursor.getColumnIndex(QuoteColumns.ISUP)) == 1,
                            stockQuoteContentDescription
                    ));
                } while (mStockCursor.moveToNext());
            }
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Error attempting to update stocks for widget.", ex);
        }
        finally {
            if (mStockCursor != null)
            {
                mStockCursor.close();
            }

            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public int getCount() {
        return mStocks.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);

        Stock stockToView = mStocks.get(position);

        row.setTextViewText(R.id.stock_symbol, stockToView.symbol);
        row.setTextViewText(R.id.bid_price,    stockToView.bidPrice);
        row.setTextViewText(R.id.change,       stockToView.change);
        if (stockToView.isUp) {
            row.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            row.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }
        row.setContentDescription(R.id.bid_price,
                stockToView.stockQuoteContentDescription
                        + " "
                        + String.format(mContext.getString(R.string.item_description_additional_info),
                                        stockToView.symbol,
                                        stockToView.bidPrice,
                                        stockToView.change)
                );

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putString(LineGraphActivity.STOCK_SYMBOL_ARG, stockToView.symbol);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        row.setOnClickFillInIntent(R.id.stock_quote, fillInIntent);

        return row;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}