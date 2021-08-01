package org.dzianis.spacerep.controller;

import static com.google.common.collect.ImmutableList.toImmutableList;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import org.dzianis.spacerep.controller.model.CreateLearningEntry;
import org.dzianis.spacerep.controller.model.UpdateLearningEntry;
import org.dzianis.spacerep.converter.LocalDateConverter;
import org.dzianis.spacerep.service.LearningEntryService;
import org.dzianis.spacerep.service.TimeSource;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Status;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LearningEntryController {

  private static final int DEFAULT_SCHEDULE_DELAY = 7;

  private final LearningEntryService learningEntryService;
  private final LocalDateConverter localDateConverter;
  private final TimeSource timeSource;

  public LearningEntryController(
      LearningEntryService learningEntryService,
      LocalDateConverter localDateConverter,
      TimeSource timeSource) {
    this.learningEntryService = learningEntryService;
    this.localDateConverter = localDateConverter;
    this.timeSource = timeSource;
  }

  @GetMapping(value = "/")
  public String index(Model model) {
    model.addAttribute("entries", learningEntryService.readAllActive());
    model.addAttribute("view", "allActive");
    return "list";
  }

  @GetMapping(value = "/all")
  public String showAll(Model model) {
    model.addAttribute("entries", learningEntryService.readAll());
    model.addAttribute("view", "all");
    return "list";
  }

  @GetMapping(value = "/archive")
  public String showArchive(Model model) {
    model.addAttribute("entries", learningEntryService.readArchive());
    model.addAttribute("view", "archive");
    return "list";
  }

  @GetMapping("/edit/{type}/{uuid}")
  public String showUpdateForm(
      @PathVariable("type") String type, @PathVariable("uuid") String uuid, Model model) {
    LearningEntryProto entry = learningEntryService.get(uuid);
    model.addAttribute(
        "entry",
        UpdateLearningEntry.builder()
            .id(entry.getId())
            .notes(entry.getNotes())
            .name(entry.getName())
            .link(entry.getLink())
            .scheduledFor(localDateConverter.toLocalDate(entry.getScheduledFor()))
            .status(entry.getStatus())
            .attempt(entry.getAttempt())
            .delayInDays(entry.getDelayInDays())
            .uuid(entry.getUuid())
            .build());
    model.addAttribute(
        "statuses",
        Arrays.stream(Status.values())
            .filter(s -> s != Status.UNRECOGNIZED)
            .filter(s -> s != Status.UNDEFINED)
            .collect(toImmutableList()));

    return type.equals("full") ? "edit-entry" : "update-mark-entry";
  }

  @PostMapping("/update/{uuid}")
  public String update(@PathVariable("uuid") String uuid, UpdateLearningEntry entry) {
    Preconditions.checkArgument(
        uuid.equals(entry.getUuid()), "Uuid of entity and path should be the same.");
    learningEntryService.updateWithoutProcess(entry);
    return "redirect:/";
  }

  @PostMapping("/update-mark/{uuid}")
  public String updateMark(@PathVariable("uuid") String uuid, UpdateLearningEntry entry) {
    Preconditions.checkArgument(
        uuid.equals(entry.getUuid()), "Uuid of entity and path should be the same.");
    learningEntryService.updateMarkAndReschedule(entry);
    return "redirect:/";
  }

  @GetMapping("/create")
  public String showCreateForm(Model model) {
    model.addAttribute(
        "entry",
        CreateLearningEntry.builder()
            .attempt(1)
            .scheduledFor(timeSource.localDateNow().plusDays(DEFAULT_SCHEDULE_DELAY))
            .delayInDays(DEFAULT_SCHEDULE_DELAY)
            .build());
    return "create-entry";
  }

  @PostMapping("/create")
  public String createEntry(CreateLearningEntry entry) {
    learningEntryService.createNew(entry);
    return "redirect:/";
  }

  @PostMapping("/delete/{uuid}")
  public String delete(@PathVariable("uuid") String uuid) {
    learningEntryService.delete(uuid);
    return "redirect:/";
  }
}
