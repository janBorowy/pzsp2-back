package pl.pzsp2back.dtoPost;

public record TradePostDto(Long timeSlotId, String sellerLogin, String buyerLogin, Integer price) {
}
