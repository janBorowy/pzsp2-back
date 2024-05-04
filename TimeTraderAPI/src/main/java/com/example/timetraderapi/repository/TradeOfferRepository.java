package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.TradeOffer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TradeOfferRepository extends PagingAndSortingRepository<TradeOffer, Long> {
}
