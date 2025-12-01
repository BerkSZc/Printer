package com.berksozcu.controller;

import com.berksozcu.controller.base.RootEntity;
import com.berksozcu.entity.Printer;
import com.berksozcu.entity.PrinterStatus;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IPrinterController {
    public RootEntity<List<Printer>> findAllAvailablePrinters();
    public RootEntity<List<Printer>> findAllPrinters();
    public RootEntity<Printer> assignPrinter(Printer printer ,@PathVariable(name = "id") Long id);
}
