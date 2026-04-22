package com.learning.backend.api.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimezoneHeartbeatJob {

    private static final Logger log = LoggerFactory.getLogger(TimezoneHeartbeatJob.class);

    /**
     * Fires in the JVM default zone but logs an explicit {@link ZoneId} to practice timezone-aware scheduling narratives.
     */
    @Scheduled(cron = "0 0 * * * *", zone = "UTC")
    public void hourlyUtc() {
        ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
        log.debug("Scheduled heartbeat (UTC hour roll): {}", utc);
    }
}
