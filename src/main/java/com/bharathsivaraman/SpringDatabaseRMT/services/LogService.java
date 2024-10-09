package com.bharathsivaraman.SpringDatabaseRMT.services;

import java.time.temporal.TemporalAccessor;

public interface LogService
{
    void logApiCall(String ownerName, String apiName, String errors, TemporalAccessor logTimestamp);
}
