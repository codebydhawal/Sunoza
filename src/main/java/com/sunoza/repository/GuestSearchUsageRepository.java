package com.sunoza.repository;

import com.sunoza.model.GuestSearchUsage;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestSearchUsageRepository extends JpaRepository<GuestSearchUsage, Long> {
    @Modifying
    @Query(value = "INSERT INTO guest_search_usage (client_key, usage_date, search_count) "
            + "VALUES (:clientKey, :usageDate, 0) ON CONFLICT (client_key, usage_date) DO NOTHING", nativeQuery = true)
    int createUsageRowIfMissing(@Param("clientKey") String clientKey, @Param("usageDate") LocalDate usageDate);

    @Modifying
    @Query("UPDATE GuestSearchUsage g SET g.searchCount = g.searchCount + 1 "
            + "WHERE g.clientKey = :clientKey AND g.usageDate = :usageDate AND g.searchCount < :limit")
    int incrementIfBelowLimit(@Param("clientKey") String clientKey, @Param("usageDate") LocalDate usageDate,
                              @Param("limit") int limit);

    @Query("SELECT g.searchCount FROM GuestSearchUsage g "
            + "WHERE g.clientKey = :clientKey AND g.usageDate = :usageDate")
    int findSearchCount(@Param("clientKey") String clientKey, @Param("usageDate") LocalDate usageDate);
}
