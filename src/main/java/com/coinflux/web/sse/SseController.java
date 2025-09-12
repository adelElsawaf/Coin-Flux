package com.coinflux.web.sse;

import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.sse.dtos.SseNotificationDTO;
import com.coinflux.web.user.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SseNotificationDTO>> subscribe(@LoggedInUser UserDTO currentUser) {
        return sseService.subscribe(currentUser.getId());
    }

    @PostMapping("/test/{userId}")
    public void testPush(@PathVariable Long userId, @RequestBody SseNotificationDTO dto) {
        sseService.sendToUser(userId, dto);
    }
}
