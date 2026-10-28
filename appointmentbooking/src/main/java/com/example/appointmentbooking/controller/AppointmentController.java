
package com.example.appointmentbooking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppointmentController {

    private final List<Map<String, String>> appointments = new ArrayList<>();

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/book")
    public String bookAppointment() {
        return "book";
    }

    @PostMapping("/book")
    public String saveAppointment(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String type,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam(required = false, defaultValue = "") String message,
            RedirectAttributes redirectAttributes) {

        Map<String, String> appointment = Map.of(
                "name", name,
                "email", email,
                "phone", phone,
                "type", type,
                "date", date,
                "time", time,
                "message", message,
                "status", "Pending"
        );

        appointments.add(appointment);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Appointment booked successfully!");

        return "redirect:/manage";
    }

    @GetMapping("/manage")
    public String manageAppointments(Model model) {
        model.addAttribute("appointments", appointments);
        return "manage";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("total", appointments.size());

        long pending = appointments.stream()
                .filter(a -> "Pending".equals(a.get("status")))
                .count();

        long confirmed = appointments.stream()
                .filter(a -> "Confirmed".equals(a.get("status")))
                .count();

        model.addAttribute("pending", pending);
        model.addAttribute("confirmed", confirmed);

        return "admin";
    }
}
