package com.berksozcu.controller;

import com.berksozcu.controller.base.RootEntity;
import com.berksozcu.entity.PrintJob;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IPrintJobController {
    public ResponseEntity<byte[]> generateQr(@PathVariable Long jobId) throws IOException, WriterException, InterruptedException;

    public ResponseEntity<byte[]> getTextPdf(@PathVariable Long jobId) throws Exception;

    public ResponseEntity<byte[]> generateBarcode(@PathVariable(name = "jobId") Long jobId) throws IOException, InterruptedException, ExecutionException;

    public RootEntity<PrintJob> generateJob(@RequestBody PrintJob printJob);
    public RootEntity<List<PrintJob>> findAllJobs();
}
