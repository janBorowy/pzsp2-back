package pl.pzsp2back;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.pzsp2back.dto.SignUpDto;
import pl.pzsp2back.dto.TimeSlotDto;
import pl.pzsp2back.dto.TradeOfferDto;
import pl.pzsp2back.dto.UserShortDto;
import pl.pzsp2back.dtoPost.OptimizationProcessPostDto;
import pl.pzsp2back.dtoPost.TradeOfferPostDto;
import pl.pzsp2back.exceptions.UserAlreadyExistsException;
import pl.pzsp2back.orm.*;

import java.sql.Time;
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
                new SignUpDto("worker4", "password4", "worker4@worker.com", "Mikołaj", "Mikołaj", testGroup.getId(), 300);



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


            OptimizationProcessPostDto op1 = new OptimizationProcessPostDto(LocalDateTime.of(2024, 5, 11, 23,50), null);
            OptimizationProcess op = this.optimizationProcessService.createOptimizationProcess(op1, "admin");



            //TradeOfferPostDto tradeOffer6 = new TradeOfferPostDto(22, ts10.getId(), false, op.getId());
            //this.tradeOfferService.createTradeOffer(tradeOffer6, "worker3"); //canUp worker 3

            //TradeOfferPostDto tradeOffer7 = new TradeOfferPostDto(15, ts7.getId(), true, op.getId());
            //this.tradeOfferService.createTradeOffer(tradeOffer7, "worker3"); //wantDown worker 3

            //TradeOfferPostDto tradeOffer8 = new TradeOfferPostDto(10, ts3.getId(), true, op.getId());
            //this.tradeOfferService.createTradeOffer(tradeOffer8, "worker2"); //wantDown worker2

            //TradeOfferPostDto tradeOffer8 = new TradeOfferPostDto(43, ts7.getId(), false, op.getId());
            //this.tradeOfferService.createTradeOffer(tradeOffer8, "worker2"); //canUp worker 2

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