package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OptimizationProcessRepository extends CrudRepository<OptimizationProcess, Long> {
    List<OptimizationProcess> findOptimizationProcessByScheduleAndOfferAcceptanceDeadlineAfterOrderByOfferAcceptanceDeadline(Schedule schedule, LocalDateTime optimizationTime);

}
