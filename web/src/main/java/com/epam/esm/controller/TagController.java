package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public Tag getOne(@PathVariable("id") Long id) {
        return tagService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag save(@RequestBody @Valid Tag tag) {
        try {
            return tagService.save(tag);
        } catch (ServiceException ex) {
            throw new ResourceDuplicateException("name", tag.getName());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        tagService.delete(id);
    }
}
