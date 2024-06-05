package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeOfferRepository extends CrudRepository<TradeOffer, Long> {
    TradeOffer findTradeOfferByOptimizationProcessAndOfferOwnerAndTimeslot(OptimizationProcess optimizationProcess, User user, TimeSlot timeSlot);
    TradeOffer findByOfferOwnerAndTimeslot(User offerOwner, TimeSlot timeslot);
    TradeOffer findTradeOfferByOfferOwnerAndTimeslot(User offerOwner, TimeSlot timeSlot);
}
