package com.example.timetraderapi;

import com.example.timetraderapi.entity.Group;
import com.example.timetraderapi.entity.Schedule;
import com.example.timetraderapi.entity.TimeSlot;
import com.example.timetraderapi.entity.User;
import com.example.timetraderapi.repository.GroupRepository;
import com.example.timetraderapi.repository.ScheduleRepository;
import com.example.timetraderapi.repository.TimeslotRepository;
import com.example.timetraderapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            this.groupRepository.save(new Group(101L, "Test", null, null));
            test_group = this.groupRepository.findByName("Test");
        }

        User admin = new User("admin", User.PASSWORD_ENCODER.encode("admin"), true, 0, "admin@admin.com", "Marek", "Racibor", test_group, null, null);
        User worker1 =
                new User("worker", User.PASSWORD_ENCODER.encode("worker"), false, 100, "worker@worker.com", "Adam", "Camerun", test_group, null,
                        null);
        User worker2 =
                new User("worker1", User.PASSWORD_ENCODER.encode("worker"), false, 300, "worker1@worker.com", "Pawel", "Mata", test_group, null,
                        null);

        this.userRepository.save(admin);
        this.userRepository.save(worker1);
        this.userRepository.save(worker2);

        Schedule test_schedule = this.scheduleRepository.findByTag("19 week");
        if (test_schedule == null) {
            this.scheduleRepository.save(new Schedule(null, "19 week", test_group, null));
            test_schedule = this.scheduleRepository.findByTag("19 week");
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 6, 10, 30), LocalDateTime.of(2024, 5, 6, 14, 30), worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 7, 8, 30), LocalDateTime.of(2024, 5, 6, 14, 0), worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 8, 11, 00), LocalDateTime.of(2024, 5, 6, 15, 0), worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 9, 30), LocalDateTime.of(2024, 5, 6, 13, 30), worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 10, 15, 30), LocalDateTime.of(2024, 5, 6, 18, 30), worker1, test_schedule, null));

            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 7, 8, 30), LocalDateTime.of(2024, 5, 6, 16, 30), worker2, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 8, 0), LocalDateTime.of(2024, 5, 6, 16, 0), worker2, test_schedule, null));
        }
    }
}
