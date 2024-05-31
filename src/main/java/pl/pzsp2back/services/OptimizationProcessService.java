package pl.pzsp2back.services;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.mapper.Mapper;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OptimizationProcessService {

    private final OptimizationProcessRepository optimizationProcessRepository;

    private UserService userService;


    public OptimizationProcess createOptimizationProcess(OptimizationProcessPostDto optimizationProcessPostDto, String login) {
        User userOwner = userService.findUserByLogin(login);

        if (optimizationProcessPostDto.optimizationTime() != null) {
            if (optimizationProcessPostDto.optimizationTime().isBefore(optimizationProcessPostDto.offerAcceptanceDeadline())) {
                throw new OptimizationProcessServiceException("Optimization time is before offer acceptance deadline!");
            }
        }

        OptimizationProcess optimizationProcess = new OptimizationProcess(null, LocalDateTime.now(), optimizationProcessPostDto.offerAcceptanceDeadline(), optimizationProcessPostDto.optimizationTime(), ScheduleService.getOneSchedule(userOwner.getGroup().getSchedulesList()), userOwner, null);

        return optimizationProcessRepository.save(optimizationProcess);
    }


    public OptimizationProcess getNearestAcceptanceDeadlineOptimizationProcess(String login) {
        User user = userService.findUserByLogin(login);
        Schedule schedule =  ScheduleService.getOneSchedule(user.getGroup().getSchedulesList());

        //TODO
        List<OptimizationProcess> optimizationProcesses = optimizationProcessRepository.findOptimizationProcessByOfferAcceptanceDeadlineAfterOrderByOfferAcceptanceDeadline(LocalDateTime.now());

        if (optimizationProcesses.isEmpty()) {
            throw new OptimizationProcessServiceException("This schedule doesn't have assigned optimization processes");
        }

        return optimizationProcesses.get(0);
    }


    @Transactional
    public List<OptimizationProcess> getAllOptimizationProcess(String login) {
        User user = userService.findUserByLogin(login);
        List<OptimizationProcess> optimizationProcessList = ScheduleService.getOneSchedule(user.getGroup().getSchedulesList()).getOptimizationProcesses();
        return optimizationProcessList;
    }


    public OptimizationProcess getOptimizationProcess(Long id) {
        return findOptimizationProcessById(id);
    }


    public OptimizationProcess updateOptimizationProcess(Long id, OptimizationProcessPostDto optimizationProcessPostDto) {

        OptimizationProcess optimizationProcess = findOptimizationProcessById(id);

        if (optimizationProcessPostDto.offerAcceptanceDeadline() != null) {
            optimizationProcess.setOfferAcceptanceDeadline(optimizationProcessPostDto.offerAcceptanceDeadline());
        }

        if (optimizationProcessPostDto.optimizationTime() != null) {
            if (optimizationProcessPostDto.optimizationTime().isBefore(optimizationProcess.getOfferAcceptanceDeadline())) {
                throw new OptimizationProcessServiceException("Optimization time is before offer acceptance deadline!");
            }
            optimizationProcess.setOptimizationTime(optimizationProcessPostDto.optimizationTime());
        }

        return optimizationProcessRepository.save(optimizationProcess);
    }

    public OptimizationProcess deleteOptimizationProcess(Long id) {

        OptimizationProcess optimizationProcess = findOptimizationProcessById(id);

        optimizationProcessRepository.delete(optimizationProcess);

        return optimizationProcess;
    }

    public Schedule runOptimizationProcess(Long id) {
        OptimizationProcess optimizationProcess = findOptimizationProcessById(id);

        Mapper mapper = new Mapper();

        //TODO write logic which will be responsible for running ampl model and (temporary) saving data

        return ScheduleService.getOneSchedule(optimizationProcess.getProcessOwner().getGroup().getSchedulesList());
    }


    private OptimizationProcess findOptimizationProcessById(Long id) {
        Optional<OptimizationProcess> optimizationProcess = optimizationProcessRepository.findById(id);
        if(!optimizationProcess.isPresent()) {
            throw new OptimizationProcessServiceException("Optimization process doesn't exist with id: " + id);
        } else {
            return optimizationProcess.get();
        }
    }


}
