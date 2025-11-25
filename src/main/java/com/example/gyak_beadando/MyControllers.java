package com.example.gyak_beadando;

import com.example.gyak_beadando.model.Message;
import com.example.gyak_beadando.repository.MessageRepository;
import com.example.gyak_beadando.repository.ResultRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MyControllers {

    private final ResultRepository resultRepository;
    private final MessageRepository messageRepository;

    public MyControllers(ResultRepository resultRepository,
                         MessageRepository messageRepository) {
        this.resultRepository = resultRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String index() {
        return "fooldal";
    }

    @GetMapping("/fooldal")
    public String fooldal() {
        return "fooldal";
    }

    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("results", resultRepository.findAll());
        return "adatbazis";
    }

    // --- Kapcsolat űrlap ---

    @GetMapping("/kapcsolat")
    public String showContactForm(Model model) {
        if (!model.containsAttribute("messageForm")) {
            model.addAttribute("messageForm", new Message());
        }
        return "kapcsolat";
    }

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
        redirectAttributes.addFlashAttribute("successMessage",
                "Köszönjük, üzenetét sikeresen elküldte!");

        return "redirect:/kapcsolat";
    }

    // --- Üzenetek lista ---

    @GetMapping("/uzenetek")
    public String uzenetek(Model model) {
        model.addAttribute("messages",
                messageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        return "uzenetek";
    }

    @GetMapping("/diagram")
    public String diagram(Model model) {
        model.addAttribute("results", resultRepository.findAll());
        return "diagram";
    }

    @GetMapping("/crud")
    public String crud() {
        return "crud";
    }

    @GetMapping("/restful")
    public String restful() {
        return "restful";
    }
}
