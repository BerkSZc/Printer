package com.berksozcu.model;

import com.berksozcu.entity.PrintJobStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintJobCreatedEvent implements Serializable {
    private Long jobId;
    private PrintJobStatus printJobStatus;
    private String path;
    private String status;
}
