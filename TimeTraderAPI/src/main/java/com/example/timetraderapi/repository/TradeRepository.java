package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.Trade;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TradeRepository extends PagingAndSortingRepository<Trade, Long> {
}
