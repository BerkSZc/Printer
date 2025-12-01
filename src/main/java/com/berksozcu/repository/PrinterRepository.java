package com.berksozcu.repository;

import com.berksozcu.entity.PrintJob;
import com.berksozcu.entity.Printer;
import com.berksozcu.entity.PrinterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrinterRepository extends JpaRepository<Printer, Long> {

    //Eğer otomatik yazmazsa bu sorgu kullanılabilir, nativeQuery özel Sql dili kullanır false yapılırsa.
//    @Query(value = "SELECT * FROM printer WHERE status= ? LIMIT 1", nativeQuery = true)
    Optional<Printer> findFirstByStatus(PrinterStatus status);
}
