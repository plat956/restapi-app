package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag save(Tag tag) throws ServiceException {
        try {
            return tagRepository.save(tag);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("This tag already exists", e);
        }
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }
}
