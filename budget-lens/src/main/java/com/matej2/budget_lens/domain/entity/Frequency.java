package com.matej2.budget_lens.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "frequency")
@Getter
public class Frequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private short number;
    private String description;
}
