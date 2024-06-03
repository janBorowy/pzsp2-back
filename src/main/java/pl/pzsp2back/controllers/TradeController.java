package pl.pzsp2back.controllers;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.DtoMapper;
import pl.pzsp2back.dto.OptimizationProcessDto;
import pl.pzsp2back.dto.TradeDto;
import pl.pzsp2back.dto.TradeOfferDto;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.TradeOfferService;
import pl.pzsp2back.services.TradeService;
import pl.pzsp2back.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/trade")
public class TradeController {
    private final TradeService tradeService;
    private final DtoMapper dtoMapper;

    @Operation(summary = "Get all trades for logged user [Admin gets all trades for group]",
            description = "For user you can split offers to sell and buy offers")
    @GetMapping("/all")
    public ResponseEntity<?> getUserTradeOffers(@AuthenticationPrincipal User requesterUser) {
        try {
            List<TradeDto> trades = null;
            if(requesterUser.getIfAdmin()){
                trades = tradeService.getGroupTrades(requesterUser.getLogin()).stream().map(to -> dtoMapper.toDto(to)).collect(Collectors.toList());
            } else {
                trades = tradeService.getUserTrades(requesterUser.getLogin()).stream().map(to -> dtoMapper.toDto(to)).collect(Collectors.toList());
            }

            return ResponseEntity.ok(trades);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Get trade object for given trade_offer id (only if trade_offer is realized positive)")
    @GetMapping("/fromTradeOffer/{id}")
    public ResponseEntity<?> getUserTradeOffers(@AuthenticationPrincipal User requesterUser, Long id) {
        try {

            TradeDto trade = dtoMapper.toDto(tradeService.getTradeFromOffer(id));

            return ResponseEntity.ok(trade);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
