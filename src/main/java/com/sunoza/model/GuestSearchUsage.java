package com.sunoza.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "guest_search_usage", uniqueConstraints =
        @UniqueConstraint(name = "uk_guest_search_client_date", columnNames = {"client_key", "usage_date"}))
public class GuestSearchUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_key", nullable = false, length = 64)
    private String clientKey;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "search_count", nullable = false)
    private int searchCount;

    protected GuestSearchUsage() {}
}
