package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Long> {

    Trade findTradeByBuyerOffer(TradeOffer offer);
    Trade findTradeBySellerOffer(TradeOffer offer);
}
