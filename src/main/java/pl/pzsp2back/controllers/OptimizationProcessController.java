package pl.pzsp2back.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.OptimizationProcessDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.services.OptimizationProcessService;

@RestController
@AllArgsConstructor
@RequestMapping("/optimizationProcess")
public class OptimizationProcessController {

    private final OptimizationProcessService optimizationProcessService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOptimizationProcess(@PathVariable("id") Long id) {
        try {
            var optimizationProcessDto = optimizationProcessService.getOptimizationProcess(id);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/nearest/{login}")
    public ResponseEntity<?> getNearestOptimizationProcess(@PathVariable("login") String login) {
        try {
            var optimizationProcessDto = optimizationProcessService.getNearestAcceptanceDeadlineOptimizationProcess(login);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all/{login}")
    public ResponseEntity<?> getAllOptimizationProcesses(@PathVariable("login") String login) {
        try {
            var optimizationProcessDto = optimizationProcessService.getAllOptimizationProcess(login);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{login}")
    public ResponseEntity<?> createOptimizationProcess(@Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto, @PathVariable("login") String login) {
        try {
            OptimizationProcessDto savedOptimizationProcessDto = optimizationProcessService.createOptimizationProcess(optimizationProcessPostDto, login);
            return ResponseEntity.ok(savedOptimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateTradeOffer(@Valid @RequestBody TradeOffer offer, @PathVariable("id") Long id) {
//        try {
//            var updatedOffer = tradeOfferService.updateTradeOffer(offer, id);
//            return ResponseEntity.ok(updatedOffer);
//        } catch (OptimizationProcessServiceException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteTradeOffer(@PathVariable("id") Long id) {
//        try {
//            var deletedOffer = tradeOfferService.deleteTradeOffer(id);
//            return ResponseEntity.ok(deletedOffer);
//        } catch (OptimizationProcessServiceException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}