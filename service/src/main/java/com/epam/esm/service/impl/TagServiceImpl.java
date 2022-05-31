package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private MessageProvider messageProvider;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, MessageProvider messageProvider) {
        this.tagRepository = tagRepository;
        this.messageProvider = messageProvider;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag save(Tag tag) throws ServiceException {
        try {
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(messageProvider.getMessage("message.error.tag-exists"), e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            tagRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ServiceException(messageProvider.getMessage("message.error.no-tag"), e);
        }
    }
}
