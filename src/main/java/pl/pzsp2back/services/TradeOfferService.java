package pl.pzsp2back.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.dto.OptimizationProcessDto;
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

    public List<TradeOfferDto> getUserTradeOffers(String login) {
        User user = userService.findUserByLogin(login);
        return user.getListTradeOffers().stream().map(t -> mapToTradeOfferDto(t)).collect(Collectors.toList());
    }

    public TradeOfferDto getTradeOffer(Long id) {
        return mapToTradeOfferDto(findTradeOfferById(id));
    }

    public TradeOfferDto createTradeOffer(TradeOfferPostDto newOffer, String login) {

        User user = userService.findUserByLogin(login);

        TimeSlot timeSlot = timeSlotService.findTimeSlotById(newOffer.timeSlotId());

        //TODO find optimization process for trade offer and add this to offer.

        TradeOffer tradeOffer = new TradeOffer(null, newOffer.price(), LocalDateTime.now(), user, timeSlot, null , newOffer.ifWantOffer(), null);

        return mapToTradeOfferDto(tradeOfferRepository.save(tradeOffer));
    }

    public TradeOfferDto updateTradeOffer(TradeOfferPostDto updatedOffer, Long id) {
        TradeOffer offer = findTradeOfferById(id);

        if (updatedOffer.price() != null) {
            offer.setPrice(updatedOffer.price());
        }

        if (updatedOffer.timeSlotId() != null) {
            TimeSlot timeSlot = timeSlotService.findTimeSlotById(updatedOffer.timeSlotId());
            offer.setTimeslot(timeSlot);
        }

        if (updatedOffer.ifWantOffer() != null) {
            offer.setIfWantOffer(updatedOffer.ifWantOffer());
        }

        return mapToTradeOfferDto(tradeOfferRepository.save(offer));
    }

    public TradeOfferDto deleteTradeOffer(Long id) {
        var offer = findTradeOfferById(id);
        tradeOfferRepository.delete(offer);
        return mapToTradeOfferDto(offer);
    }

    private TradeOffer findTradeOfferById(Long id) {
        return tradeOfferRepository.findById(id)
                .orElseThrow(() -> new TradeOfferServiceException("Trade offer not found!"));
    }

    public static TradeOfferDto mapToTradeOfferDto(TradeOffer tradeOffer) {
        return new TradeOfferDto(tradeOffer.getId(), tradeOffer.getPrice(), tradeOffer.getTimestamp(), UserService.mapToUserShortDto(tradeOffer.getOfferOwner()), TimeSlotService.mapToTimeSlotDto(tradeOffer.getTimeslot()), OptimizationProcessService.mapToOptimizationProcessDto(tradeOffer.getOptimizationProcess()), tradeOffer.getIfWantOffer(), tradeOffer.getIsActive());
    }



}
