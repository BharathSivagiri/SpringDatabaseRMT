package com.bharathsivaraman.SpringDatabaseRMT.repo;

import com.bharathsivaraman.SpringDatabaseRMT.entities.Log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long>
{

}
