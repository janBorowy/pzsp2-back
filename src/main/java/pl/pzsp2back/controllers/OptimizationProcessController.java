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
import pl.pzsp2back.dto.ScheduleDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.OptimizationProcess;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.OptimizationProcessService;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/optimizationProcess")
public class OptimizationProcessController {

    private final OptimizationProcessService optimizationProcessService;
    private final DtoMapper dtoMapper;

    @Operation(summary = "[DONT USE] Get Optimization Process with given id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOptimizationProcess(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id) {
        try {
            if (optimizationProcessService.ifSameGroup(requesterUser.getLogin(), id)) {
                var optimizationProcessDto = optimizationProcessService.getOptimizationProcess(id);
                return ResponseEntity.ok(optimizationProcessDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get first by deadline date optimization process with nearest acceptance deadline date")
    @GetMapping("/")
    public ResponseEntity<?> getAnyOptimizationProcess(@AuthenticationPrincipal User requesterUser) {
        try {
            var optimizationProcessDto = dtoMapper.toDto(optimizationProcessService.getAnyOptimizationProcess(requesterUser.getLogin()));
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "[DONT USE] Get Optimization Process with nearest acceptance deadline date")
    @GetMapping("/nearest/{login}")
    public ResponseEntity<?> getNearestOptimizationProcess(@AuthenticationPrincipal User requesterUser, @PathVariable("login") String login) {
        try {
            var optimizationProcessDto = dtoMapper.toDto(optimizationProcessService.getNearestAcceptanceDeadlineOptimizationProcess(requesterUser.getLogin()));
            return ResponseEntity.ok(optimizationProcessDto);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "[DONT USE] Get all optimization processes for given user (login)")
    @GetMapping("/all/{login}")
    public ResponseEntity<?> getAllOptimizationProcesses(@AuthenticationPrincipal User requesterUser, @PathVariable("login") String login) {
        try {
            var optimizationProcessDtoList = optimizationProcessService.getAllOptimizationProcess(requesterUser.getLogin()).stream().map(op -> dtoMapper.toDto(op)).collect(Collectors.toList());
            return ResponseEntity.ok(optimizationProcessDtoList);
        } catch (TradeOfferServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "[DONT USE] Create new optimization process [admin only]",
            description = """
                    OfferAcceptanceDeadline: must be given and this date must be before optimization time date \n
                    OptimizationTime: can be null \n
                    
                    return: created optimization process
                    """)
    @PostMapping("/{login}")
    public ResponseEntity<?> createOptimizationProcess(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto, @PathVariable("login") String login) {
        try {
            if(requesterUser.getIfAdmin()) {
                OptimizationProcessDto savedOptimizationProcessDto = dtoMapper.toDto(optimizationProcessService.createOptimizationProcess(optimizationProcessPostDto, requesterUser.getLogin()));
                return ResponseEntity.ok(savedOptimizationProcessDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "[DONT USE] Update optimization process [admin only]",
            description = """
                    
                    All values can be null. Not given values wouldn't be changed.
                    
                    return: created optimization process
                    """)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOptimizationProcess(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id, @Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto) {
        try {
            if(requesterUser.getIfAdmin() &&  optimizationProcessService.ifUserIsProcessOwner(requesterUser.getLogin(), id)) {
                var updatedOptimizationProcess = dtoMapper.toDto(optimizationProcessService.updateOptimizationProcess(id, optimizationProcessPostDto));
                return ResponseEntity.ok(updatedOptimizationProcess);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update first by date optimization process [admin only]",
            description = """
                    
                    All values can be null. Not given values wouldn't be changed.
                    
                    return: created optimization process
                    """)
    @PutMapping("/")
    public ResponseEntity<?> updateAnyOptimizationProcess(@AuthenticationPrincipal User requesterUser, @Valid @RequestBody OptimizationProcessPostDto optimizationProcessPostDto) {
        try {
            if(requesterUser.getIfAdmin()) {
                var updatedOptimizationProcess = dtoMapper.toDto(optimizationProcessService.updateAnyOptimizationProcess(requesterUser.getLogin(), optimizationProcessPostDto));
                return ResponseEntity.ok(updatedOptimizationProcess);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);

        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Run optimization for optimization process with given id [admin only]",
            description = """
                    return: optimized schedule 
                    """)
    @GetMapping("/run/{id}")
    public ResponseEntity<?> runOptimization(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id) {
        try {
            if(requesterUser.getIfAdmin() &&  optimizationProcessService.ifUserIsProcessOwner(requesterUser.getLogin(), id)) {
                ScheduleDto optimizedScheduleDto = dtoMapper.toDto(optimizationProcessService.runOptimizationProcess(id));
                return ResponseEntity.ok(optimizedScheduleDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
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


    @Operation(summary = "[DONT USE] Delete optimization process [admin only]",
            description = """
                    return: deleted optimization process 
                    """)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOptimizationProcess(@AuthenticationPrincipal User requesterUser, @PathVariable("id") Long id) {
        try {
            if(requesterUser.getIfAdmin() &&  optimizationProcessService.ifUserIsProcessOwner(requesterUser.getLogin(), id)) {
                 OptimizationProcessDto deletedProcessDto = dtoMapper.toDto(optimizationProcessService.deleteOptimizationProcess(id));
                return ResponseEntity.ok(deletedProcessDto);
            }
            return new ResponseEntity<>("Action forbidden for this user.", HttpStatus.FORBIDDEN);
        } catch (OptimizationProcessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}