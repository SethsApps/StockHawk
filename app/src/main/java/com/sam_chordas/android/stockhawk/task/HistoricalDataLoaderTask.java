package com.sam_chordas.android.stockhawk.task;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.interfaces.IHandleItems;
import com.sam_chordas.android.stockhawk.model.HistoricalData;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.service.YahooFinanceService;
import com.sam_chordas.android.stockhawk.utility.RetrofitHelper;

public class HistoricalDataLoaderTask extends AsyncTask<HistoricalDataLoaderTask.LoaderArgs, Void, List<Quote>> {

    private static String               LOG_TAG         = HistoricalDataLoaderTask.class.getSimpleName();
    private        AppCompatActivity    mCaller;
    private        IHandleItems<Quote>  mItemHandler;

    public static void Run(AppCompatActivity caller, IHandleItems<Quote> itemHandler, String symbol)
    {
        if (symbol.equals(null) || symbol.isEmpty())
        {
            Log.d(LOG_TAG, "Unable to load historical data since stock symbol is invalid.");
            return;
        }
        String baseUrl = caller.getString(R.string.base_url_yahoo_finance);

        Calendar threeWeeksAgo = Calendar.getInstance();
        threeWeeksAgo.add(Calendar.WEEK_OF_MONTH, -3);
        Calendar rightNow = Calendar.getInstance();

        HistoricalDataLoaderTask            loaderTask = new HistoricalDataLoaderTask(caller, itemHandler);
        HistoricalDataLoaderTask.LoaderArgs args       = loaderTask.new LoaderArgs(baseUrl,
                symbol,
                threeWeeksAgo.getTime(),
                rightNow.getTime());

        loaderTask.execute(args);
    }

    public HistoricalDataLoaderTask(AppCompatActivity caller, IHandleItems<Quote> itemHandler) {
        mCaller      = caller;
        mItemHandler = itemHandler;
    }

    @Override
    protected List<Quote> doInBackground(LoaderArgs... params) {

        LoaderArgs args = params[0];

        final YahooFinanceService yahooFinanceApi = YahooFinanceService.Factory.create(args.getYahooFinanceBaseUrl());

        HistoricalData historicalData
                = RetrofitHelper.ExecuteCall(
                                                yahooFinanceApi.getHistoricalData(args.getQuery(),
                                                                                  args.getFormat(),
                                                                                  args.getEnv())
                                            );

        if (historicalData == null) {
            Log.d(LOG_TAG, "response from getHistoricalData() call is null");
            return null;
        }

        if (historicalData.getQuery() == null) {
            Log.d(LOG_TAG, "query property of response from getHistoricalData() call is null");
            return null;
        }

        if (historicalData.getQuery().getResults() == null) {
            Log.d(LOG_TAG, "results property of query property of response from getHistoricalData() call is null");
            return null;
        }

        if (historicalData.getQuery().getResults().getQuote() == null) {
            Log.d(LOG_TAG, "quote property of results property of query property of response from getHistoricalData() call is null");
            return null;
        }

        return historicalData.getQuery().getResults().getQuote();
    }

    @Override
    protected void onPostExecute(List<Quote> historicalData) {
        super.onPostExecute(historicalData);
        if (historicalData != null) Collections.reverse(historicalData);
        mItemHandler.handleItems(historicalData);
    }

    public class LoaderArgs
    {
        private static final String QUERY_FORMAT = "select * from yahoo.finance.historicaldata where symbol=\"%s\" and startDate=\"%s\" and endDate=\"%s\"";

        private String mYahooFinanceBaseUrl;
        private String mSymbol;
        private String mStartDate;
        private String mEndDate;
        private String mFormat;
        private String mEnv;

        public String getYahooFinanceBaseUrl() {
            return mYahooFinanceBaseUrl;
        }
        public String getSymbol() {
            return mSymbol;
        }
        public String getStartDate() {
            return mStartDate;
        }
        public String getEndDate() {
            return mEndDate;
        }
        public String getQuery() {
            return String.format(QUERY_FORMAT, getSymbol(), getStartDate(), getEndDate());
        }
        public String getFormat() { return mFormat; }
        public String getEnv() { return mEnv; }

        public LoaderArgs(String yahooFinanceBaseUrl, String symbol, Date startDate, Date endDate) {
            this.mYahooFinanceBaseUrl = yahooFinanceBaseUrl;
            this.mSymbol              = symbol;
            this.mStartDate           = DateFormat.format("yyyy-MM-dd", startDate).toString();
            this.mEndDate             = DateFormat.format("yyyy-MM-dd", endDate).toString();
            this.mFormat              = "json";
            this.mEnv                 = "store://datatables.org/alltableswithkeys";
        }
    }
}