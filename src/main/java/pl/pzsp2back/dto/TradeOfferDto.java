package pl.pzsp2back.dto;

import pl.pzsp2back.orm.OfferStatus;

import java.time.LocalDateTime;

public record TradeOfferDto(Long id, Integer price, LocalDateTime timestamp, UserShortDto userOwner, TimeSlotDto timeSlot,OptimizationProcessDto optimizationProcess, Boolean ifWantOffer, OfferStatus status) {

}
