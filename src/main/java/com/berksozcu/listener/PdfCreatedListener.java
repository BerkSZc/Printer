package com.berksozcu.listener;

import com.berksozcu.model.PrintJobCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class PdfCreatedListener {

    @RabbitListener(queues = "textQueue")
    public void receivePdfCreatedEvent(PrintJobCreatedEvent event) {
        System.out.println("Pdf alındı: " + event);
    }

    @RabbitListener(queues = "qrQueue")
    public void receiveQrCreatedEvent(PrintJobCreatedEvent event) {
        System.out.println("Qr alındı: " + event);
    }

    @RabbitListener(queues = "barcodeQueue")
    public void receiveBarcodeCreatedEvent(PrintJobCreatedEvent event) {
        System.out.println("Barcode alındı: " + event);
    }
}
