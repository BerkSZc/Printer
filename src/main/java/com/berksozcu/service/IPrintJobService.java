package com.berksozcu.service;


import com.berksozcu.controller.base.RootEntity;
import com.berksozcu.entity.PrintJob;
import com.berksozcu.entity.Printer;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IPrintJobService {
    public void generateQr(Long jobId) throws IOException, WriterException;

    public CompletableFuture<Path> generateBarcode(Long jobId);

    public PrintJob generateJob(PrintJob printJob);

    public CompletableFuture<Path> generateTextToPdf(Long jobId) throws Exception;

    public List<PrintJob> findAllJobs();
}
