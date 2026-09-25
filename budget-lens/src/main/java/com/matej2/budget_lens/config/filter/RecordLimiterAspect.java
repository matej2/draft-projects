package com.matej2.budget_lens.config.filter;

import com.matej2.budget_lens.domain.entity.RecordLimit;
import com.matej2.budget_lens.exception.RecordOverLimitExeption;
import com.matej2.budget_lens.repository.domain.RecordLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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

    private static String getArgClassName(List<Object> args, ProceedingJoinPoint pjp) throws Throwable {
        if (args.isEmpty() || args.getFirst().getClass().getName().equals("com.matej2.budget_lens.domain.entity.RecordLimit")) {
            pjp.proceed();
        }
        Object firstArg = args.getFirst();
        return firstArg.getClass().getName();
    }

    private long getCurrentRecordCountAddition(RecordLimit recordLimit, long argumentCount) throws Throwable {
        long actualRecordCount =  recordLimit.getCurrentCount() == null ?  0L : recordLimit.getCurrentCount();
        return actualRecordCount + argumentCount;

    }

    private long getCurrentRecordCountSubstraction(RecordLimit recordLimit, long argumentCount) throws Throwable {
        long actualRecordCount =  recordLimit.getCurrentCount() == null ?  0L : recordLimit.getCurrentCount();
        return actualRecordCount - argumentCount;

    }

    private void processRecordCount(RecordProcessingType recordProcessingType, ProceedingJoinPoint pjp) throws Throwable {
        List<Object> args = List.of(pjp.getArgs());
        String argClassName = getArgClassName(args, pjp);

        RecordLimit recordLimiter = this.recordLimiter.findOneByClassName(getArgClassName(args, pjp));
        if (recordLimiter == null) {
            pjp.proceed();
        } else {
            long recordCount;
            if (recordProcessingType == RecordProcessingType.SAVE) {
                recordCount = getCurrentRecordCountAddition(recordLimiter, args.size());
            } else {
                recordCount = getCurrentRecordCountSubstraction(recordLimiter, args.size());
            }

            Long recordLimit = recordLimiter.getRecordLimit();

            if (recordCount > recordLimit) {
                throw new RecordOverLimitExeption(String.format("Record limit exceeded for object of type %s. Limit is %d", argClassName, recordLimit));
            } else {
                recordLimiter.setCurrentCount(recordCount);
                this.recordLimiter.save(recordLimiter);
                pjp.proceed();
            }
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
