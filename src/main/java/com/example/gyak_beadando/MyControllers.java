package com.example.gyak_beadando;

import com.example.gyak_beadando.repository.ResultRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyControllers {

    private final ResultRepository resultRepository;

    public MyControllers(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @GetMapping({"/", "/fooldal"})
    public String fooldal() {
        return "fooldal"; // templates/fooldal.html
    }

    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("results", resultRepository.findAllWithJoins());
        return "adatbazis";
    }

    @GetMapping("/kapcsolat")
    public String kapcsolat() {
        return "kapcsolat";
    }

    @GetMapping("/uzenetek")
    public String uzenetek() {
        return "uzenetek";
    }

    @GetMapping("/diagram")
    public String diagram() {
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
