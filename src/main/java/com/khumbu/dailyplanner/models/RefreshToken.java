package com.khumbu.dailyplanner.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    private Date expiryDate;

    @OneToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Users user;
}
