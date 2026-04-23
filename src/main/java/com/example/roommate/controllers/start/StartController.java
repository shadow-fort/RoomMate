package com.example.roommate.controllers.start;

import com.example.roommate.application.DatabaseSimulator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.controllers.start.MySimpleUrlAuthenticationSuccessHandler.determineTargetUrl;

@Controller
public class StartController {

    /**
     * .
     */
    private final DatabaseSimulator databaseSimulator;

    /**
     * .
     * @param databaseSimulation Objekt für Simulation
     */
    public StartController(
            final DatabaseSimulator databaseSimulation) {
        this.databaseSimulator = databaseSimulation;
    }

    /**
     * Web login Seite.
     * @param authentication enthält die Informationen
     *                       für die Authentication von Nutzer.
     * @return zuerst die login Seite und danach
     * entweder die normal user Seite oder admin Seite
     */
    @GetMapping
    public String start(final Authentication authentication) {
        if (authentication != null) {
            return "redirect:" + determineTargetUrl(authentication);
        }
        return "login";
    }
}