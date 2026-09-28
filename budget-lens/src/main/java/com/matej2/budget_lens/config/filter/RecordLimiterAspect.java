package com.matej2.budget_lens.config.filter;

import com.matej2.budget_lens.config.annotation.RecordLimitEnabled;
import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.exception.RecordOverLimitExeption;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RecordLimiterAspect {
    private final RecordLimiter recordLimiter;
    private enum RecordProcessingType {
        SAVE,
        DELETE
    }

    private static RecordLimitEnabled getArgClassName(Object repository) {
        List<Class<?>> repositoryInterfaces = Arrays.stream(repository.getClass().getInterfaces()).toList();
        if (repositoryInterfaces.isEmpty()) {
            return null;
        }

        return repositoryInterfaces.getFirst().getAnnotation(RecordLimitEnabled.class);
    }

    private long getCurrentRecordCount(RecordLimit recordLimit, long recordCount) throws Throwable {
        long actualRecordCount =  recordLimit.getCurrentCount() == null ?  0L : recordLimit.getCurrentCount();
        return actualRecordCount + recordCount;

    }

    private long getCurrentRecordCountSubstraction(RecordLimit recordLimit) throws Throwable {
        return getCurrentRecordCount(recordLimit, -1);
    }

    private void checkRecordLimit(Long recordCount, RecordLimit recordLimitForEntity, ProceedingJoinPoint pjp) throws Throwable {
        if (recordCount > recordLimitForEntity.getRecordLimit()) {
            throw new RecordOverLimitExeption(String.format("Record limit exceeded for object of type %s", recordLimitForEntity));
        } else {
            recordLimitForEntity.setCurrentCount(recordCount);
            this.recordLimiter.save(recordLimitForEntity);
            pjp.proceed();
        }
    }

    private void processRecordCount(RecordProcessingType recordProcessingType, ProceedingJoinPoint pjp) throws Throwable {
        List<Object> args = List.of(pjp.getArgs());
        RecordLimitEnabled argClassName = getArgClassName(pjp.getThis());

        if  (argClassName == null) {
            pjp.proceed();
            return;
        }

        RecordLimit recordLimitsForEntity = this.recordLimiter.findOneByClassName(argClassName.entityName());
        if (recordLimitsForEntity == null) {
            pjp.proceed();
        } else {
            long recordCount;
            if (recordProcessingType == RecordProcessingType.SAVE) {
                recordCount = getCurrentRecordCount(recordLimitsForEntity, args.size());
            } else {
                recordCount = getCurrentRecordCountSubstraction(recordLimitsForEntity);
            }

            checkRecordLimit(recordCount, recordLimitsForEntity, pjp);
        }
    }

    // It might not cover all edge cases, for those one should create a scheduled job that
    // Updates the count by directly querying data in the database
    // Method assumes that all arguments are of the same type
    @Around("this(org.springframework.data.repository.Repository) && execution(* save*(..))")
    public synchronized void interceptSaveRepositoryCalls(ProceedingJoinPoint pjp) throws Throwable {
        processRecordCount(RecordProcessingType.SAVE, pjp);
    }

    @Around("this(org.springframework.data.repository.Repository) && execution(* delete*(..))")
    public synchronized void interceptDeleteRepositoryCalls(ProceedingJoinPoint pjp) throws Throwable {
        processRecordCount(RecordProcessingType.DELETE, pjp);
    }
}
