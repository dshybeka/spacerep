package org.dzianis.spacerep.controller;

import org.dzianis.spacerep.dao.LearningEntryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListController {

  private final LearningEntryDao learningEntryDao;

  @Autowired
  public ListController(LearningEntryDao learningEntryDao) {
    this.learningEntryDao = learningEntryDao;
  }

  @GetMapping(value = "/")
  public String index(Model model) {
    model.addAttribute("entries", learningEntryDao.getAll());
    return "list";
  }
}
