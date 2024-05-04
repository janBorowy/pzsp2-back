package com.example.timetraderapi.repository;

import com.example.timetraderapi.entity.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long>, CrudRepository<Group, Long> {
}
