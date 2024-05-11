package pl.pzsp2back.orm;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {}
