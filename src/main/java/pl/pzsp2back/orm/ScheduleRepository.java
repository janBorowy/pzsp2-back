package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
    Schedule findByTag(String tag);
}
