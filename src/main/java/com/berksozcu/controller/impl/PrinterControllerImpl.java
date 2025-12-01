package com.berksozcu.controller.impl;

import com.berksozcu.controller.IPrinterController;
import com.berksozcu.controller.base.RestBaseController;
import com.berksozcu.controller.base.RootEntity;
import com.berksozcu.entity.Printer;
import com.berksozcu.service.IPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/api/print")
public class PrinterControllerImpl extends RestBaseController implements IPrinterController {

    @Autowired
   private IPrinterService printerService;

    @Override
    @GetMapping("/find")
    public RootEntity<List<Printer>> findAllAvailablePrinters() {
        return list(printerService.findAllAvailablePrinters()) ;
    }

    @Override
    @GetMapping("/find-all")
    public RootEntity<List<Printer>> findAllPrinters() {
        return list(printerService.findAllPrinters());
    }

    @Override
    @PostMapping("/assign/{id}")
    public RootEntity<Printer> assignPrinter(@RequestBody Printer printer, @PathVariable(name = "id") Long id) {
        return ok(printerService.assignPrinter(printer, id));
    }

}
