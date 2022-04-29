package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.MessageProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagServiceImplTest {

    @Mock
    private MessageProvider messageProvider;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

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