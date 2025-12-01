package com.berksozcu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "printJob")
public class PrintJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PrintJobStatus type;

    @Column(name = "content")
    private String content;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "İş durumu")
    @Enumerated(EnumType.STRING)
    private PrinterJobStatus printerJobStatus = PrinterJobStatus.BEKLIYOR;
}
