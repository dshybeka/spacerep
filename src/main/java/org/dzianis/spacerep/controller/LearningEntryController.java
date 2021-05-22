package org.dzianis.spacerep.controller;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.util.Arrays;
import org.dzianis.spacerep.controller.model.UpdateLearningEntry;
import org.dzianis.spacerep.converter.LocalDateConverter;
import org.dzianis.spacerep.service.LearningEntryService;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Status;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LearningEntryController {

  private final LearningEntryService learningEntryService;
  private final LocalDateConverter localDateConverter;

  public LearningEntryController(
      LearningEntryService learningEntryService, LocalDateConverter localDateConverter) {
    this.learningEntryService = learningEntryService;
    this.localDateConverter = localDateConverter;
  }

  @GetMapping(value = "/")
  public String index(Model model) {
    model.addAttribute("entries", learningEntryService.readAllActive());
    return "list";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    LearningEntryProto entry = learningEntryService.get(id);
    model.addAttribute(
        "entry",
        UpdateLearningEntry.builder()
            .id(entry.getId())
            .notes(entry.getNotes())
            .name(entry.getName())
            .link(entry.getLink())
            .scheduleFor(localDateConverter.toLocalDateTime(entry.getScheduledFor()))
            .status(entry.getStatus())
            .build());
    model.addAttribute(
        "statuses",
        Arrays.stream(Status.values())
            .filter(s -> s != Status.UNRECOGNIZED)
            .filter(s -> s != Status.UNDEFINED)
            .collect(toImmutableList()));
    return "edit-entry";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@PathVariable("id") long id, UpdateLearningEntry entry) {

    System.out.println("updated!");
    return "redirect:/";
  }
}
