package com.matej2.budget_lens.model.entity;


import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "frequency")
@Data
@NoArgsConstructor
public class Frequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nonnull
    @Min(0)
    @Max(365)
    private Short number;
    @Nonnull
    private String description;
}
