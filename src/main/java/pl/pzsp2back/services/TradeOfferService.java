package pl.pzsp2back.services;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pzsp2back.exceptions.TradeOfferServiceException;
import pl.pzsp2back.orm.TradeOffer;
import pl.pzsp2back.orm.TradeOfferRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class TradeOfferService {
    private final TradeOfferRepository tradeOfferRepository;

    //TODO
    public List<TradeOffer> getUserTradeOffers(String login) {
        return null;
    }

    public TradeOffer getTradeOffer(Long id) {
        return findTradeOfferById(id);
    }

    public TradeOffer updateTradeOffer(TradeOffer newOffer, Long id) {
        var offer = findTradeOfferById(id);
        //TODO write newOffer to offer and save
        offer = tradeOfferRepository.save(offer);
        return offer;
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

    private TradeOffer findTradeOfferByLogin(Long id) {
        return tradeOfferRepository.findById(id)
                .orElseThrow(() -> new TradeOfferServiceException("Trade offer not found!"));
    }



}
