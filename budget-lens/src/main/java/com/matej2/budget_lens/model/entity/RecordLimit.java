package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "recordlimit")
@Data
@NoArgsConstructor
public class RecordLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nonnull
    private String className;
    @PositiveOrZero
    private Long currentCount;
    @Nonnull
    @PositiveOrZero
    private Long recordLimit;
}
