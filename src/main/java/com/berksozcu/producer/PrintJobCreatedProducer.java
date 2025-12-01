package com.berksozcu.producer;

import com.berksozcu.entity.PrintJobStatus;
import com.berksozcu.model.PrintJobCreatedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PrintJobCreatedProducer {

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    @Value("${sr.rabbit.routing.text}")
    private String textRoutingName;

    @Value("${sr.rabbit.routing.qr}")
    private String qrRoutingName;

    @Value("${sr.rabbit.routing.barcode}")
    private String barcodeRoutingName;

    @Autowired
    private AmqpTemplate template;

    public void sendPdfCreatedEvent(PrintJobCreatedEvent event){
        if(event.getPrintJobStatus() == PrintJobStatus.TEXT) {
            template.convertAndSend(exchangeName, textRoutingName, event);
        }

        else if(event.getPrintJobStatus() == PrintJobStatus.QR) {
            template.convertAndSend(exchangeName, qrRoutingName, event);
        }

        else if(event.getPrintJobStatus() == PrintJobStatus.BARCODE) {
            template.convertAndSend(exchangeName, barcodeRoutingName, event);
        }

        System.out.println("PrintJob gönderildi: " + event);
    }
}
