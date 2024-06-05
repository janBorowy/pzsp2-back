package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.dto.TradeOfferDto;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TradeOfferService {
    private final TradeOfferRepository tradeOfferRepository;
    private final UserService userService;
    private final TimeSlotService timeSlotService;
    private final @Lazy OptimizationProcessService optimizationProcessService;

    public List<TradeOffer> getUserTradeOffers(String login) {
        User user = userService.findUserByLogin(login);
        return user.getListTradeOffers();
    }

    public TradeOffer getTradeOffer(Long id) {
        return findTradeOfferById(id);
    }

    @Transactional
    public TradeOffer createTradeOffer(TradeOfferPostDto newOffer, String login) {
        User user = userService.findUserByLogin(login);

        TimeSlot timeSlot = timeSlotService.getTimeSlot(newOffer.timeSlotId());

        OptimizationProcess optimizationProcess;

        if (newOffer.optimizationProcessId() == null) {
            optimizationProcess = optimizationProcessService.getAnyOptimizationProcess(login);
        } else {
            optimizationProcess = optimizationProcessService.getOptimizationProcess(newOffer.optimizationProcessId());
        }

        TradeOffer existingTradeOffer = tradeOfferRepository.findTradeOfferByOptimizationProcessAndOfferOwnerAndTimeslot(optimizationProcess, user, timeSlot);

        if (optimizationProcess == null) {
            throw new TradeOfferServiceException("Schedule doesn't have any up-to date optimization processes assigned.");
        }


        if (existingTradeOffer != null) {
            throw new TradeOfferServiceException("This trade offer already exists. Offer ID: " + existingTradeOffer.getId());
        }

        Integer price = newOffer.price();

        if (price == null) {
            price = 0;
        }

        TradeOffer tradeOffer = new TradeOffer(null, price, LocalDateTime.now(), user, timeSlot, optimizationProcess, newOffer.ifWantOffer(), OfferStatus.ACTIVE);

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

    public boolean ifSameGroup(String login, Long offerId) {
        User user = userService.getUser(login);
        var ts = getTradeOffer(offerId);
        return ts.getOfferOwner().getGroup().getUsersList().stream().anyMatch(u -> u.getLogin().equals(login));
    }


    public boolean ifOfferOwner(String login, Long offerId) {
        var ts = getTradeOffer(offerId);
        return ts.getOfferOwner().getLogin().equals(login);
    }

    private TradeOffer findTradeOfferById(Long id) {
        return tradeOfferRepository.findById(id)
                .orElseThrow(() -> new TradeOfferServiceException("Trade offer not found!"));
    }

    @Transactional
    public List<TradeOffer> getGroupTradeOffers(String login) {
        User user = userService.getUser(login);
        var usersList = user.getGroup().getUsersList();
        List<TradeOffer> groupTradeOffers = new ArrayList<>();
        for (User u : usersList) {
            groupTradeOffers.addAll(u.getListTradeOffers());
        }
        return groupTradeOffers;
    }

    @Transactional
    public TradeOffer getTradeOfferByOwnerLoginAndTimeSlotId(String ownerLogin, Long timeSlotId) {
        User owner = userService.getUser(ownerLogin);
        TimeSlot timeSlot = timeSlotService.getTimeSlot(timeSlotId);
        return tradeOfferRepository.findByOfferOwnerAndTimeslot(owner, timeSlot);
    }
}


