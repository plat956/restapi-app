package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.RequestedPage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        userService.findOne(id);
        verify(userRepository).findOne(id);
    }

    @ParameterizedTest
    @ValueSource(longs = 115L)
    void findAllPaginated() {
        RequestedPage page = new RequestedPage(10L, 20L);
        userService.findAllPaginated(page);
        verify(userRepository).findAllPaginated(page);
    }
}