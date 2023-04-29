package com.example.GestionPatientMVC.web;

import com.example.GestionPatientMVC.entities.Patient;
import com.example.GestionPatientMVC.repositories.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientrepository;

    @GetMapping(path = "/user/index")
    public String patients(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page, // num de page par defaut
                           @RequestParam(name = "size", defaultValue = "5") int size, // taille de chaque page
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) { // parametre keyword recupéré a partir du formulaire
        Page<Patient> pagepatients = patientrepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("ListePatients", pagepatients.getContent()); // avoir contenu de chaque page
        model.addAttribute("pages", new int[pagepatients.getTotalPages()]); // tableau pour pagination
        model.addAttribute("currentPage", page); // stocker page courante
        model.addAttribute("keyword", keyword); // avoir tous les patients
        return "patients";
    }

    @GetMapping(path = "/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id, String keyword, int page) {
        patientrepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";

    }

    @GetMapping("/patients")
    @ResponseBody
    public List<Patient> listePatients() {
        return patientrepository.findAll();

    }

    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatients(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            return "formPatients";
        } else {
            patientrepository.save(patient);
            return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
        }
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model, Long id, String keyword, int page) {
        Patient patient = patientrepository.findById(id).orElse(null); // chercher patient par id si il est present sinon retourner un null
        if (patient == null) {
            throw new RuntimeException("patient introuvable!");
        }
        model.addAttribute("patient", patient);
        model.addAttribute("keyword", keyword); // stocker le keyword courant
        model.addAttribute("page", page); // stocker la page courante
        return "editPatient";
    }


}
