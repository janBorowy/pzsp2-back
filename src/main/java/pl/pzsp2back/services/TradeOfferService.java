package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.TradeOfferDto;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TradeOfferService {
    private final TradeOfferRepository tradeOfferRepository;
    private final UserService userService;
    private final TimeSlotService timeSlotService;
    private final OptimizationProcessService optimizationProcessService;

    public List<TradeOffer> getUserTradeOffers(String login) {
        User user = userService.findUserByLogin(login);
        return user.getListTradeOffers();
    }

    public TradeOffer getTradeOffer(Long id) {
        return findTradeOfferById(id);
    }

    public TradeOffer createTradeOffer(TradeOfferPostDto newOffer, String login) {

        User user = userService.findUserByLogin(login);

        TimeSlot timeSlot = timeSlotService.getTimeSlot(newOffer.timeSlotId());

        OptimizationProcess optimizationProcess;
        if (newOffer.optimizationProcessId() == null) {
            optimizationProcess = optimizationProcessService.getNearestAcceptanceDeadlineOptimizationProcess(login);
        } else {
            optimizationProcess = optimizationProcessService.getOptimizationProcess(newOffer.optimizationProcessId());
        }

        TradeOffer tradeOffer = new TradeOffer(null, newOffer.price(), LocalDateTime.now(), user, timeSlot, optimizationProcess, newOffer.ifWantOffer(), null);

        return tradeOfferRepository.save(tradeOffer);
    }

    public TradeOffer updateTradeOffer(TradeOfferPostDto updatedOffer, Long id) {
        TradeOffer offer = findTradeOfferById(id);

        if (updatedOffer.price() != null) {
            offer.setPrice(updatedOffer.price());
        }

        if (updatedOffer.timeSlotId() != null) {
            TimeSlot timeSlot = timeSlotService.getTimeSlot(updatedOffer.timeSlotId());
            offer.setTimeslot(timeSlot);
        }

        if (updatedOffer.ifWantOffer() != null) {
            offer.setIfWantOffer(updatedOffer.ifWantOffer());
        }

        return tradeOfferRepository.save(offer);
    }

    public TradeOffer deleteTradeOffer(Long id) {
        var offer = findTradeOfferById(id);
        tradeOfferRepository.delete(offer);
        return offer;
    }

    private TradeOffer findTradeOfferById(Long id) {
        return tradeOfferRepository.findById(id)
                .orElseThrow(() -> new TradeOfferServiceException("Trade offer not found!"));
    }



}
