package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface OptimizationProcessRepository extends CrudRepository<OptimizationProcess, Long> {
    OptimizationProcess findOptimizationProcessByOptimizationTime(LocalDateTime optimizationTime);

}
