package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * The Tag REST API controller.
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Get all tags.
     *
     * @param page the requested page
     * @return all available tags
     */
    @GetMapping
    public List<Tag> getAll(RequestedPage page) {
        return tagService.findAllPaginated(page);
    }

    /**
     * Get one tag.
     *
     * @param id the tag id
     * @return found tag, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public Tag getOne(@PathVariable("id") Long id) {
        return tagService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Save a tag.
     *
     * @param tag the tag json object
     * @return saved tag
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag save(@RequestBody @Valid Tag tag) {
        try {
            return tagService.save(tag);
        } catch (ServiceException ex) {
            throw new ResourceDuplicateException("name", tag.getName());
        }
    }

    /**
     * Delete a tag.
     *
     * @param id the tag id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        tagService.delete(id);
    }
}
