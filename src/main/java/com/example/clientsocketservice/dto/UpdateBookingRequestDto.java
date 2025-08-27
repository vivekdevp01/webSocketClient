package com.example.clientsocketservice.dto;

import com.example.uberprojectentityservice.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingRequestDto {
    private BookingStatus status;
    private Optional<Long> driverId;


}
