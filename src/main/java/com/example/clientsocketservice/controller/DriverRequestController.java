package com.example.clientsocketservice.controller;

import com.example.clientsocketservice.Producers.KafkaProducerService;
import com.example.clientsocketservice.dto.*;
import com.example.uberprojectentityservice.models.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
@RequestMapping("/api/socket")
public class DriverRequestController {
//    private final SimpMessagingTemplate messagingTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;


    public DriverRequestController(RestTemplate restTemplate,KafkaProducerService kafkaProducerService, SimpMessagingTemplate simpMessagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.restTemplate = restTemplate;
        this.kafkaProducerService = kafkaProducerService;
    }
    @GetMapping

    public ResponseEntity<Boolean> help() {
        kafkaProducerService.publishMessage("sample-topic", "Hello from client-socket-service");
        return ResponseEntity.ok(Boolean.TRUE);
    }


    @PostMapping("/newride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        System.out.println("Raising ride request");
        sendDriverNewRideRequest(rideRequestDto);
        System.out.println("Raised ride request");
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);

    }

//    @MessageMapping("/ping")
//    @SendTo("/topic/ping")
//    public TestResponse request(TestRequest message){
//        System.out.println("Received request"+message.getData());
//        return TestResponse.builder().data("Recived").build();
//    }
    public void sendDriverNewRideRequest(RideRequestDto rideRequestDto) {
        System.out.println("Sending periodic message");
//        near By driver
        simpMessagingTemplate.convertAndSend("/topic/rideRequest",rideRequestDto);
    }
    @MessageMapping("/rideResponse/{userId}")
    public synchronized void rideResponseHandler(@DestinationVariable String userId, RideResponseDto rideResponseDto){
        if (rideResponseDto.getBookingId() == null) {
            throw new IllegalArgumentException("BookingId cannot be null in driver response");
        }

//        System.out.println("Received ride response "+rideResponseDto.getResponse()+" "+userId);
        System.out.println("Ride response received: "
                + rideResponseDto.getResponse()
                + " bookingId="
                + rideResponseDto.getBookingId());
        UpdateBookingRequestDto requestDto= UpdateBookingRequestDto.builder()
                .driverId(Optional.of(Long.parseLong(userId)))
                .status(BookingStatus.SCHEDULED)
                .build();
      ResponseEntity<UpdateBookingDto> result=  this.restTemplate.postForEntity("http://localhost:7777/api/v1/booking/"+rideResponseDto.bookingId,requestDto,UpdateBookingDto.class);
      kafkaProducerService.publishMessage("sample-topic","hello");
      System.out.println(result.getStatusCode());
      System.out.println(result.getBody());

    }

}
