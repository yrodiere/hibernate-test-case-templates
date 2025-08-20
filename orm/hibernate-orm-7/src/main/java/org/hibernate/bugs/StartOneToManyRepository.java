package org.hibernate.bugs;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import java.util.UUID;

@Repository
public interface StartOneToManyRepository extends CrudRepository<MyEntity, Long> {
}