package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * The Tag REST API controller.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;
    private TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired
    public void setTagModelAssembler(TagModelAssembler tagModelAssembler) {
        this.tagModelAssembler = tagModelAssembler;
    }

    /**
     * Get all tags.
     *
     * @param pageable object containing page and size request parameters
     * @return all available tags
     */
    @GetMapping
    public PagedModel<Tag> getAll(@PageableDefault Pageable pageable) {
        Page<Tag> tags = tagService.findAll(pageable);
        return tagModelAssembler.toPagedModel(tags);
    }

    /**
     * Get one tag.
     *
     * @param id the tag id
     * @return found tag, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<Tag> getOne(@PathVariable("id") Long id) {
        Tag tag = tagService.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return tagModelAssembler.toModelWithAllLink(tag);
    }

    /**
     * Save a tag.
     *
     * @param tag the tag json object
     * @return saved tag
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Tag> save(@RequestBody @Valid Tag tag) {
        try {
            Tag t = tagService.save(tag);
            return tagModelAssembler.toModelWithAllLink(t);
        } catch (ServiceException ex) {
            throw new ResourceDuplicateException("name", tag.getName());
        }
    }

    /**
     * Delete a tag.
     *
     * @param id the tag id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            tagService.delete(id);
        } catch (ServiceException ex) {
            throw new ResourceNotFoundException(id);
        }
    }
}
