package com.epam.esm.service.impl;

import com.epam.esm.config.profile.TestProfileConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import repository.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestProfileConfig.class)
@ActiveProfiles("test")
public class TagServiceImplTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void findOne() {
        when(tagRepository.findOne(anyLong())).thenAnswer(invocation -> {
            Tag t = new Tag();
            t.setId(invocation.getArgument(0));
            t.setName("test name");
            return Optional.of(t);
        });

        Optional<Tag> actual = tagService.findOne(51L);
        assertTrue(actual.isPresent());
    }

    @Test
    void save() {
        when(tagRepository.save(any()))
                .thenThrow(new DuplicateKeyException("This tag name already exists"));
        Assertions.assertThrows(ServiceException.class, () -> tagService.save(new Tag()));
    }

    @Test
    void delete() {
        tagService.delete(999L);
        verify(tagRepository, times(1)).delete(anyLong());
    }
}