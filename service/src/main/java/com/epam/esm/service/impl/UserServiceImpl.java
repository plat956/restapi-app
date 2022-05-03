package com.epam.esm.service.impl;

import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<UserStatisticsDto> findUserStatistics(Pageable pageable) {
        Page<Tuple> tuples = userRepository.findUserStatistics(pageable);
        List<UserStatisticsDto> dtos = tuples.stream().map(this::mapTupleToStatisticsDto).toList();
        return new PageImpl<>(dtos, tuples.getPageable(), tuples.getTotalElements());
    }

    private UserStatisticsDto mapTupleToStatisticsDto(Tuple tuple) {
        Long userId = tuple.get("user_id", BigInteger.class).longValue();
        BigDecimal maxAmount = tuple.get("max_amount", BigDecimal.class);
        Long tagId = tuple.get("tag_id", Integer.class).longValue();
        Integer tagCount = tuple.get("tag_count", BigInteger.class).intValue();
        return new UserStatisticsDto(userId, maxAmount, tagId, tagCount);
    }
}
