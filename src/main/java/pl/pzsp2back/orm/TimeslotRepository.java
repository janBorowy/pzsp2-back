package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;


public interface TimeslotRepository extends CrudRepository<TimeSlot, Long> {
    TimeSlot findByScheduleAndStartTimeAndAndBaseSlotQuantity(Schedule schedule, LocalDateTime startTime, Integer slotQuantity);
}
