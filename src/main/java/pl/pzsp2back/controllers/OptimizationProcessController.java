package pl.pzsp2back.controllers;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pzsp2back.dto.OptimizationProcessDto;
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.services.OptimizationProcessService;

@RestController
@AllArgsConstructor
@RequestMapping("/optimizationProcess")
public class OptimizationProcessController {

    private final OptimizationProcessService optimizationProcessService;

    @Operation(summary = "Get Optimization Process with given id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOptimizationProcess(@PathVariable("id") Long id) {
        try {
            var optimizationProcessDto = optimizationProcessService.getOptimizationProcess(id);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get Optimization Process with nearest acceptance deadline date")
    @GetMapping("/nearest/{login}")
    public ResponseEntity<?> getNearestOptimizationProcess(@PathVariable("login") String login) {
        try {
            var optimizationProcessDto = optimizationProcessService.getNearestAcceptanceDeadlineOptimizationProcess(login);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Get all optimization processes for given user (login)")
    @GetMapping("/all/{login}")
    public ResponseEntity<?> getAllOptimizationProcesses(@PathVariable("login") String login) {
        try {
            var optimizationProcessDto = optimizationProcessService.getAllOptimizationProcess(login);
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Create new optimization process [admin only]",
            description = """
                    OfferAcceptanceDeadline: must be given and this date must be before optimization time date \n
                    OptimizationTime: can be null \n
                    
                    return: created optimization process
                    """)
    @PostMapping("/{login}")
    public ResponseEntity<?> createOptimizationProcess(@Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto, @PathVariable("login") String login) {
        try {
            OptimizationProcessDto savedOptimizationProcessDto = optimizationProcessService.createOptimizationProcess(optimizationProcessPostDto, login);
            return ResponseEntity.ok(savedOptimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Create new optimization process [admin only]",
            description = """
                    
                    All values can be null. Not given values wouldn't be changed.
                    
                    return: created optimization process
                    """)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOptimizationProcess(@PathVariable("id") Long id, @Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto) {
        try {
            var updatedOptimizationProcess = optimizationProcessService.updateOptimizationProcess(id, optimizationProcessPostDto);
            return ResponseEntity.ok(updatedOptimizationProcess);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Run optimization for optimization process with given id [admin only]",
            description = """
                    return: optimized schedule 
                    """)
    @GetMapping("/run/{id}")
    public ResponseEntity<?> runOptimization(@PathVariable("id") Long id) {
        try {
            ScheduleDto optimizedScheduleDto = optimizationProcessService.runOptimizationProcess(id);
            return ResponseEntity.ok(optimizedScheduleDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

// TODO maybe GET will be for simulate and POST will be for acceptance of changes
//    @PostMapping("/run/{id}")
//    public ResponseEntity<?> saveOptimization(@PathVariable("id") Long id) {
//        try {
//            ScheduleDto optimizedScheduleDto = optimizationProcessService.saveOptimizationProcess(optimizationProcessPostDto, login);
//            return ResponseEntity.ok(optimizedScheduleDto);
//        } catch (OptimizationProcessServiceException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


    @Operation(summary = "Delete optimization process [admin only]",
            description = """
                    return: deleted optimization process 
                    """)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOptimizationProcess(@PathVariable("id") Long id) {
        try {
            var deletedProcess = optimizationProcessService.deleteOptimizationProcess(id);
            return ResponseEntity.ok(deletedProcess);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}