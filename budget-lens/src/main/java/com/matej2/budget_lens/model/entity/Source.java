package com.matej2.budget_lens.model.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "source",
        uniqueConstraints = @UniqueConstraint(
                name = "source_source_category_key",
                columnNames = {"source", "category"}))
@Data
@NoArgsConstructor
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nonnull
    @NotBlank
    private String source;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    @Nonnull
    private Category category;
}
