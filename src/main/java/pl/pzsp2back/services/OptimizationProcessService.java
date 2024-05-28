package pl.pzsp2back.services;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dto.OptimizationProcessDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.orm.OptimizationProcess;
import pl.pzsp2back.orm.OptimizationProcessRepository;
import pl.pzsp2back.orm.Schedule;
import pl.pzsp2back.orm.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OptimizationProcessService {

    private final OptimizationProcessRepository optimizationProcessRepository;

    private UserService userService;

    public OptimizationProcessDto createOptimizationProcess(OptimizationProcessPostDto optimizationProcessPostDto, String login) {
        User userOwner = userService.findUserByLogin(login);

        OptimizationProcess optimizationProcess = new OptimizationProcess(null, LocalDateTime.now(), optimizationProcessPostDto.offerAcceptanceDeadline(), optimizationProcessPostDto.optimizationTime(), ScheduleService.getOneSchedule(userOwner.getGroup().getSchedulesList()), userOwner, null);

        return mapToOptimizationProcessDto(optimizationProcessRepository.save(optimizationProcess));
    }

    public OptimizationProcessDto getNearestAcceptanceDeadlineOptimizationProcess(String login) {
        User user = userService.findUserByLogin(login);
        Schedule schedule =  ScheduleService.getOneSchedule(user.getGroup().getSchedulesList());

        List<OptimizationProcess> optimizationProcesses = optimizationProcessRepository.findOptimizationProcessByOfferAcceptanceDeadlineAfterOrderByOfferAcceptanceDeadline(LocalDateTime.now());

        if (optimizationProcesses.isEmpty()) {
            throw new OptimizationProcessServiceException("This schedule doesn't have assigned optimization processes");
        }

        return mapToOptimizationProcessDto(optimizationProcesses.get(0));

    }

    @Transactional
    public List<OptimizationProcessDto> getAllOptimizationProcess(String login) {
        User user = userService.findUserByLogin(login);
        List<OptimizationProcess> optimizationProcessList = ScheduleService.getOneSchedule(user.getGroup().getSchedulesList()).getOptimizationProcesses();
        List<OptimizationProcessDto> optimizationProcessDtoList = optimizationProcessList.stream().map(op -> mapToOptimizationProcessDto(op)).collect(Collectors.toList());
        return optimizationProcessDtoList;
    }



    public OptimizationProcessDto getOptimizationProcess(Long id) {
        return mapToOptimizationProcessDto(findOptimizationProcessById(id));
    }

    private OptimizationProcess findOptimizationProcessById(Long id) {
        Optional<OptimizationProcess> optimizationProcess = optimizationProcessRepository.findById(id);
        if(!optimizationProcess.isPresent()) {
            throw new OptimizationProcessServiceException("Optimization process doesn't exist with id: " + id);
        } else {
            return optimizationProcess.get();
        }
    }


    public static OptimizationProcessDto mapToOptimizationProcessDto(OptimizationProcess optimizationProcess) {
        return new OptimizationProcessDto(optimizationProcess.getId(), optimizationProcess.getTimestamp(), optimizationProcess.getOfferAcceptanceDeadline(), optimizationProcess.getOptimizationTime(), optimizationProcess.getSchedule().getId(), UserService.mapToUserShortDto(optimizationProcess.getProcessOwner()));
    }
}
