package pl.pzsp2back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final TimeslotRepository timeslotRepository;

    @Autowired
    public DatabaseLoader(GroupRepository groupRepository, UserRepository userRepository, ScheduleRepository scheduleRepository,
                          TimeslotRepository timeslotRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        Group test_group = this.groupRepository.findByName("Test");

        if (test_group == null) {
            this.groupRepository.save(new Group(null, "Test", null, null));
            test_group = this.groupRepository.findByName("Test");
        }

        User admin = new User("admin", User.PASSWORD_ENCODER.encode("adminpass"), true, 0, "admin@admin.com", "Marek", "Racibor", test_group, null);
        User worker1 =
                new User("worker1", User.PASSWORD_ENCODER.encode("password1"), false, 100, "worker@worker.com", "Adam", "Camerun", test_group, null);
        User worker2 =
                new User("worker2", User.PASSWORD_ENCODER.encode("password2"), false, 300, "worker1@worker.com", "Pawel", "Mata", test_group, null);

        this.userRepository.save(admin);
        this.userRepository.save(worker1);
        this.userRepository.save(worker2);

        Schedule test_schedule = this.scheduleRepository.findByTag("test");
        if (test_schedule == null) {
            // id, baseSlotLength, name, tag, group, timeSlotList
            this.scheduleRepository.save(new Schedule(null, 60, "19 week", "test", test_group, null));
            test_schedule = this.scheduleRepository.findByTag("19 week");
            //
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 6, 10, 30), 4, 0, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 7, 8, 30), 6, 0, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 8, 11, 00), 2, 100, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 9, 30), 5, 50, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 10, 15, 30), 8, 200, worker1, test_schedule, null));

            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 7, 8, 30), 10, 1000, worker2, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 8, 0), 3, 20, worker2, test_schedule, null));
        }
    }
}
