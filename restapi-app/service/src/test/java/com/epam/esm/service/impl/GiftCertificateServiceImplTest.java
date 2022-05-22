package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private List<GiftCertificate> giftCertificates;

    @BeforeAll
    void setUp() {
        giftCertificates = new ArrayList<>();
        GiftCertificate c1 = new GiftCertificate();
        c1.setId(1L);
        c1.setName("cert1");
        c1.setDescription("descr of cert1");
        c1.setCreateDate(LocalDateTime.now());
        c1.setDuration(2);
        c1.setPrice(BigDecimal.TEN);

        GiftCertificate c2 = new GiftCertificate();
        c2.setId(2L);
        c2.setName("cert2");
        c2.setDescription("descr of cert2");
        c2.setCreateDate(LocalDateTime.now().minusDays(10));
        c2.setLastUpdateDate(LocalDateTime.now());
        c2.setDuration(22);
        c2.setPrice(BigDecimal.ONE);

        GiftCertificate c3 = new GiftCertificate();
        c3.setId(3L);
        c3.setName("cert3");
        c3.setDescription("descr of cert3");
        c3.setCreateDate(LocalDateTime.now().minusDays(4));
        c3.setLastUpdateDate(LocalDateTime.now());
        c3.setDuration(4);
        c3.setPrice(BigDecimal.valueOf(20.13));

        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("test tag");
        c3.getTags().add(tag);

        giftCertificates.add(c1);
        giftCertificates.add(c2);
        giftCertificates.add(c3);
    }

    @BeforeEach
    void setUpEach(TestInfo info) {
        String method = info.getTestMethod().get().getName();
        if (!method.equals("save") && !method.equals("delete")) {
            when(giftCertificateRepository.findById(anyLong())).
                    thenAnswer(invocation -> giftCertificates.stream().
                            filter(u -> u.getId().equals(invocation.getArgument(0))).
                            findAny());
        }
    }

    @Test
    void findOne() {
        Optional<GiftCertificate> cert = giftCertificateService.findById(2L);
        String expected = "descr of cert2";
        String actual = cert.get().getDescription();
        assertEquals(expected, actual);
    }

    @Test
    void save() {
        GiftCertificate c = new GiftCertificate();
        c.setName("new cert");
        c.setPrice(BigDecimal.TEN);

        GiftCertificate spyCertificate = spy(c);
        giftCertificateService.save(spyCertificate);

        verify(spyCertificate).setLastUpdateDate(null);
        assertNotNull(spyCertificate.getCreateDate());
    }

    @ParameterizedTest
    @ValueSource(longs = 3L)
    void updateTrue(Long id) throws ServiceException {
        when(tagRepository.findByName(anyString())).thenAnswer(invocation -> {
            Tag t = new Tag();
            t.setId(1L);
            return Optional.of(t);
        });

        GiftCertificate c = new GiftCertificate();
        c.setPrice(BigDecimal.ZERO);

        GiftCertificateService spyService = spy(giftCertificateService);
        spyService.update(id, c);
        verify(spyService).findById(id);

        ArgumentCaptor<GiftCertificate> updateArgument = ArgumentCaptor.forClass(GiftCertificate.class);
        verify(giftCertificateRepository).save(updateArgument.capture());

        String expected = "cert3";
        String actual = updateArgument.getValue().getName();
        assertEquals(expected, actual);
    }

    @Test
    void updateFalse() {
        Assertions.assertThrows(ServiceException.class, () -> giftCertificateService.update(0L, new GiftCertificate()));
    }

    @ParameterizedTest
    @ValueSource(longs = 12L)
    void delete(Long id) throws ServiceException {
        giftCertificateService.delete(id);
        verify(giftCertificateRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = 3L)
    void unbindTag(Long id) throws ServiceException {
        GiftCertificate certificate = giftCertificateService.findById(id).get();

        giftCertificateService.unbindTag(id, 1L);

        verify(giftCertificateRepository, times(2)).findById(id);
        verify(giftCertificateRepository).save(any(GiftCertificate.class));

        assertTrue(certificate.getTags().isEmpty());
    }
}