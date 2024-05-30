package pl.pzsp2back.dtoPost;

public record TradeOfferPostDto(Integer price, Long timeSlotId, Boolean ifWantOffer, Long optimizationProcessId) {
}
