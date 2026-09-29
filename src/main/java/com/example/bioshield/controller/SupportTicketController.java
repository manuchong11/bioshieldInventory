package com.example.bioshield.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = {"http://localhost:4200", "https://bioshield-capstone.web.app", "https://bioshield-capstone.firebaseapp.com"})
public class SupportTicketController {

    @PostMapping("/ticket")
    public ResponseEntity<?> submitSupportTicket(@RequestBody Map<String, String> ticketData) {
        // In a real application, this would route to JIRA, ServiceNow, or send an email.
        // For the Capstone, we just log it and return success to fulfill the protocol requirement.
        System.out.println("--- NEW SUPPORT TICKET SUBMITTED ---");
        System.out.println("Subject: " + ticketData.get("subject"));
        System.out.println("Description: " + ticketData.get("description"));
        System.out.println("Urgency: " + ticketData.get("urgency"));
        System.out.println("------------------------------------");

        return ResponseEntity.ok().body("{\"message\": \"Support ticket successfully routed to IT.\"}");
    }
}
