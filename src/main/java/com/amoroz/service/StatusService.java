package com.amoroz.service;

import com.amoroz.entity.Status;
import com.amoroz.repository.StatusRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService {
    private static final Logger LOGGER = LogManager.getLogger(StatusService.class);
    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Cacheable(value = "status")
    public Status getStatusByName(String name) {
        Optional<Status> optionalStatus = statusRepository.findFirstByName(name);
        if (optionalStatus.isPresent()) {
            return optionalStatus.get();
        }
        return null;
    }

    @Cacheable(value = "status")
    public Status getStatusById(int id) {
        Optional<Status> optionalStatus = statusRepository.findById(id);
        if (optionalStatus.isPresent()) {
            return optionalStatus.get();
        }
        return null;
    }

    @CacheEvict("status")
    public void clearCache(){
        LOGGER.debug("Clear status cache");
    }
}