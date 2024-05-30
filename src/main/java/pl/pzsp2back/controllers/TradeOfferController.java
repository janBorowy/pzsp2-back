package pl.pzsp2back.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.TradeOffer;
import pl.pzsp2back.services.TradeOfferService;

@RestController
@AllArgsConstructor
@RequestMapping("/tradeOffers")
public class TradeOfferController {

    private final TradeOfferService tradeOfferService;

    @Operation(summary = "Get trade offer with given id",
            description = "This endpoint returns trade offer for given id.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTradeOffer(@PathVariable("id") Long id) {
        try {
            var offer = tradeOfferService.getTradeOffer(id);
            return ResponseEntity.ok(offer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get all trade offers for given user (login)",
            description = "This endpoint returns all trade offers for given user, for all optimization processes, regardless if offer isActive or not, so you have to handle it.")
    @GetMapping("/all/{login}")
    public ResponseEntity<?> getAllUserTradeOffers(@PathVariable("login") String login) {
        try {
            var offer = tradeOfferService.getTradeOffer(10L);
            return ResponseEntity.ok(offer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Update trade offer with given id.",
            description = """
                    This endpoint updates trade offer with given id.
                    
                    Values not given will not be changed.
                    
                    return: updated offer
                    """)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTradeOffer(@Valid @RequestBody TradeOfferPostDto tradeOfferPostDto, @PathVariable("id") Long id) {
        try {
            var updatedOffer = tradeOfferService.updateTradeOffer(tradeOfferPostDto, id);
            return ResponseEntity.ok(updatedOffer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Create trade offer for given user (login).",
            description = """
                    This endpoint updates trade offer with given id.

                    Price: can be null (default: 0)\n
                    TimeSlotId: must be given\n
                    IfWantOffer: must be given\n
                    OptimizationProcessId: can be null (default: nearest optimization process)\n
                    
                    return: updated offer
                    """)
    @PostMapping("/{login}")
    public ResponseEntity<?> createTradeOffer(@Valid @RequestBody TradeOfferPostDto tradeOfferPostDto, @PathVariable("login") String login) {
        try {
            var updatedOffer = tradeOfferService.createTradeOffer(tradeOfferPostDto, login);
            return ResponseEntity.ok(updatedOffer);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Delete trade offer with given id.",
            description = """
                    return: deleted offer
                    """)
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
