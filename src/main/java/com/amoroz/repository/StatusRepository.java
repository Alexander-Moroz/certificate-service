package com.amoroz.repository;

import com.amoroz.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}