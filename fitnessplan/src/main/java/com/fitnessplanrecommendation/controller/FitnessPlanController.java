package com.fitnessplanrecommendation.controller;

import com.fitnessplanrecommendation.model.FitnessPlan;
import com.fitnessplanrecommendation.service.FitnessService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/fitness")
public class FitnessPlanController {

    private final FitnessService fitnessService = new FitnessService();

    @GetMapping("/recommend")
    public List<FitnessPlan> recommendPlans(@RequestParam String fitnessGoal, @RequestParam String fitnessLevel) {
        return fitnessService.getRecommendedPlans(fitnessGoal, fitnessLevel);
    }
}
