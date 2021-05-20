package org.dzianis.spacerep.controller;

import org.dzianis.spacerep.service.LearningEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LearningEntryController {

  private final LearningEntryService learningEntryService;

  public LearningEntryController(LearningEntryService learningEntryService) {
    this.learningEntryService = learningEntryService;
  }

  @GetMapping(value = "/")
  public String index(Model model) {
    model.addAttribute("entries", learningEntryService.readAllActive());
    return "list";
  }
}
