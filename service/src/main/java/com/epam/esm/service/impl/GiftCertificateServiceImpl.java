package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.CustomGiftCertificateRepository;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.MessageProvider;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepository;
    private TagRepository tagRepository;
    private ModelMapper modelMapper;
    private MessageProvider messageProvider;
    private CustomGiftCertificateRepository customGiftCertificateRepository;

    @Autowired
    public void setGiftCertificateRepository(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.modelMapper.typeMap(GiftCertificate.class, GiftCertificate.class).addMappings(mapper -> {
            mapper.skip(GiftCertificate::setCreateDate);
            mapper.skip(GiftCertificate::setLastUpdateDate);
            mapper.skip(GiftCertificate::setTags);
        });
    }

    @Autowired
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Autowired
    public void setCustomGiftCertificateRepository(CustomGiftCertificateRepository customGiftCertificateRepository) {
        this.customGiftCertificateRepository = customGiftCertificateRepository;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return giftCertificateRepository.findById(id);
    }

    @Override
    public Page<GiftCertificate> findAll(List<String> tags, String search, List<String> sort, Pageable pageable) {
        return customGiftCertificateRepository.findAll(tags, search, sort, pageable);
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate certificate) {
        certificate.setId(null);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(null);
        saveTags(certificate);
        return giftCertificateRepository.save(certificate);
    }

    @Override
    @Transactional
    public GiftCertificate update(Long id, GiftCertificate certificate) throws ServiceException {
        GiftCertificate updatedCertificate = findById(id).orElseThrow(ServiceException::new);
        modelMapper.map(certificate, updatedCertificate);
        updatedCertificate.getTags().addAll(certificate.getTags());
        updatedCertificate.setLastUpdateDate(LocalDateTime.now());
        saveTags(updatedCertificate);
        return giftCertificateRepository.save(updatedCertificate);
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            giftCertificateRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ServiceException(messageProvider.getMessage("message.error.no-certificate"), e);
        }
    }

    @Override
    public GiftCertificate unbindTag(Long certificateId, Long tagId) throws ServiceException {
        GiftCertificate certificate = giftCertificateRepository.findById(certificateId).orElseThrow(ServiceException::new);
        certificate.getTags().removeIf(t -> t.getId().equals(tagId));
        return giftCertificateRepository.save(certificate);
    }

    @Override
    public Page<GiftCertificate> findByOrderId(Long id, Pageable pageable) {
        return giftCertificateRepository.findByOrderId(id, pageable);
    }

    private void saveTags(GiftCertificate certificate) {
        certificate.getTags().forEach(t -> {
            Optional<Tag> existingTag = tagRepository.findByName(t.getName());
            if(existingTag.isPresent()) {
                t.setId(existingTag.get().getId());
            } else {
                tagRepository.save(t);
            }
        });
    }
}
