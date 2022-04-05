package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String test() {
        Optional<Tag> tag = tagService.findById(1L);

        return "hello";
    }
}
