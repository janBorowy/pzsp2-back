package pl.pzsp2back.dto;

import pl.pzsp2back.orm.TradeOffer;

import java.time.LocalDateTime;

public record TradeDto(Long id, Integer finalPrice, LocalDateTime timestamp, OptimizationProcessDto optimizationProcessDto, TradeOfferDto sellerOffer, TradeOfferDto buyerOffer) {
}
