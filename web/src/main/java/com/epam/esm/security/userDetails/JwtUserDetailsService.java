package com.epam.esm.security.userDetails;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;
    private MessageProvider messageProvider;

    @Autowired
    public JwtUserDetailsService(UserService userService, MessageProvider messageProvider) {
        this.userService = userService;
        this.messageProvider = messageProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.findByLogin(login);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException(messageProvider.getMessage("message.error.wrong-login", login));
        }

        return JwtUserDetailsFactory.produce(userOptional.get());
    }
}
