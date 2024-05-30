package pl.pzsp2back.dto;

import java.time.LocalDateTime;

public record TradeOfferDto(Long id, Integer price, LocalDateTime timestamp, UserShortDto userOwner, TimeSlotDto timeSlot,OptimizationProcessDto optimizationProcess, Boolean ifWantOffer, Boolean isActive) {

}
