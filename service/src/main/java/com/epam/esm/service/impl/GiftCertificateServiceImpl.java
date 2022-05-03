package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
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
//
//    private static final String ORDER_TYPE_REGEX = "^(\\+|\\-).*$";
//    private static final String NEGATIVE_SIGN = "-";
//    private static final String PERCENT_SIGN = "%";

    private GiftCertificateRepository giftCertificateRepository;
    private TagRepository tagRepository;
    private ModelMapper modelMapper;
    private MessageProvider messageProvider;

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

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return giftCertificateRepository.findById(id);
    }

    @Override
    public Page<GiftCertificate> findAll(List<String> tags, String search, List<String> sort, Pageable pageable) {
//        Specification<GiftCertificate> specification = buildSpecification(tags, search, sort);
//        return giftCertificateRepository.findAll(specification, PageRequest.of(page, size));
        return null; //fixme
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

//    private Specification<GiftCertificate> buildSpecification(List<String> tags, String search, List<String> sort) {
//        return (certRoot, criteriaQuery, criteriaBuilder) -> {
//            List<Predicate> wherePredicates = new ArrayList<>();
//            List<Predicate> subWherePredicates = new ArrayList<>();
//            Subquery subQuery = criteriaQuery.subquery(Long.class);
//            Root subRoot = subQuery.from(GiftCertificate.class);
//            subQuery.select(criteriaBuilder.count(subRoot.get("id")));
//
//            if (tags != null && !tags.isEmpty()) {
//                addFilteringByTags(criteriaBuilder, criteriaQuery, certRoot, tags, wherePredicates);
//                addFilteringByTags(criteriaBuilder, subQuery, subRoot, tags, subWherePredicates);
//            }
//            if (search != null) {
//                addSearch(criteriaBuilder, certRoot, search, wherePredicates);
//                addSearch(criteriaBuilder, subRoot, search, subWherePredicates);
//            }
//            if (sort != null && !sort.isEmpty()) {
//                addOrdering(criteriaBuilder, criteriaQuery, certRoot, sort);
//            }
//
//            if(!subWherePredicates.isEmpty()) {
//                subWherePredicates.add(criteriaBuilder.equal(certRoot.get("id"), subRoot.get("id")));
//                subQuery.where(subWherePredicates.toArray(Predicate[]::new));
//                if (tags != null && !tags.isEmpty()) {
//                    wherePredicates.add(criteriaBuilder.equal(subQuery, tags.size()));
//                }
//            }
//            //criteriaQuery.distinct(true);
//            return criteriaBuilder.and(wherePredicates.toArray(Predicate[]::new));
//        };
//    }
//
//    private void addFilteringByTags(CriteriaBuilder cb, AbstractQuery cq, Root<GiftCertificate> certRoot,
//                                    List<String> tags, List<Predicate> wherePredicates) {
//        Join<GiftCertificate, Tag> tagJoin = certRoot.join("tags", JoinType.LEFT);
//        Predicate[] tagPredicates = new Predicate[tags.size()];
//
//        for(int i = 0; i < tags.size(); i++) {
//            Predicate p = cb.equal(tagJoin.get("name"), tags.get(i));
//            tagPredicates[i] = p;
//        }
//
//        wherePredicates.add(cb.or(tagPredicates));
//
//        cq.groupBy(certRoot.get("id"));
//    }
//
//    private void addSearch(CriteriaBuilder cb, Root<GiftCertificate> certRoot,
//                           String search, List<Predicate> wherePredicates) {
//        search = PERCENT_SIGN + search.toLowerCase() + PERCENT_SIGN;
//        Predicate searchPredicate = cb.or(
//                cb.like(cb.lower(certRoot.get("name")), search),
//                cb.like(cb.lower(certRoot.get("description")), search)
//        );
//        wherePredicates.add(searchPredicate);
//    }
//
//    private void addOrdering(CriteriaBuilder cb, CriteriaQuery cq, Root<GiftCertificate> certRoot, List<String> sort) {
//        List<Order> orderList = new ArrayList<>();
//        List<String> allowedColumns = List.of("name", "createDate", "lastUpdateDate");
//        OrderType orderType;
//        String orderColumn;
//        for (String f: sort) {
//            String field = f.trim();
//            if (field.matches(ORDER_TYPE_REGEX)) {
//                orderType = field.startsWith(NEGATIVE_SIGN) ? DESC : ASC;
//                orderColumn = field.substring(1);
//            } else {
//                orderType = ASC;
//                orderColumn = field;
//            }
//            if (allowedColumns.contains(orderColumn)) {
//                Path column = certRoot.get(orderColumn);
//                Order order = orderType == ASC ? cb.asc(column) : cb.desc(column);
//                orderList.add(order);
//            }
//        }
//        cq.orderBy(orderList);
//    }

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
