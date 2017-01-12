package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.view.LineChartView;

import java.util.List;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.interfaces.IHandleItems;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.task.HistoricalDataLoaderTask;

public class LineGraphActivity extends AppCompatActivity implements IHandleItems<Quote> {

    private static String LOG_TAG = LineGraphActivity.class.getSimpleName();

    public final static String STOCK_SYMBOL_ARG = "STOCK_SYMBOL_ARG";

    private LineChartView mLineChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_line_graph);

        mLineChartView = (LineChartView) findViewById(R.id.linechart);

        if (getIntent() == null) {
            Log.d(LOG_TAG, "Unable to load LineGraphActivity since it was not given stock data.");
            return;
        }
        String symbol = getIntent().getStringExtra(STOCK_SYMBOL_ARG);
        if (symbol.equals(null) || symbol.isEmpty()) {
            Log.d(LOG_TAG, "Unable to load LineGraphActivity since it was not given a stock symbol.");
            return;
        }

        HistoricalDataLoaderTask.Run(this, this, symbol);
    }

    @Override
    public void handleItems(List<Quote> items) {
        if (items == null) {
            String errorInfo = getString(R.string.load_stock_quotes_failed);
            Log.e(LOG_TAG, errorInfo);

            Toast.makeText(this, errorInfo, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        LineSet lineSet = getLineSet();

        float minimumPrice = Float.MAX_VALUE;
        float maximumPrice = Float.MIN_VALUE;

        int     thirdOfItems = (int) Math.floor(items.size() / 3);
        int     count = 0;
        String  date;
        int     dateCount = 0;
        float   price;
        for (Quote q : items) {
            if (dateCount < 4 &&
                    (count == 0 || (count % thirdOfItems) == 0 || count == items.size() -1))
            {
                date = q.getDate();
                dateCount++;
            }
            else
            {
                date = "";
            }
            price = Float.parseFloat(q.getClose());
            lineSet.addPoint(date, price);

            minimumPrice = Math.min(minimumPrice, price);
            maximumPrice = Math.max(maximumPrice, price);

            count++;
        }

        int yAxisStart = Math.round(Math.max(0f, minimumPrice - 5f));
        yAxisStart = yAxisStart - (yAxisStart % 10);
        int yAxisEnd = Math.round(maximumPrice + 5f);

        mLineChartView.setYLabels(YRenderer.LabelPosition.OUTSIDE)
                .setXLabels(XRenderer.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#448AFF"))
                .setXAxis(true)
                .setYAxis(false)
                .setAxisBorderValues(yAxisStart, yAxisEnd)
                .setStep(5)
                .setAxisLabelsSpacing(Tools.fromDpToPx(8))
                .addData(lineSet);

        mLineChartView.show();
    }

    private LineSet getLineSet()
    {
        LineSet dataSet = new LineSet();

        dataSet.setColor(Color.parseColor("#448AFF"))
                .setFill(Color.parseColor("#303F9F"))
                .setThickness(Tools.fromDpToPx(2))
                .setDashed(new float[]{30f, 10f});

        return dataSet;
    }
}