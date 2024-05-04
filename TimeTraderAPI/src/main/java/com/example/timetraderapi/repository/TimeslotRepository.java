package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.TimeSlot;
import org.springframework.data.repository.CrudRepository;


public interface TimeslotRepository extends CrudRepository<TimeSlot, Long> {

}
