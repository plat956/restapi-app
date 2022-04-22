package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateRepository giftCertificateRepository;
    private ModelMapper modelMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, ModelMapper modelMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.modelMapper.typeMap(GiftCertificate.class, GiftCertificate.class).addMappings(mapper -> {
            mapper.skip(GiftCertificate::setCreateDate);
            mapper.skip(GiftCertificate::setLastUpdateDate);
            mapper.skip(GiftCertificate::setTags);
        });
    }

    @Override
    public Optional<GiftCertificate> findOne(Long id) {
        return giftCertificateRepository.findOne(id);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateRepository.findAll();
    }

    @Override
    public List<GiftCertificate> findAll(List<String> tags, String search, List<String> sort) {
        return giftCertificateRepository.findAll(tags, search, sort);
    }

    @Override
    public GiftCertificate save(GiftCertificate certificate) {
        certificate.setId(null);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(null);
        return giftCertificateRepository.save(certificate);
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate certificate) throws ServiceException {
        GiftCertificate updatedCertificate = findOne(id).orElseThrow(ServiceException::new);
        modelMapper.map(certificate, updatedCertificate);
        updatedCertificate.getTags().addAll(certificate.getTags());
        updatedCertificate.setLastUpdateDate(LocalDateTime.now());
        return giftCertificateRepository.update(updatedCertificate);
    }

    @Override
    public void delete(Long id) {
        giftCertificateRepository.delete(id);
    }
}
