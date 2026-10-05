package com.sunoza.service;

import com.sunoza.repository.GuestSearchUsageRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HexFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestSearchLimitService {
    public static final int DAILY_LIMIT = 5;
    private final GuestSearchUsageRepository usageRepository;

    public GuestSearchLimitService(GuestSearchUsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    /** Returns the searches remaining today, or -1 when the daily limit is exhausted. */
    @Transactional
    public int consumeSearch(String remoteAddress) {
        String clientKey = hash(remoteAddress == null ? "unknown" : remoteAddress);
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        usageRepository.createUsageRowIfMissing(clientKey, today);
        int updated = usageRepository.incrementIfBelowLimit(clientKey, today, DAILY_LIMIT);
        if (updated == 0) return -1;
        return DAILY_LIMIT - usageRepository.findSearchCount(clientKey, today);
    }

    private static String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
