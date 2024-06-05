package pl.pzsp2back;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.dto.UserShortDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.UserAlreadyExistsException;
import pl.pzsp2back.orm.*;

import java.time.LocalDateTime;
import java.util.*;

import pl.pzsp2back.security.AuthService;
import pl.pzsp2back.services.OptimizationProcessService;
import pl.pzsp2back.services.TimeSlotService;
import pl.pzsp2back.services.TradeOfferService;

@Component
@AllArgsConstructor
@Slf4j
public class DatabaseLoader implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final AuthService authService;
    private final ScheduleRepository scheduleRepository;
    private final TimeslotRepository timeslotRepository;
    private final UserRepository userRepository;
    private final OptimizationProcessRepository optimizationProcessRepository;
    private final TradeOfferRepository tradeOfferRepository;
    private final TradeRepository tradeRepository;

    private final TimeSlotService timeSlotService;
    private final TradeOfferService tradeOfferService;
    private final OptimizationProcessService optimizationProcessService;

    @Override
    public void run(String... strings) throws Exception {

        Group testGroup = this.groupRepository.findByName("Test");

        if (testGroup == null) {
            testGroup = this.groupRepository.save(new Group(null, "Test", null, null));;
        }


        var adminDto = new SignUpDto("admin", "adminpass", "admin@admin.com", "Marek", "Racibor", testGroup.getId(), 100);
        var worker1SignUpDto =
                new SignUpDto("worker1", "password1", "worker@worker.com", "Adam", "Camerun", testGroup.getId(), 100);
        var worker2SignUpDto =
                new SignUpDto("worker2", "password2", "worker1@worker.com", "Pawel", "Mata", testGroup.getId(), 300);
        var worker3SignUpDto =
                new SignUpDto("worker3", "password3", "worker3@worker.com", "Robert", "Robert", testGroup.getId(), 100);
        var worker4SignUpDto =
                new SignUpDto("worker4", "password4", "worker4@worker.com", "Mikolaj", "Lukasz", testGroup.getId(), 300);



        tryToSignUp(adminDto);
        tryToSignUp(worker1SignUpDto);
        tryToSignUp(worker2SignUpDto);
        tryToSignUp(worker3SignUpDto);
        tryToSignUp(worker4SignUpDto);

        UserShortDto worker1Dto = new UserShortDto(worker1SignUpDto.login(), worker1SignUpDto.name(), worker1SignUpDto.surname());
        UserShortDto worker2Dto = new UserShortDto(worker2SignUpDto.login(), worker2SignUpDto.name(), worker2SignUpDto.surname());
        UserShortDto worker3Dto = new UserShortDto(worker3SignUpDto.login(), worker3SignUpDto.name(), worker3SignUpDto.surname());
        UserShortDto worker4Dto = new UserShortDto(worker4SignUpDto.login(), worker4SignUpDto.name(), worker4SignUpDto.surname());

        var admin = userRepository.findById(adminDto.login()).get();
        var worker1 = userRepository.findById(worker1SignUpDto.login()).get();
        var worker2 = userRepository.findById(worker2SignUpDto.login()).get();
        var worker3 = userRepository.findById(worker3SignUpDto.login()).get();
        var worker4 = userRepository.findById(worker4SignUpDto.login()).get();
        admin.setIfAdmin(true);
        userRepository.save(admin);

        Schedule testSchedule = this.scheduleRepository.findByTag("test");
        if (testSchedule == null) {
            // id, baseSlotLength, name, tag, group, timeSlotList
            testSchedule = this.scheduleRepository.save(new Schedule(null, 60, "19 week", "test", testGroup, null, null));

            OptimizationProcessPostDto op1 = new OptimizationProcessPostDto(LocalDateTime.of(2024, 5, 11, 23,50), null);
            OptimizationProcess op = this.optimizationProcessService.createOptimizationProcess(op1, "admin");


            List<UserShortDto> worker1DtoList = new ArrayList<>();
            worker1DtoList.add(worker1Dto);

            List<UserShortDto> worker2DtoList = new ArrayList<>();
            worker2DtoList.add(worker2Dto);

            List<UserShortDto> worker3DtoList = new ArrayList<>();
            worker3DtoList.add(worker3Dto);

            List<UserShortDto> worker4DtoList = new ArrayList<>();
            worker4DtoList.add(worker4Dto);

            List<UserShortDto> worker1worker2DtoList = new ArrayList<>();
            worker1worker2DtoList.add(worker1Dto);
            worker1worker2DtoList.add(worker2Dto);



            TimeSlotDto timeslot1Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 6, 10, 30), 1, 0, null, null, worker1DtoList, null);
            TimeSlot ts1 = timeSlotService.createTimeSlot(timeslot1Dto);

            TimeSlotDto timeslot2Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 7, 8, 30), 6, 0, null, null, worker1DtoList, null);
            TimeSlot ts2 = this.timeSlotService.createTimeSlot(timeslot2Dto);


            TimeSlotDto timeslot3Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 8, 11, 0), 2, 0, null, null, worker1worker2DtoList, null);
            TimeSlot ts3 = this.timeSlotService.createTimeSlot(timeslot3Dto);

            TimeSlotDto timeslot4Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 9, 9, 30), 5, 0, null, null, worker4DtoList, null);
            TimeSlot ts4 = timeSlotService.createTimeSlot(timeslot4Dto);

            TimeSlotDto timeslot5Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 10, 15, 30), 8, 0, null, null, worker3DtoList, null);
            TimeSlot ts5 = this.timeSlotService.createTimeSlot(timeslot5Dto);

            TimeSlotDto timeslot6Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 9, 8, 0), 3, 0, null, null, worker2DtoList, null);
            TimeSlot ts6 = this.timeSlotService.createTimeSlot(timeslot6Dto);

            TimeSlotDto timeslot7Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 9, 13, 0), 3, 0, null, null, worker3DtoList, null);
            TimeSlot ts7 = this.timeSlotService.createTimeSlot(timeslot7Dto);

            TimeSlotDto timeslot8Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 6, 13, 0), 3, 0, null, null, worker3DtoList, null);
            TimeSlot ts8 = this.timeSlotService.createTimeSlot(timeslot8Dto);

            TimeSlotDto timeslot9Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 6, 18, 0), 3, 0, null, null, worker4DtoList, null);
            TimeSlot ts9 = this.timeSlotService.createTimeSlot(timeslot9Dto);

            TimeSlotDto timeslot10Dto = new TimeSlotDto(null, LocalDateTime.of(2024, 5, 7, 15, 0), 3, 0, null, null, worker4DtoList, null);
            TimeSlot ts10 = this.timeSlotService.createTimeSlot(timeslot10Dto);



            OfferStatus o = OfferStatus.ACTIVE;
            TradeOfferPostDto tradeOffer1 = new TradeOfferPostDto(10, ts1.getId(), true, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer1, "worker1"); //wantDown worker1

            TradeOfferPostDto tradeOffer2 = new TradeOfferPostDto(10, ts1.getId(), false, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer2, "worker2"); //canUp worker 2

            TradeOfferPostDto tradeOffer6 = new TradeOfferPostDto(10, ts6.getId(), true, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer6, "worker2"); //wantDown worker 2

            TradeOfferPostDto tradeOffer3 = new TradeOfferPostDto(10, ts4.getId(), false, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer3, "worker1"); //canUp worker 1

            TradeOfferPostDto tradeOffer4 = new TradeOfferPostDto(10, ts4.getId(), true, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer4, "worker4"); //wantDown worker 4

            TradeOfferPostDto tradeOffer5 = new TradeOfferPostDto(10, ts6.getId(), false, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer5, "worker4"); //canUp worker 4

            TradeOfferPostDto tradeOffer10 = new TradeOfferPostDto(20, ts2.getId(), false, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer10, "worker3"); //canUp worker 3

            TradeOfferPostDto tradeOffer7 = new TradeOfferPostDto(20, ts8.getId(), true, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer7, "worker3"); //wantDown worker 3

            TradeOfferPostDto tradeOffer8 = new TradeOfferPostDto(20, ts2.getId(), true, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer8, "worker1"); //wantDown worker1

            TradeOfferPostDto tradeOffer9 = new TradeOfferPostDto(20, ts8.getId(), false, op.getId());
            this.tradeOfferService.createTradeOffer(tradeOffer9, "worker1"); //canUp worker 1

        }

        Group testGroup2 = this.groupRepository.findByName("Test2");

        if (testGroup2 == null) {
            testGroup2 = this.groupRepository.save(new Group(null, "Test2", null, null));;
        }


        var admin2Dto = new SignUpDto("admin2", "adminpass2", "admin@admin.com", "Marek2", "Racibor2", testGroup2.getId(), 5000);
        var worker12SignUpDto =
                new SignUpDto("worker12", "password12", "worker12@worker.com", "Adam2", "Camerun2", testGroup2.getId(), 1000);
        var worker22SignUpDto =
                new SignUpDto("worker22", "password22", "worker22@worker.com", "Pawel2", "Mata2", testGroup2.getId(), 1000);
        var worker32SignUpDto =
                new SignUpDto("worker32", "password32", "worker32@worker.com", "Robert2", "Robert2", testGroup2.getId(), 1000);




        tryToSignUp(admin2Dto);
        tryToSignUp(worker12SignUpDto);
        tryToSignUp(worker22SignUpDto);
        tryToSignUp(worker32SignUpDto);


        UserShortDto worker12Dto = new UserShortDto(worker1SignUpDto.login(), worker1SignUpDto.name(), worker1SignUpDto.surname());
        UserShortDto worker22Dto = new UserShortDto(worker2SignUpDto.login(), worker2SignUpDto.name(), worker2SignUpDto.surname());
        UserShortDto worker32Dto = new UserShortDto(worker3SignUpDto.login(), worker3SignUpDto.name(), worker3SignUpDto.surname());

        var admin2 = userRepository.findById(adminDto.login()).get();
        var worker12 = userRepository.findById(worker12SignUpDto.login()).get();
        var worker22 = userRepository.findById(worker22SignUpDto.login()).get();
        var worker32 = userRepository.findById(worker32SignUpDto.login()).get();
        admin2.setIfAdmin(true);
        userRepository.save(admin2);




        Schedule testSchedule2 = this.scheduleRepository.findByTag("test2");
        if (testSchedule2 == null) {
            // id, baseSlotLength, name, tag, group, timeSlotList
            testSchedule2 = this.scheduleRepository.save(new Schedule(null, 60, "Schedule for small group", "test2", testGroup2, null, null));

            OptimizationProcessPostDto op12 = new OptimizationProcessPostDto(LocalDateTime.of(2024, 5, 11, 23,50), null);
            OptimizationProcess op2 = this.optimizationProcessService.createOptimizationProcess(op12, "admin2");

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
