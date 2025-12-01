package com.berksozcu.service.impl;

import com.berksozcu.entity.PrintJob;
import com.berksozcu.entity.Printer;
import com.berksozcu.entity.PrinterStatus;
import com.berksozcu.exception.BaseException;
import com.berksozcu.exception.ErrorMessage;
import com.berksozcu.exception.MessageType;
import com.berksozcu.repository.PrinterRepository;
import com.berksozcu.service.IPrinterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrinterServiceImpl implements IPrinterService {

    @Autowired
    private PrinterRepository printerRepository;


    @Override
    public List<Printer> findAllAvailablePrinters() {
        List<Printer> allPrinters = printerRepository.findAll();

       return allPrinters.stream()
               .filter(printer -> printer.getStatus() == PrinterStatus.BOS).toList();
    }

    @Override
    public List<Printer> findAllPrinters() {
        return printerRepository.findAll();
    }

    @Override
    public Printer assignPrinter(Printer printer, Long id) {
        Optional<Printer> optional = printerRepository.findById(id);
        if(optional.isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.YAZICI_MEVCUT));
        }
       printer.setName(printer.getName());
        printer.setStatus(printer.getStatus());
        printerRepository.save(printer);
        return printer;

    }


}
