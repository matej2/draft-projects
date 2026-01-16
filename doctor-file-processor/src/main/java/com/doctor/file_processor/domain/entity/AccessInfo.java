package com.doctor.file_processor.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "access_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessInfo {
    @Id
    private String ip;
    // Can be byte
    private Long numOfCallsLastMinute;
    // Not really needed
    private Date created;

}
