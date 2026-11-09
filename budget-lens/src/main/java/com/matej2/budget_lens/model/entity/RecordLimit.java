package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "recordlimit")
@Data
public class RecordLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nonnull
    private String className;
    private Long currentCount;
    @Nonnull
    private Long recordLimit;
}
