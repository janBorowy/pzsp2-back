package com.example.timetraderapi;

import com.example.timetraderapi.entity.Group;
import com.example.timetraderapi.entity.User;
import com.example.timetraderapi.repository.GroupRepository;
import com.example.timetraderapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseLoader(GroupRepository groupRepository, UserRepository userRepository){
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        Iterable<Group> groups = this.groupRepository.findAll();
        Group test_group = null;
        for(Group g : groups)
        {
            if(g.getName().equals("Test"))
            {
                test_group = g;
            }
        }
        if(test_group == null) {
            groups = this.groupRepository.findAll();
            this.groupRepository.save(new Group(101L, "Test", null, null));

            for(Group g : groups)
            {
                if(g.getName().equals("Test"))
                {
                    test_group = g;
                }
            }
        }


        this.userRepository.save(new User("admin", User.PASSWORD_ENCODER.encode("admin"), true, "admin@admin.com", "Marek", "Racibor", test_group, null, null));
        this.userRepository.save(new User("worker", User.PASSWORD_ENCODER.encode("worker"), false, "worker@worker.com", "Adam", "Camerun", test_group, null, null));
        this.userRepository.save(new User("worker1", User.PASSWORD_ENCODER.encode("worker"), false, "worker1@worker.com", "Pawel", "Mata", test_group, null, null));
    }
}
