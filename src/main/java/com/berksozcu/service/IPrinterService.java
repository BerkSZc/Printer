package com.berksozcu.service;

import com.berksozcu.entity.Printer;
import com.berksozcu.entity.PrinterStatus;

import java.util.List;

public interface IPrinterService {
    public List<Printer> findAllAvailablePrinters();
    public List<Printer> findAllPrinters();
    public Printer assignPrinter(Printer printer, Long id);
}
