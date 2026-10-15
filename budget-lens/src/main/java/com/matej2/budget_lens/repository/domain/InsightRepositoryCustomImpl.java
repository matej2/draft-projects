package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Insight;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InsightRepositoryCustomImpl
        implements InsightRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    private static final String UPSERT_SQL = """
        INSERT INTO insight (
            year_month,
            category,
            standard_deviation_percent,
            median,
            percentile90,
            expense_count,
            updated_at,
            safe_budget_confidence,
            budget_utilization
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (year_month, category)
        DO UPDATE SET
            standard_deviation_percent = EXCLUDED.standard_deviation_percent,
            median = EXCLUDED.median,
            percentile90 = EXCLUDED.percentile90,
            expense_count = EXCLUDED.expense_count,
            updated_at = EXCLUDED.updated_at,
            safe_budget_confidence = EXCLUDED.safe_budget_confidence,
            budget_utilization = EXCLUDED.budget_utilization
        """;

    @Override
    @Transactional
    public void upsertAll(List<Insight> insights) {
        if (insights == null || insights.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                UPSERT_SQL,
                insights,
                500,
                (ps, insight) -> {
                    ps.setDate(1, Date.valueOf(insight.getYearMonth().atDay(1)));
                    ps.setInt(2, insight.getCategory().getId());
                    ps.setObject(3, insight.getStandardDeviationPercent());
                    ps.setObject(4, insight.getMedian());
                    ps.setObject(5, insight.getPercentile90());
                    ps.setObject(6, insight.getExpenseCount());
                    ps.setObject(7, insight.getUpdatedAt());
                    ps.setObject(8, insight.getSafeBudgetConfidence());
                    ps.setObject(9, insight.getBudgetUtilization());
                }
        );
    }
}