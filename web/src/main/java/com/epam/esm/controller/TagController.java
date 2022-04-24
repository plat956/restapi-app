package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }

    /**
     * Get all tags.
     *
     * @param page the requested page
     * @return all available tags
     */
    @GetMapping
    public CollectionModel<EntityModel<Tag>> getAll(RequestedPage page) {
        List<Tag> tags = tagService.findAllPaginated(page);
        return tagModelAssembler.toCollectionModel(tags);
    }

    /**
     * Get one tag.
     *
     * @param id the tag id
     * @return found tag, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public EntityModel<Tag> getOne(@PathVariable("id") Long id) {
        Tag tag = tagService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(id));
        return tagModelAssembler.toModelWithAllLink(tag);
    }

    /**
     * Save a tag.
     *
     * @param tag the tag json object
     * @return saved tag
     */
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        tagService.delete(id);
    }
}
