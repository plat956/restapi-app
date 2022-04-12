package com.epam.esm.service.impl;

import com.epam.esm.config.profile.TestProfileConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import repository.GiftCertificateRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestProfileConfig.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Autowired
    private GiftCertificateService giftCertificateService;

    private List<GiftCertificate> giftCertificates;

    @BeforeAll
    void beforeTestClass() {
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

        giftCertificates.add(c1);
        giftCertificates.add(c2);
        giftCertificates.add(c3);

        when(giftCertificateRepository.findOne(anyLong())).
                thenAnswer(invocation -> giftCertificates.stream().
                        filter(u -> u.getId().equals(invocation.getArgument(0))).
                        findAny());
    }

    @Test
    void findOne() {
        Optional<GiftCertificate> cert = giftCertificateService.findOne(2L);
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
        GiftCertificate c = new GiftCertificate();
        c.setPrice(BigDecimal.ZERO);

        GiftCertificateService spyService = spy(giftCertificateService);
        spyService.update(id, c);
        verify(spyService).findOne(id);

        ArgumentCaptor<GiftCertificate> updateArgument = ArgumentCaptor.forClass(GiftCertificate.class);
        verify(giftCertificateRepository).update(updateArgument.capture());

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
    void delete(Long id) {
        giftCertificateService.delete(id);
        verify(giftCertificateRepository, times(1)).delete(id);
    }

    @AfterAll
    public void tearDown() {
        verify(giftCertificateRepository, atLeast(2)).findOne(anyLong());
    }
}