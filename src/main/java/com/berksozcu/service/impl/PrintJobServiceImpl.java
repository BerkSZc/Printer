package com.berksozcu.service.impl;

import com.berksozcu.entity.*;
import com.berksozcu.exception.BaseException;
import com.berksozcu.exception.ErrorMessage;
import com.berksozcu.exception.MessageType;
import com.berksozcu.model.PrintJobCreatedEvent;
import com.berksozcu.producer.PrintJobCreatedProducer;
import com.berksozcu.repository.PrintJobRepository;
import com.berksozcu.repository.PrinterRepository;
import com.berksozcu.service.IPrintJobService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PrintJobServiceImpl implements IPrintJobService {

    @Autowired
    private PrinterRepository printerRepository;

    @Autowired
    private PrintJobRepository printJobRepository;

    @Value("${file.output-dir}")
    private String outputDir;

    @Autowired
    private PrintJobCreatedProducer producer;

    @Override
    public PrintJob generateJob(PrintJob printJob) {

        printJob.setContent(printJob.getContent());
        printJob.setType(printJob.getType());
        printJob.setCreatedAt(new Date());
        printJobRepository.save(printJob);
        return printJob;
    }

    @Override
    public List<PrintJob> findAllJobs() {
        return printJobRepository.findAll();
    }

    @Override
    @Transactional
    public void generateQr(Long jobId) throws IOException, WriterException {
        Printer printer = printerRepository.findFirstByStatus(PrinterStatus.BOS)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.PRINTER_NOT_FOUND)));

        PrintJob printJob = printJobRepository.findById(jobId).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        if (printJob.getType() != PrintJobStatus.QR) {
            throw new BaseException(new ErrorMessage(MessageType.YANLIS_TIP));
        }

        printer.setStatus(PrinterStatus.KULLANIMDA);
        printJob.setPrinterJobStatus(PrinterJobStatus.ISLEMDE);
        printerRepository.save(printer);
        printJobRepository.save(printJob);

        try {
            processQRJob(printJob);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            printer.setStatus(PrinterStatus.BOS);
            printJob.setPrinterJobStatus(PrinterJobStatus.HAZIR);
            printerRepository.save(printer);
            printJobRepository.save(printJob);
        }
        producer.sendPdfCreatedEvent(
                new PrintJobCreatedEvent(
                        printJob.getId(),
                        PrintJobStatus.QR,
                        null,
                        "QR_HAZIR"
                )
        );

    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Path> generateBarcode(Long jobId) {
        Path barcodePath = null;

        Printer printer = printerRepository.findFirstByStatus(PrinterStatus.BOS)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.PRINTER_NOT_FOUND)));

        PrintJob printJob = printJobRepository.findById(jobId).orElseThrow(()
                -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        if (printJob.getType() != PrintJobStatus.BARCODE) {
            throw new BaseException(new ErrorMessage(MessageType.YANLIS_TIP));
        }

        printer.setStatus(PrinterStatus.KULLANIMDA);
        printJob.setPrinterJobStatus(PrinterJobStatus.ISLEMDE);
        printerRepository.save(printer);
        printJobRepository.save(printJob);

        try {
                barcodePath = processBarcodeJob(printJob);
                printer.setStatus(PrinterStatus.BOS);
                printerRepository.save(printer);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                printer.setStatus(PrinterStatus.BOS);
                printJob.setPrinterJobStatus(PrinterJobStatus.HAZIR);
                printerRepository.save(printer);
                printJobRepository.save(printJob);
            }

//manuel olarak yeni bir Thread (iş parçacığı) oluşturmak ve çalıştırmak anlamına gelir.
//Asenkron çalışma
//Uzun süren işlemleri ana thread’i bloke etmeden çalıştırır
//Kullanıcıya hızlı cevap, arka planda işleme imkanı sağlar
// Sektörde fazla kullanılmaz.
//        new Thread(() -> {
//            try {
//                processBarcodeJob(printJob);
//                printer.setStatus(PrinterStatus.BOS);
//                printerRepository.save(printer);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            } finally {
//                printer.setStatus(PrinterStatus.BOS);
//                printJob.setPrinterJobStatus(PrinterJobStatus.HAZIR);
//                printerRepository.save(printer);
//                printJobRepository.save(printJob);
//            }
//        })
        //Thread’i başlatır, kod arka planda çalışır
//                .start();
//    }


        assert barcodePath != null;
        producer.sendPdfCreatedEvent(
                new PrintJobCreatedEvent(
                        printJob.getId(),
                        PrintJobStatus.BARCODE,
                        barcodePath.toString(),
                        "BARCODE_HAZIR"
                )
        );



        return CompletableFuture.completedFuture(barcodePath);
    }




    @Override
    @Transactional
    @Async
//CompletableFuture asenkron işlemleri yapmak için sektörede güncel olarak kullanılır.
    public CompletableFuture<Path> generateTextToPdf(Long jobId) throws Exception {
        Path pdfPath;

      Printer printer = printerRepository.findFirstByStatus(PrinterStatus.BOS)
            .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.PRINTER_NOT_FOUND)));


        PrintJob printJob = printJobRepository.findById(jobId).orElseThrow(()
                -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        if (printJob.getType() != PrintJobStatus.TEXT) {
            throw new BaseException(new ErrorMessage(MessageType.YANLIS_TIP));
        }

        printer.setStatus(PrinterStatus.KULLANIMDA);
        printJob.setPrinterJobStatus(PrinterJobStatus.ISLEMDE);
        printerRepository.save(printer);
        printJobRepository.save(printJob);

        try {
            pdfPath = processTextToPdf(printJob);
        } finally {
            printJob.setPrinterJobStatus(PrinterJobStatus.HAZIR);
            printer.setStatus(PrinterStatus.BOS);
            printJobRepository.save(printJob);
            printerRepository.save(printer);
        }

        producer.sendPdfCreatedEvent(
                new PrintJobCreatedEvent(
                        printJob.getId(),
                        PrintJobStatus.TEXT,
                        pdfPath.toString(),
                        "PDF_HAZIR"
                )
        );


        return CompletableFuture.completedFuture(pdfPath);
    }



    private Path processTextToPdf(PrintJob printJob) throws IOException, DocumentException {

        //PDF dosyasının kaydedileceği klasörü belirler.
        Path folder = Paths.get(outputDir);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        //PDF dosya yolu oluşturma
        Path pdfPath = folder.resolve("Text_" + printJob.getId() + ".pdf");

        //iText kütüphanesinin PDF temsilcisi.
        Document document = new Document();

        //PDF’i dosyaya yazacak writer
        PdfWriter.getInstance(document,
                //dosyaya yazı yazmak için kullanılır.
                new FileOutputStream(pdfPath.toFile()));
        //PDF içerisine yazmaya başlamak için açılır.
        document.open();

        //PDF’e paragrafı ekler
        document.add(new Paragraph(printJob.getContent()));

        document.close();

        System.out.println("Pdf oluşturuldu: " + pdfPath);
        return pdfPath;
    }

    private Path processBarcodeJob(PrintJob printJob) throws WriterException, IOException {
        //Barkod türü oluşturmak için kullanılır.
        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
        //Barkodun görsel temsilini üretmek için kullanılır.
        BitMatrix bitMatrix = barcodeWriter.encode(printJob.getContent(), BarcodeFormat.CODE_128, 400, 150);

        //Yazılacak klasörü seçer yoksa oluşturur
        Path folder = Paths.get(outputDir);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        // klasörün içine yazılcak dosyanın eklentilerini belirtir.
        Path barcodePath = folder.resolve("BARCODE_" + printJob.getId() + ".png");

        //  Barkod PNG olarak kaydeder
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", barcodePath);
        return barcodePath;
    }

    private Path processQRJob(PrintJob printJob) throws WriterException, IOException {
        //Qr code üretmek için kullanılır.
        QRCodeWriter qrWriter = new QRCodeWriter();

        //Qr kodun görsel temsilini oluşturmak için kullanılır.
        BitMatrix bitMatrix = qrWriter.encode(printJob.getContent(), BarcodeFormat.QR_CODE, 200, 200);

    //Dosya yolunun belirlemek için kullanılır
        Path folder = Paths.get(outputDir);

        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        //Kaydedilcek klasörü belirledikten sonra dosya yolunu oluşturur.(png,pdf,jpg)
        Path qrPath = folder.resolve("QR_" + printJob.getId() + ".png");

        //Qr u png olarak kaydeder
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrPath);
        return qrPath;
    }
}
