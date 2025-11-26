package com.example.gyak_beadando;

import com.example.gyak_beadando.model.Message;
import com.example.gyak_beadando.model.Result;
import com.example.gyak_beadando.model.Pilot;
import com.example.gyak_beadando.repository.MessageRepository;
import com.example.gyak_beadando.repository.ResultRepository;
import com.example.gyak_beadando.repository.PilotRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyControllers {

    private final ResultRepository resultRepository;
    private final MessageRepository messageRepository;
    private final PilotRepository pilotRepository;

    public MyControllers(ResultRepository resultRepository,
                         MessageRepository messageRepository,
                         PilotRepository pilotRepository) {
        this.resultRepository = resultRepository;
        this.messageRepository = messageRepository;
        this.pilotRepository = pilotRepository;
    }

    // --- Főoldal ---

    @GetMapping({"/", "/fooldal"})
    public String fooldal() {
        return "fooldal";
    }

    // --- Adatbázis menü ---

    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("results", resultRepository.findAll());
        return "adatbazis";
    }

    // --- Kapcsolat menü: űrlap megjelenítése ---

    @GetMapping("/kapcsolat")
    public String showContactForm(@ModelAttribute("messageForm") Message messageForm) {
        // Ha van flash attribute (hibás kitöltés után), azt használja,
        // ha nincs, Spring létrehoz egy új Message példányt.
        return "kapcsolat";
    }

    // --- Kapcsolat menü: űrlap feldolgozása ---

    @PostMapping("/kapcsolat")
    public String handleContactForm(
            @Valid @ModelAttribute("messageForm") Message messageForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.messageForm",
                    bindingResult);
            redirectAttributes.addFlashAttribute("messageForm", messageForm);
            return "redirect:/kapcsolat";
        }

        messageRepository.save(messageForm);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Köszönjük, üzenetét sikeresen elküldte!");

        return "redirect:/kapcsolat";
    }

    // --- Üzenetek menü ---

    @GetMapping("/uzenetek")
    public String uzenetek(Model model) {
        model.addAttribute("messages",
                messageRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "createdAt")));
        return "uzenetek";
    }

    // --- Diagram menü ---

    @GetMapping("/diagram")
    public String diagram(Model model) {
        List<Result> results = resultRepository.findAll();

        Map<String, Long> countsByTeam = results.stream()
                .collect(Collectors.groupingBy(Result::getTeam, Collectors.counting()));

        List<String> labels = new ArrayList<>(countsByTeam.keySet());
        List<Long> data = labels.stream()
                .map(countsByTeam::get)
                .collect(Collectors.toList());

        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "diagram";
    }

    // --- CRUD menü: Pilóták teljes CRUD-ja ---

    // Lista + rendezés
    @GetMapping("/crud")
    public String crud(@RequestParam(name = "sortField", defaultValue = "id") String sortField,
                       @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                       Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        List<Pilot> pilots = pilotRepository.findAll(sort);

        model.addAttribute("pilots", pilots);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir",
                sortDir.equalsIgnoreCase("asc") ? "desc" : "asc");

        return "crud";
    }

    // Új pilóta felvétele
    @PostMapping("/crud/add")
    public String addPilot(@ModelAttribute Pilot pilot,
                           @RequestParam(required = false) String sortField,
                           @RequestParam(required = false) String sortDir,
                           RedirectAttributes redirectAttributes) {

        // alapértelmezett rendezés, ha valamiért nincs megadva
        if (sortField == null || sortField.isBlank()) {
            sortField = "id";
        }
        if (sortDir == null || sortDir.isBlank()) {
            sortDir = "asc";
        }

        // legacy_id egyediség ellenőrzése
        if (pilot.getLegacyId() != null && pilotRepository.existsByLegacyId(pilot.getLegacyId())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Hiba: A megadott legacy_id már létezik az adatbázisban!");
            return "redirect:/crud?sortField=" + sortField + "&sortDir=" + sortDir;
        }

        pilotRepository.save(pilot);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Új pilóta sikeresen hozzáadva.");

        return "redirect:/crud?sortField=" + sortField + "&sortDir=" + sortDir;
    }


    // Pilóta módosítása
    @PostMapping("/crud/update")
    public String updatePilot(@ModelAttribute Pilot pilot,
                              @RequestParam String sortField,
                              @RequestParam String sortDir,
                              RedirectAttributes redirectAttributes) {

        pilotRepository.save(pilot);
        redirectAttributes.addFlashAttribute("successMessage", "Pilóta frissítve.");

        return "redirect:/crud?sortField=" + sortField + "&sortDir=" + sortDir;
    }


    // Pilóta törlése
    @PostMapping("/crud/delete/{id}")
    public String deletePilot(@PathVariable Long id,
                              @RequestParam String sortField,
                              @RequestParam String sortDir,
                              RedirectAttributes redirectAttributes) {

        pilotRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Pilóta törölve.");

        return "redirect:/crud?sortField=" + sortField + "&sortDir=" + sortDir;
    }

    // --- RESTful menü oldal (HTML) ---

    @GetMapping("/restful")
    public String restful() {
        return "restful";
    }

    // --- RESTful API: Pilóták JSON-ben ---

    // Összes pilóta lekérdezése (GET /api/pilots)
    @GetMapping("/api/pilots")
    public ResponseEntity<List<Pilot>> getAllPilots() {
        List<Pilot> pilots = pilotRepository.findAll();
        return ResponseEntity.ok(pilots);
    }

    // Egy pilóta lekérdezése ID alapján (GET /api/pilots/{id})
    @GetMapping("/api/pilots/{id}")
    public ResponseEntity<Pilot> getPilotById(@PathVariable Long id) {
        return pilotRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Új pilóta létrehozása (POST /api/pilots)
    @PostMapping("/api/pilots")
    public ResponseEntity<Pilot> createPilot(@RequestBody Pilot pilot) {
        Pilot saved = pilotRepository.save(pilot);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Pilóta módosítása (PUT /api/pilots/{id})
    @PutMapping("/api/pilots/{id}")
    public ResponseEntity<Pilot> updatePilotApi(@PathVariable Long id,
                                                @RequestBody Pilot pilot) {
        if (!pilotRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // biztosítjuk, hogy a kérésben kapott objektum az adott ID-t kapja
        pilot.setId(id);
        Pilot updated = pilotRepository.save(pilot);
        return ResponseEntity.ok(updated);
    }

    // Pilóta törlése (DELETE /api/pilots/{id})
    @DeleteMapping("/api/pilots/{id}")
    public ResponseEntity<Void> deletePilotApi(@PathVariable Long id) {
        if (!pilotRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pilotRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
