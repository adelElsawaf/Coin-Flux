package com.coinflux.web.sse.scheduled;

import static com.coinflux.web.sse.constants.SseConstants.*;


import com.coinflux.web.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SseHeartbeatScheduler {

    private final SseService sseService;

    @Scheduled(fixedRate = 15000) // every 15 seconds
    public void sendHeartbeat() {
        sseService.sendHeartbeat();
    }
}
