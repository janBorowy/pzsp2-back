package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.exceptions.UserServiceException;
import pl.pzsp2back.orm.Trade;
import pl.pzsp2back.orm.TradeOffer;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.TradeOfferService;

@RestController
@AllArgsConstructor
@RequestMapping("/tradeOffers")
public class TradeOfferController {

    private final TradeOfferService tradeOfferService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserTradeOffers(@PathVariable("id") Long id) {
        try {
            var offer = tradeOfferService.getTradeOffer(id);
            return ResponseEntity.ok(offer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTradeOffer(@Valid @RequestBody TradeOffer offer, @PathVariable("id") Long id) {
        try {
            var updatedOffer = tradeOfferService.updateTradeOffer(offer, id);
            return ResponseEntity.ok(updatedOffer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTradeOffer(@PathVariable("id") Long id) {
        try {
            var deletedOffer = tradeOfferService.deleteTradeOffer(id);
            return ResponseEntity.ok(deletedOffer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
