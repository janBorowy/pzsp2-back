package pl.pzsp2back.services;


import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.mapper.Mapper;
import pl.pzsp2back.mapper.Result;
import pl.pzsp2back.mapper.Runner;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OptimizationProcessService {

    private final OptimizationProcessRepository optimizationProcessRepository;

    private UserService userService;

    @Transactional
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

        List<OptimizationProcess> optimizationProcesses = optimizationProcessRepository.findOptimizationProcessByScheduleAndOfferAcceptanceDeadlineAfterOrderByOfferAcceptanceDeadline(schedule, LocalDateTime.now());

        OptimizationProcess nearest=null;

        if (!optimizationProcesses.isEmpty()) {
            nearest = optimizationProcesses.get(0);
        }

        return nearest;
    }

    public OptimizationProcess getAnyOptimizationProcess(String login) {
        User user = userService.findUserByLogin(login);
        Schedule schedule =  ScheduleService.getOneSchedule(user.getGroup().getSchedulesList());

        List<OptimizationProcess> optimizationProcesses = optimizationProcessRepository.findOptimizationProcessByScheduleOrderByOfferAcceptanceDeadline(schedule);

        if (optimizationProcesses.isEmpty()) {
            throw new OptimizationProcessServiceException("Schedule doesn't have any optimization process assigned");
        } else if ( optimizationProcesses.size() > 1) {
            throw new OptimizationProcessServiceException("Schedule has more than one optimization process assigned");
        }

        return optimizationProcesses.get(0) ;
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


    @Transactional
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

    @Transactional
    public OptimizationProcess updateAnyOptimizationProcess(String login, OptimizationProcessPostDto optimizationProcessPostDto) {

        User user = userService.findUserByLogin(login);
        Schedule schedule = ScheduleService.getOneSchedule(user.getGroup().getSchedulesList());

        List<OptimizationProcess> optimizationProcesses = optimizationProcessRepository.findOptimizationProcessByScheduleOrderByOfferAcceptanceDeadline(schedule);

        OptimizationProcess any = null;

        if (optimizationProcesses.isEmpty()) {
            throw new OptimizationProcessServiceException("Schedule doesn't have assigned any optimization process");
        }
        any = optimizationProcesses.get(0);

        if (optimizationProcessPostDto.offerAcceptanceDeadline() != null) {
            any.setOfferAcceptanceDeadline(optimizationProcessPostDto.offerAcceptanceDeadline());
        }

        if (optimizationProcessPostDto.optimizationTime() != null) {
            if (optimizationProcessPostDto.optimizationTime().isBefore(any.getOfferAcceptanceDeadline())) {
                throw new OptimizationProcessServiceException("Optimization time is before offer acceptance deadline!");
            }
            any.setOptimizationTime(optimizationProcessPostDto.optimizationTime());
        }

        return optimizationProcessRepository.save(any);

    }

    @Transactional
    public OptimizationProcess deleteOptimizationProcess(Long id) {

        OptimizationProcess optimizationProcess = findOptimizationProcessById(id);

        optimizationProcessRepository.delete(optimizationProcess);

        return optimizationProcess;
    }

    @Transactional
    public Schedule runOptimizationProcess(Long id) {
        OptimizationProcess optimizationProcess = findOptimizationProcessById(id);

        Mapper mapper = new Mapper();
        System.out.println("xxxxx");
        mapper.mapDataFile(optimizationProcess);
        Runner runner = new Runner();
        System.out.println("xxxxx");
        Result res = runner.runAmpl(optimizationProcess.getProcessOwner().getGroup().getId());
        System.out.println("xxxxx");
        System.out.println(res);
        //TODO write logic which will be responsible for running ampl model and (temporary) saving data

        return ScheduleService.getOneSchedule(optimizationProcess.getProcessOwner().getGroup().getSchedulesList());
    }

    public boolean ifUserIsProcessOwner(String login, Long processId) {
        OptimizationProcess op = getOptimizationProcess(processId);
        return op.getProcessOwner().getLogin().equals(login);

    }

    public boolean ifSameGroup(String login, Long processId) {
        OptimizationProcess op = getOptimizationProcess(processId);
        List<User> usersList = op.getSchedule().getGroup().getUsersList();
        return usersList.stream().anyMatch(u -> u.getLogin().equals(login));

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
