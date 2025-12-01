🖨️ Yazıcı İş Yönetim Sistemi (Printer Job Management System)

Bu proje, Spring Boot tabanlı bir backend sistemi olarak tasarlanmıştır. Sistem, QR kod, Barkod ve Metin/PDF türündeki yazdırma işleri için asenkron işleme, RabbitMQ ile mesaj kuyruğu yönetimi ve PostgreSQL veri tabanı ile entegrasyon sunar. Docker ile container ortamında çalıştırılabilir.

📌 Özellikler

Yazıcı Yönetimi:

Yazıcı durumlarını (BOS, KULLANIMDA, ARIZALI) takip eder.

Boş yazıcıya iş ataması yapar.

Yazdırma İş Türleri:

QR Kod: İçeriği QR kod olarak PNG dosyası üretir.

Barkod: CODE-128 barkod PNG dosyası üretir.

Metin / PDF: Metin içeriğini PDF’e dönüştürür.

Asenkron İşleme:

RabbitMQ ile yazdırma işleri asenkron şekilde kuyruğa alınır.

Tek veya çoklu kuyruk kullanımı ile farklı iş tipleri için ayrı consumer’lar oluşturulabilir.

Veri Tabanı:

PostgreSQL ile yazıcı ve yazdırma işleri kalıcı olarak saklanır.

Docker ile Çalıştırılabilir:

Spring Boot uygulaması, RabbitMQ ve PostgreSQL container’ları Docker Compose ile ayağa kalkar.

Ölçeklenebilirlik:

Kuyruk tabanlı işleme sayesinde, aynı anda birden fazla job consumer ile yüksek throughput sağlanabilir.

🛠️ Kullanılan Teknolojiler
Bileşen	Teknoloji / Kütüphane
Backend	Spring Boot 3.x
Veri Tabanı	PostgreSQL 17
ORM / Veri Erişimi	Spring Data JPA / Hibernate
Asenkron Mesajlaşma	RabbitMQ + Spring AMQP
QR / Barkod Üretimi	ZXing (QRCodeWriter, MultiFormatWriter)
PDF Üretimi	iText 7
Containerizasyon	Docker & Docker Compose
Yapılandırma Dosyaları	application.properties veya application.yml
Asenkron İşleme	RabbitMQ Consumer / CompletableFuture


🚀 Kurulum ve Çalıştırma

Projeyi klonla

git clone https://github.com/BerkSZc/Printer.git
cd printer-job-management


Docker Compose ile container’ları çalıştır

docker-compose up --build


RabbitMQ: http://localhost:15672

Username: hello

Password: hello

PostgreSQL: localhost:5432

Username / Password: .env dosyasında belirtilen değerler

Spring Boot API’lerini kullanma

QR / Barkod / PDF işlerini göndermek için POST endpointlerini kullanabilirsiniz.

Örnek:

POST http://localhost:8080/printjob
Content-Type: application/json

{
  "content": "Merhaba Dünya",
  "type": "QR"
}


Üretilen dosyaları görüntüleme

QR ve Barkod PNG dosyaları veya Text PDF’ler, Docker container içindeki belirlenen output klasörüne kaydedilir.

REST endpoint üzerinden byte[] olarak alınabilir.

⚙️ Konfigürasyon

application.properties veya .yml dosyasında:

spring.datasource.url=jdbc:postgresql://postgres:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.rabbitmq.host=spboot-rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=hello
spring.rabbitmq.password=hello

sr.rabbit.queue.qr=qrQueue
sr.rabbit.queue.barcode=barcodeQueue
sr.rabbit.exchange.name=printExchange
sr.rabbit.routing.key.qr=qrRouting
sr.rabbit.routing.key.barcode=barcodeRouting
