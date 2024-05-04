package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.Schedule;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}
