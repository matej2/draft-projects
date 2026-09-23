package com.example.expense_tracker.config.singleton;

import com.example.expense_tracker.domain.*;

import java.util.Arrays;
import java.util.List;

public final class AvgInflationQuerySingleton {
    private static AvgInflationQueryRequest INSTANCE;

    public static AvgInflationQueryRequest getInstance() {

        if(INSTANCE == null) {
            Selection selectionMonths = new Selection("item", Utils.generateMonths());
            Selection selectionIndex = new Selection("item", Arrays.asList("2", "3"));

            ResponseFormat response = new ResponseFormat("json-stat");;
            List<QueryItem> query = Arrays.asList(
                    new QueryItem("MESEC", selectionMonths),
                    new QueryItem("INDEKS", selectionIndex)
            );

            INSTANCE = new AvgInflationQueryRequest(query, response);
        }

        return INSTANCE;
    }
}
