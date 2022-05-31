package com.epam.esm.mapper.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityMappingException;
import com.epam.esm.mapper.EntityMapper;
import com.epam.esm.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements EntityMapper<UserDto, User> {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserMapperImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User toEntity(UserDto dto) throws EntityMappingException {
        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName(DEFAULT_USER_ROLE).get());
        user.setStatus(User.Status.ACTIVE);
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}
