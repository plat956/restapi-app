package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @ParameterizedTest
    @ValueSource(longs = 34L)
    void findOne(Long id) {
        userService.findById(id);
        verify(userRepository).findById(id);
    }

    @Test
    void findAllPaginated() {
        Pageable page = PageRequest.of(10, 20);
        userService.findAll(page);
        verify(userRepository).findAll(page);
    }
}