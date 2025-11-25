package com.example.gyak_beadando;

import com.example.gyak_beadando.model.Message;
import com.example.gyak_beadando.model.Result;
import com.example.gyak_beadando.repository.MessageRepository;
import com.example.gyak_beadando.repository.ResultRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyControllers {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ResultRepository resultRepository;

    // Gyökér URL -> főoldal
    @GetMapping("/")
    public String index() {
        return "redirect:/fooldal";
    }

    // Főoldal
    @GetMapping("/fooldal")
    public String fooldal() {
        return "fooldal";
    }

    // Adatbázis menü – a results táblán keresztül használja a 3 F1 táblát
    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("results", resultRepository.findAll());
        return "adatbazis";
    }

    // Kapcsolat űrlap (GET)
    @GetMapping("/kapcsolat")
    public String kapcsolatForm(Model model) {
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", new Message());
        }
        return "kapcsolat";
    }

    // Kapcsolat űrlap feldolgozás (POST)
    @PostMapping("/kapcsolat")
    public String kuldes(@Valid @ModelAttribute("message") Message message,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Hibák + kitöltött űrlap visszaadása flash attribútumokkal
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.message",
                    bindingResult
            );
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/kapcsolat";
        }

        // Mentés adatbázisba
        messageRepository.save(message);

        // Sikerüzenet
        redirectAttributes.addFlashAttribute("success",
                "Üzenetedet sikeresen elküldted!");

        return "redirect:/kapcsolat";
    }

    // Üzenetek menü – üzenetek idő szerint csökkenő sorrendben
    @GetMapping("/uzenetek")
    public String uzenetek(Model model) {
        model.addAttribute(
                "messages",
                messageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return "uzenetek";
    }

    // Diagram menü – csapatonkénti darabszám a results táblából
    @GetMapping("/diagram")
    public String diagram(Model model) {
        List<Result> results = resultRepository.findAll();

        // team -> darabszám
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

    // CRUD menü
    @GetMapping("/crud")
    public String crud() {
        return "crud";
    }

    // RESTful menü
    @GetMapping("/restful")
    public String restful() {
        return "restful";
    }
}
