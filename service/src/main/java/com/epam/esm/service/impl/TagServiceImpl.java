package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.MessageProvider;
import com.epam.esm.util.RequestedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private MessageProvider messageProvider;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public PagedModel<Tag> findAllPaginated(RequestedPage page) {
        return tagRepository.findAllPaginated(page);
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
    public void delete(Long id) {
        tagRepository.delete(id);
    }
}
