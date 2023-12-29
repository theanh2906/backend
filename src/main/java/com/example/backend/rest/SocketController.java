package com.example.backend.rest;

import com.example.backend.dtos.ChatMessage;
import com.example.backend.dtos.Message;
import com.example.backend.dtos.OutputMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/api")
public class SocketController {
    @MessageMapping("/chat.check-in")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        }
        chatMessage.setTime(new Date().getTime());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/private")
    public void sendSpecific(@Payload Message message, Principal user, @Header("simpSessionId") String sessionId) {
        LOG.info(sessionId);
        OutputMessage out = new OutputMessage(message.getFrom(), message.getText(), new SimpleDateFormat("HH:mm").format(new Date()));
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(message.getTo());
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/private/user", out, headerAccessor.getMessageHeaders());
    }
    private static Logger LOG = LoggerFactory.getLogger(SocketController.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
}
