package com.berksozcu.controller.impl;

import com.berksozcu.controller.IPrintJobController;
import com.berksozcu.controller.base.RootEntity;
import com.berksozcu.entity.PrintJob;
import com.berksozcu.exception.BaseException;
import com.berksozcu.exception.ErrorMessage;
import com.berksozcu.exception.MessageType;
import com.berksozcu.repository.PrintJobRepository;
import com.berksozcu.service.IPrintJobService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rest/api/job")
public class PrintJobControllerImpl implements IPrintJobController {

    @Autowired
    private IPrintJobService printJobService;

    @Autowired
    private PrintJobRepository printJobRepository;


    @Value("${file.output-dir}")
    private String outputDir;



    @Override
    @GetMapping("/find-all")
    public RootEntity<List<PrintJob>> findAllJobs() {
        return RootEntity.listRootEntity(printJobService.findAllJobs());
    }

    @PostMapping("/assign")
    @Override
    public RootEntity<PrintJob> generateJob(@RequestBody PrintJob printJob) {
        PrintJob assignedJob = printJobService.generateJob(printJob);
        return RootEntity.ok(assignedJob);
    }

    //byte [] kullanmamızın sebei, dosya veya resim gibi çoklu ikili veriyi tutan dizi olduğu içindir.
    //byte → kullanırsak sadece tek bir ikili veri birimi döndürebiliriz.

    @Override
    @GetMapping("/qr/{jobId}")
    public ResponseEntity<byte[]> generateQr(@PathVariable Long jobId) throws IOException, WriterException, InterruptedException {

        PrintJob job = printJobRepository.findById(jobId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        printJobService.generateQr(jobId);

        //Hangi dosyadan resmin bulunacağına bakar
        Path path = Paths.get(outputDir,"QR_" + job.getId() + ".png");
       //Oluşturulan QR PNG dosyasını diskten okur
        byte[] qrBytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                //tarayıcıya resim olduğunu bildirir
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(qrBytes);
    }


    @GetMapping("/text/{jobId}")
    @Override
    public ResponseEntity<byte[]> getTextPdf(@PathVariable Long jobId) throws Exception {

         printJobRepository.findById(jobId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        CompletableFuture<Path> future = printJobService.generateTextToPdf(jobId);

        //async işlemin tamamlanmasını bloklayarak bekler
        //Bu future.get sayesinde service den cevap gelmesini bekleriz bu sayede controller boş dönmez
        Path pdfPath = future.get();

        //Pdf içindeki yazıyı okuruz ve ekranda basması için bunu byte[] içine alırız.
        byte[] pdfBytes = Files.readAllBytes(pdfPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                //tarayıcı PDF’i inline gösterir (indirilebilir veya görüntülenebilir)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=output.pdf")
                .body(pdfBytes);
    }


    @GetMapping("/barcode/{jobId}")
    @Override
    public ResponseEntity<byte[]> generateBarcode(@PathVariable(name = "jobId") Long jobId) throws IOException, InterruptedException, ExecutionException {

        PrintJob job = printJobRepository.findById(jobId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.IS_BULUNAMADI)));

        CompletableFuture<Path> future = printJobService.generateBarcode(jobId);

        Path barcodePath = future.get();

            printJobService.generateBarcode(jobId);
        //Burda bunun kullanılmasının sebebi generateBarcode da Thread kullanılmasıdır.
        //Thread yüzünden controller service'in cevabını beklemediği için hata verir.

        byte[] barcodeBytes = Files.readAllBytes(barcodePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(barcodeBytes);
    }
}
