package com.example.expense_tracker.config.singleton;

import com.example.expense_tracker.domain.AvgInflationQueryRequest;
import com.example.expense_tracker.domain.Utils;

public final class AvgInflationQuerySingleton {
    private static AvgInflationQueryRequest INSTANCE;

    public static AvgInflationQueryRequest getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AvgInflationQueryRequest(Utils.generateMonths());
        }

        return INSTANCE;
    }
}
