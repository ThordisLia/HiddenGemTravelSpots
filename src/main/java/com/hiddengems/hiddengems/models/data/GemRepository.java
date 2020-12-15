package com.hiddengems.hiddengems.models.data;

import com.hiddengems.hiddengems.models.Gem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GemRepository extends CrudRepository<Gem, Integer> {
}
