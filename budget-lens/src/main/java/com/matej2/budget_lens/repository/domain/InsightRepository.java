package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {
    @Modifying
    @Query("""
    UPDATE Insight i
    SET 
        i.average = :average,
        i.standardDeviation = :standardDeviation,
        i.standardDeviationPercent = :standardDeviationPercent
    WHERE 
        i.category = :categoryId
    """)
    void updateByCategoryIdAndDate(
            float average,
            float standardDeviation,
            float standardDeviationPercent,
            int categoryId);
}
