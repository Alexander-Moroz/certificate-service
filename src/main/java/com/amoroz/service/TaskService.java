package com.amoroz.service;

import com.amoroz.entity.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TaskService {
    CompletableFuture<String> createTaskAndGetId(Task certTask);
    String getTaskStatus(Long taskId);
    Optional<Task> getTask(Long taskId);
    String getBase64Cert(Long taskId) throws JsonProcessingException;

    @Scheduled(fixedRate = 60000L)
    @CacheEvict(cacheNames = {"base64cert"})
    void clearBase64certCache();
}