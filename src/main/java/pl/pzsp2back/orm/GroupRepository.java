package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long>, CrudRepository<Group, Long> {
    Group findByName(String name);
}
