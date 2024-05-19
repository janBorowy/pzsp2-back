package pl.pzsp2back;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.exceptions.UserAlreadyExistsException;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import pl.pzsp2back.security.AuthService;

@Component
@AllArgsConstructor
@Slf4j
public class DatabaseLoader implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final AuthService authService;
    private final ScheduleRepository scheduleRepository;
    private final TimeslotRepository timeslotRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {

        Group test_group = this.groupRepository.findByName("Test");

        if (test_group == null) {
            test_group = this.groupRepository.save(new Group(null, "Test", null, null));;
        }



        var adminDto = new SignUpDto("admin", "adminpass", "admin@admin.com", "Marek", "Racibor", test_group.getId(), 100);
        var worker1Dto =
                new SignUpDto("worker1", "password1", "worker@worker.com", "Adam", "Camerun", test_group.getId(), 100);
        var worker2Dto =
                new SignUpDto("worker2", "password2", "worker1@worker.com", "Pawel", "Mata", test_group.getId(), 300);

        tryToSignUp(adminDto);
        tryToSignUp(worker1Dto);
        tryToSignUp(worker2Dto);

        var admin = userRepository.findById(adminDto.login()).get();
        var worker1 = userRepository.findById(worker1Dto.login()).get();
        var worker2 = userRepository.findById(worker2Dto.login()).get();
        admin.setIfAdmin(true);
        userRepository.save(admin);

        Schedule test_schedule = this.scheduleRepository.findByTag("test");
        if (test_schedule == null) {
            // id, baseSlotLength, name, tag, group, timeSlotList
            test_schedule = this.scheduleRepository.save(new Schedule(null, 60, "19 week", "test", test_group, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 6, 10, 30), 4, 0, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 7, 8, 30), 6, 0, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 8, 11, 0), 2, 1000, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 9, 30), 5, 50, worker1, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 10, 15, 30), 8, 200, worker1, test_schedule, null));

            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 8, 11, 0), 2, 1000, worker2, test_schedule, null));
            this.timeslotRepository.save(
                    new TimeSlot(null, LocalDateTime.of(2024, 5, 9, 8, 0), 3, 20, worker2, test_schedule, null));
        }
    }

    private void tryToSignUp(SignUpDto dto) {
        try {
            authService.signUp(dto);
        } catch (UserAlreadyExistsException e) {
            log.warn("Could not create user: %s, %s".formatted(dto.login(), e.getMessage()));
        }
    }
}
