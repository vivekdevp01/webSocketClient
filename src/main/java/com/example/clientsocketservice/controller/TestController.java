package com.example.clientsocketservice.controller;



import com.example.clientsocketservice.dto.ChatRequest;
import com.example.clientsocketservice.dto.ChatResponse;
import com.example.clientsocketservice.dto.TestRequest;
import com.example.clientsocketservice.dto.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message) {
//        return
//        new TestResponse("Received");
        System.out.println("Received request" + message.getData());
        return TestResponse.builder().data("Received").build();
    }

//    @SendTo("/topic/scheduled")
//    @Scheduled(fixedDelay = 1000)
//    public void sendPeriodicMessage(){
//        System.out.println("Sending periodic message");

    /// /        simpMessagingTemplate.convertAndSendToUser("/topic/scheduled" ,"Periodic Message"+System.currentTimeMillis());
//        simpMessagingTemplate.convertAndSend("/topic/scheduled","periodic message"+System.currentTimeMillis());
//    }

//    @MessageMapping("/chat")
//    @SendTo("/topic/message")
//    now for room
    @MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")

    public ChatResponse chatResponse(@DestinationVariable String room, ChatRequest chatRequest) {
        ChatResponse response = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timestamp("" + System.currentTimeMillis())
                .build();
        System.out.println("Received response " + response.getMessage());
        return response;


    }

    @MessageMapping("/privateChat/{room}/{userId}")
//    @SendTo("/topic/privateMessage/{room}/{userId}")
    public void privateChatResponse(@DestinationVariable String room, @DestinationVariable String userId, ChatRequest chatRequest) {
        ChatResponse response = ChatResponse.builder()
                .name(chatRequest.getName())
                .message(chatRequest.getMessage())
                .timestamp("" + System.currentTimeMillis())
                .build();
        simpMessagingTemplate.convertAndSendToUser(userId, "/queue/privateMessage/" + room, response);
//        System.out.println("Received response "+response.getMessage());
//        return response;


    }



}
