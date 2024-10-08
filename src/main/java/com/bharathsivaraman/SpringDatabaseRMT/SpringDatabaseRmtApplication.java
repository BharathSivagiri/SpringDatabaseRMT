package com.bharathsivaraman.SpringDatabaseRMT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO) //Spring Web data support
@EnableScheduling
public class SpringDatabaseRmtApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(SpringDatabaseRmtApplication.class, args);
	}
}
