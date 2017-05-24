package com.gw.domain.interactor.forecast;

import com.gw.domain.model.Interval;
import com.gw.domain.model.TransactionItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PredictionManager {

    @Inject
    public PredictionManager() {
    }



    public TreeMap<Date, Double> predict(TreeMap<Date, Double> inputMap, Date start, Date end){
        TreeMap<Date, Double> predictionMap = new TreeMap<>();
        start = inputMap.lastKey();

        double[] x = new double[inputMap.size()];
        double[] y = new double[inputMap.size()];

        int i = 0;

        for (Map.Entry<Date, Double> entry : inputMap.entrySet()) {
            x[i] = TimeUnit.MILLISECONDS.toDays(entry.getKey().getTime() );
            y[i] = entry.getValue();
            i++;
        }

        final long day_length = 1000 * 60 * 60 * 24;

        long days = TimeUnit.MILLISECONDS.toDays(end.getTime() - start.getTime());


        PolynomialRegression regression = new PolynomialRegression(x, y, 1);


        long predict_day = TimeUnit.MILLISECONDS.toDays(start.getTime());
        predict_day++;


        for (long j = 0; j < days; j++, predict_day++) {
//            double randomValue = 1;
//            double min = 0.95, max = 1.05;
//            if (j % 10 == 0) {
//                min = 0.8 + (1.2 - 0.8) * r.nextDouble();
//            }
//            randomValue = min + (max - min) * r.nextDouble();
            double balance = regression.predict(predict_day);
//            if(j < 1)
//                predictionMap.put(new Date(predict_day * day_length + start_trsn.getTime()), inputMap.lastEntry().getValue());
//
//            else
            predictionMap.put(new Date(predict_day * day_length), balance);
        }
        return predictionMap;
    }

}
