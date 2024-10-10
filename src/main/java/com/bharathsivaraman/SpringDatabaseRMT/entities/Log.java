package com.bharathsivaraman.SpringDatabaseRMT.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "api_name", nullable = false)
    private String apiName;

    @Column(name = "log_timestamp", nullable = false)
    private LocalDateTime logTimestamp;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "errors_and_feedback_messages")
    private String errors;
}
