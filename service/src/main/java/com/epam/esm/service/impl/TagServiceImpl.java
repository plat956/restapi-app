package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
    public List<Tag> findAllPaginated(RequestedPage page) {
        return tagRepository.findAllPaginated(page);
    }

    @Override
    public Tag save(Tag tag) throws ServiceException {
        try {
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("This tag already exists", e);
        }
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(id);
    }
}
