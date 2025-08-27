package com.example.clientsocketservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDto {

    public Boolean response;
    public Long bookingId;
}
