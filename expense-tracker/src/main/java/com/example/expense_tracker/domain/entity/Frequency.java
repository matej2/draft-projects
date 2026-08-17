package com.example.expense_tracker.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "frequency")
@NoArgsConstructor(force = true)
@Getter
public class Frequency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    public short number;
    public String description;

    public Frequency(short number, String description) {
        this.number = number;
        this.description = description;
    }
}
