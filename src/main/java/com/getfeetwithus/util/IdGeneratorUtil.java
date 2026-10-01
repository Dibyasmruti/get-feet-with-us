package com.getfeetwithus.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGeneratorUtil {

    private final AtomicLong userSeq = new AtomicLong(1001);
    private final AtomicLong orderSeq = new AtomicLong(10001);
    private final AtomicLong dietSeq = new AtomicLong(101);
    private final AtomicLong fitnessSeq = new AtomicLong(101);
    private final AtomicLong productSeq = new AtomicLong(101);
    private final AtomicLong meetingSeq = new AtomicLong(101);
    private final SecureRandom random = new SecureRandom();

    public String generateUserId() {
        return "USR" + userSeq.getAndIncrement();
    }

    public String generateOrderId() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD" + dateStr + (1000 + random.nextInt(9000));
    }

    public String generateDietId() {
        return "DIET" + dietSeq.getAndIncrement();
    }

    public String generateFitnessId() {
        return "FIT" + fitnessSeq.getAndIncrement();
    }

    public String generateProductId() {
        return "PRD" + productSeq.getAndIncrement();
    }

    public String generateMeetingId() {
        return "MTG" + meetingSeq.getAndIncrement();
    }

    public String generateResetToken() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
