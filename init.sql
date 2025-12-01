CREATE SCHEMA IF NOT EXISTS printer;

ALTER ROLE postgres SET search_path TO printer, public;

CREATE TYPE printer_status AS ENUM ('BOS', 'KULLANIMDA', 'ARIZA');
CREATE TYPE print_job_status AS ENUM ('TEXT', 'QR', 'BARCODE');
CREATE TYPE printer_job_status AS ENUM('ISLEMDE', 'BEKLIYOR', 'HAZIR');


-- Printer tablosunu oluştur
CREATE TABLE IF NOT EXISTS printer.printer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    status printer_status
);

--Printer İşlerini oluştur
CREATE TABLE IF NOT EXISTS printer.print_job (
    id SERIAL PRIMARY KEY,
    content VARCHAR(255),
    created_at DATE,
    type print_job_status,
    printerJobStatus printer_job_status DEFAULT 'BEKLIYOR'
);