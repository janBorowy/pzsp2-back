package pl.pzsp2back;

import org.junit.jupiter.api.Test;

import pl.pzsp2back.dto.GroupedTimeSlotDto;
import pl.pzsp2back.dto.ShortTimeSlotInfoDto;
import pl.pzsp2back.orm.TimeSlot;
import pl.pzsp2back.orm.User;
import pl.pzsp2back.services.TimeSlotService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeSlotServiceTest {

    @Test
    public void should_group_timeslots_in_order(){
        //given
        var user = new User("test_user", "passwd", false, 1000, "mail@mail.pl", "Test", "User", null, null );
        var ts1 = new TimeSlot(1L, LocalDateTime.of(2024, 5, 18, 12, 0), 10, 100, user, null, null);
        var ts2 = new TimeSlot(2L, LocalDateTime.of(2024, 5, 18, 10, 0), 8, 100, user, null, null);
        var ts3 = new TimeSlot(3L, LocalDateTime.of(2024, 5, 18, 10, 45), 8, 100, user, null, null);
        var ts4 = new TimeSlot(4L, LocalDateTime.of(2024, 5, 18, 10, 40), 8, 100, user, null, null);
        List<TimeSlot> tsList= new ArrayList<>();
        tsList.add(ts1);
        tsList.add(ts2);
        tsList.add(ts3);
        tsList.add(ts4);

        //when
        var result = TimeSlotService.mapToGroupedTimeSlotsDto(tsList);

        //then
        List<TimeSlot> tsListExpected = new ArrayList<>();
        tsListExpected.add(ts2);
        tsListExpected.add(ts4);
        tsListExpected.add(ts3);
        tsListExpected.add(ts1);

        List<GroupedTimeSlotDto> groupedTimeSlotDtosExpected= new ArrayList<>();


        List<ShortTimeSlotInfoDto> stsiDto1 = new ArrayList<>();
        stsiDto1.add(new ShortTimeSlotInfoDto(1L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto1 = new GroupedTimeSlotDto(4L, LocalDateTime.of(2024, 5, 18, 12, 0), 10, 100, 1, false, stsiDto1);

        List<ShortTimeSlotInfoDto> stsiDto2 = new ArrayList<>();
        stsiDto2.add(new ShortTimeSlotInfoDto(2L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto2 = new GroupedTimeSlotDto(1L, LocalDateTime.of(2024, 5, 18, 10, 0), 8, 100, 1, false, stsiDto2);

        List<ShortTimeSlotInfoDto> stsiDto3 = new ArrayList<>();
        stsiDto3.add(new ShortTimeSlotInfoDto(3L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto3 = new GroupedTimeSlotDto(3L, LocalDateTime.of(2024, 5, 18, 10, 45), 8, 100, 1, false, stsiDto3);

        List<ShortTimeSlotInfoDto> stsiDto4 = new ArrayList<>();
        stsiDto4.add(new ShortTimeSlotInfoDto(4L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto4 = new GroupedTimeSlotDto(2L, LocalDateTime.of(2024, 5, 18, 10, 40), 8, 100, 1, false, stsiDto4);

        groupedTimeSlotDtosExpected.add(gtsDto2);
        groupedTimeSlotDtosExpected.add(gtsDto4);
        groupedTimeSlotDtosExpected.add(gtsDto3);
        groupedTimeSlotDtosExpected.add(gtsDto1);


        //ifIsSorted
        System.out.println("SIZE");
        System.out.println(result.get(3).startTime());
        assertThat(tsList.equals(tsListExpected)).isTrue();
        assertThat(result.size()==4).isTrue();
        assertThat(result.equals(groupedTimeSlotDtosExpected)).isTrue();
    }

    @Test
    public void should_group_timeslots_grouping(){
        //given
        var user = new User("test_user", "passwd", false, 1000, "mail@mail.pl", "Test", "User", null, null );
        var user2 = new User("test_user2", "passwd2", false, 1000, "mail2@mail.pl", "Test2", "User2", null, null );
        var ts1 = new TimeSlot(1L, LocalDateTime.of(2024, 5, 18, 12, 0), 10, 100, user, null, null);
        var ts2 = new TimeSlot(2L, LocalDateTime.of(2024, 5, 18, 10, 0), 8, 100, user, null, null);
        var ts3 = new TimeSlot(3L, LocalDateTime.of(2024, 5, 18, 10, 40), 8, 100, user, null, null);
        var ts4 = new TimeSlot(4L, LocalDateTime.of(2024, 5, 18, 10, 40), 8, 100, user2, null, null);
        var ts5 = new TimeSlot(5L, LocalDateTime.of(2024, 5, 18, 10, 40), 5, 100, user2, null, null);

        List<TimeSlot> tsList = new ArrayList<>();

        tsList.add(ts1);
        tsList.add(ts2);
        tsList.add(ts3);
        tsList.add(ts4);
        tsList.add(ts5);

        //when
        var result = TimeSlotService.mapToGroupedTimeSlotsDto(tsList);

        //then

        List<GroupedTimeSlotDto> groupedTimeSlotDtosExpected= new ArrayList<>();

        List<ShortTimeSlotInfoDto> stsiDto1 = new ArrayList<>();
        stsiDto1.add(new ShortTimeSlotInfoDto(2L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto1 = new GroupedTimeSlotDto(1L, LocalDateTime.of(2024, 5, 18, 10, 0), 8, 100, 1, false, stsiDto1);

        List<ShortTimeSlotInfoDto> stsiDto2 = new ArrayList<>();
        stsiDto2.add(new ShortTimeSlotInfoDto(3L, user.getLogin(), user.getName(), user.getSurname()));
        stsiDto2.add(new ShortTimeSlotInfoDto(4L, user2.getLogin(), user2.getName(), user2.getSurname()));
        var gtsDto2 = new GroupedTimeSlotDto(2L, LocalDateTime.of(2024, 5, 18, 10, 40), 8, 100, 2, false, stsiDto2);

        List<ShortTimeSlotInfoDto> stsiDto3 = new ArrayList<>();
        stsiDto3.add(new ShortTimeSlotInfoDto(5L, user2.getLogin(), user2.getName(), user2.getSurname()));
        var gtsDto3 = new GroupedTimeSlotDto(3L, LocalDateTime.of(2024, 5, 18, 10, 40), 5, 100, 1, false, stsiDto3);

        List<ShortTimeSlotInfoDto> stsiDto4 = new ArrayList<>();
        stsiDto4.add(new ShortTimeSlotInfoDto(1L, user.getLogin(), user.getName(), user.getSurname()));
        var gtsDto4 = new GroupedTimeSlotDto(4L, LocalDateTime.of(2024, 5, 18, 12, 0), 10, 100, 1, false, stsiDto4);

        groupedTimeSlotDtosExpected.add(gtsDto1);
        groupedTimeSlotDtosExpected.add(gtsDto2);
        groupedTimeSlotDtosExpected.add(gtsDto3);
        groupedTimeSlotDtosExpected.add(gtsDto4);

        assertThat(result.size()==4).isTrue();
        assertThat(result.equals(groupedTimeSlotDtosExpected)).isTrue();

    }


}
