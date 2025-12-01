package com.berksozcu.repository;

import com.berksozcu.entity.PrintJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintJobRepository extends JpaRepository<PrintJob, Long> {
}
