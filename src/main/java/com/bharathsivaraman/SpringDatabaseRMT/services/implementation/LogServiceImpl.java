package com.bharathsivaraman.SpringDatabaseRMT.services.implementation;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Log;
import com.bharathsivaraman.SpringDatabaseRMT.repo.LogRepository;
import com.bharathsivaraman.SpringDatabaseRMT.services.LogService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService
{
    @Autowired
    private LogRepository logRepository;

    @Override
    public void logApiCall(String ownerName, String apiName, String errors, TemporalAccessor logTimestamp)
    {
        Log log = new Log();
        log.setApiName(apiName);
        log.setUserName(ownerName);
        log.setErrors(errors);
        log.setLogTimestamp(LocalDateTime.now(ZoneId.from(logTimestamp)));
        logRepository.save(log);
    }
}
