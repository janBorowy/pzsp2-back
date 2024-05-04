package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String>, CrudRepository<User, String> {

}
