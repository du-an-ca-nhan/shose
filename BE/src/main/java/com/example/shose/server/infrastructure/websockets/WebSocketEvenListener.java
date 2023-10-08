package com.example.shose.server.infrastructure.websockets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEvenListener {

    @EventListener
    public  void handleWebSocketDisconnectListener(SessionDisconnectEvent event){

        // todo - to be impl
    }
}
