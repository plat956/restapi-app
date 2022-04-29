package com.epam.esm.repository;

import com.epam.esm.dto.UserStatisticsDto;
import com.epam.esm.entity.User;
import com.epam.esm.util.RequestedPage;
import org.springframework.hateoas.PagedModel;

public interface UserRepository extends BaseRepository<Long, User> {

    PagedModel<UserStatisticsDto> findUserStatistics(RequestedPage page);
}
