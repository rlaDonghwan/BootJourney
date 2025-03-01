package com.BootJourney.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }

}
