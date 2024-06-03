package pl.pzsp2back.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pzsp2back.orm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final UserService userService;

    @Transactional
    public List<Trade> getUserTrades(String login) {
        User user = userService.getUser(login);
        List<TradeOffer> acceptedTradeOfferList = user.getListTradeOffers().stream()
                .filter(tradeOffer -> tradeOffer.getStatus().equals(OfferStatus.POSITIVE_REALIZED))
                .collect(Collectors.toList());
        List<Trade> trades = acceptedTradeOfferList.stream().map(to -> getTradeFromOffer(to)).collect(Collectors.toList());
        return trades;
    }

    @Transactional
    public List<Trade> getUserSellTrades(String login) {
        User user = userService.getUser(login);
        List<TradeOffer> acceptedTradeOfferList = user.getListTradeOffers().stream()
                .filter(tradeOffer -> tradeOffer.getStatus().equals(OfferStatus.POSITIVE_REALIZED))
                .collect(Collectors.toList());
        List<Trade> sellOffers = new ArrayList<>();
        for (TradeOffer tradeOffer : acceptedTradeOfferList) {
            Trade trade = tradeRepository.findTradeBySellerOffer(tradeOffer);
            if(trade!=null) {
                sellOffers.add(trade);
            }

        }
        return sellOffers;
    }

    @Transactional
    public List<Trade> getUserBuyTrades(String login) {
        User user = userService.getUser(login);
        List<TradeOffer> acceptedTradeOfferList = user.getListTradeOffers().stream()
                .filter(tradeOffer -> tradeOffer.getStatus().equals(OfferStatus.POSITIVE_REALIZED))
                .collect(Collectors.toList());
        List<Trade> buyOffers = new ArrayList<>();
        for (TradeOffer tradeOffer : acceptedTradeOfferList) {
            Trade trade = tradeRepository.findTradeByBuyerOffer(tradeOffer);
            if(trade!=null) {
                buyOffers.add(trade);
            }

        }
        return buyOffers;
    }

    @Transactional
    public List<Trade> getGroupTrades(String login) {
        User user = userService.getUser(login);
        List<User> userList = user.getGroup().getUsersList();
        List<Trade> buyOffers = new ArrayList<>();

        for (User u : userList) {
            buyOffers.addAll(getUserTrades(u.getLogin()));
        }

        return buyOffers;
    }




    public Trade getTradeFromOffer(TradeOffer offer) {
        Trade trade = tradeRepository.findTradeBySellerOffer(offer);
        if (trade==null) {
            trade = tradeRepository.findTradeByBuyerOffer(offer);
        }

        if (trade == null) {
            throw new RuntimeException("Offer is not realized.");
        }
        return trade;
    }

}
