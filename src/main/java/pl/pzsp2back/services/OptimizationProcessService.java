package pl.pzsp2back.services;


import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.dtoPost.TradePostDto;
import pl.pzsp2back.exceptions.OptimizationProcessServiceException;
import pl.pzsp2back.mapper.Mapper;
import pl.pzsp2back.mapper.Result;
import pl.pzsp2back.mapper.Runner;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OptimizationProcessService {

    private final OptimizationProcessRepository optimizationProcessRepository;
    private final TradeOfferRepository tradeOfferRepository;
    private final TimeslotRepository timeslotRepository;

    private UserService userService;
    private TradeService tradeService;

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
        mapper.mapDataFile(optimizationProcess);
        Runner runner = new Runner();
        Result res = runner.runAmpl(optimizationProcess.getProcessOwner().getGroup().getId());
        System.out.println(res);


        Map<Long, Integer> prices = res.getTimeSlotsPrices();
        MultiMap <Long, String> vDown = res.getvDown(); //seller
        MultiMap <Long, String> vUp = res.getvUp(); //buyer

        // Create a set of unique time slot IDs
        Set<Long> allTimeSlotIDs = new HashSet<>(prices.keySet());
        Set<Long> vUpIDs = new HashSet<>(vUp.keySet());

        // Convert vUp.values() and vDown.values() to sets
        Set<Long> vDownIDs = new HashSet<>(vDown.keySet());



        // Check if all sets contain the same IDs
        boolean areSame = vUpIDs.equals(vDownIDs);

        if(!areSame) {
            throw new OptimizationProcessServiceException("Error in ampl data. IDs in vUp, vDown na prices are different.");
        }


        for(Long timeSlotId : prices.keySet()) {
            var ts = timeslotRepository.findById(timeSlotId).get();
            ts.setLastMarketPrice(prices.get(timeSlotId));
            timeslotRepository.save(ts);
        }


        for (Long tsId : allTimeSlotIDs) {
            List<String> sellers = vDown.get(tsId);
            if(sellers == null){sellers = new ArrayList<>();}
            List<String> buyers = vUp.get(tsId);
            if(buyers == null){buyers = new ArrayList<>();}
            Integer price = prices.get(tsId);
            if(sellers.size() != buyers.size()) {
                throw new OptimizationProcessServiceException("The size of buyers and sellers for one timeslot are not equal");
            }
            for (int i = 0; i < sellers.size(); i++) {
                tradeService.createTrade(new TradePostDto(tsId, sellers.get(i), buyers.get(i), price));
            }
        }


        //make to inactive all offers which are still active
        var allTradeOffers = optimizationProcess.getTradeOffersList();
        for (TradeOffer tradeOffer : allTradeOffers) {
            if (tradeOffer.getStatus().equals(OfferStatus.ACTIVE)) {
                tradeOffer.setStatus(OfferStatus.NEGATIVE_REALIZED);
                tradeOfferRepository.save(tradeOffer);
            }

        }

        System.out.println("Zaktualizowano wartości");

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
