package com.example.GestionPatientMVC.repositories;

import com.example.GestionPatientMVC.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByNomContains(String kw, Pageable pageable); // chercher patient par mot dans chaque page


}
