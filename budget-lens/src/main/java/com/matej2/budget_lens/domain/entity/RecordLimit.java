package com.matej2.budget_lens.domain.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "recordlimit")
@Data
public class RecordLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String className;
    private Long currentCount;
    private Long recordLimit;
}
