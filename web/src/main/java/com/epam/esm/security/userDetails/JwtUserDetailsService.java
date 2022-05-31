package com.epam.esm.security.userDetails;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private MessageProvider messageProvider;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository, MessageProvider messageProvider) {
        this.userRepository = userRepository;
        this.messageProvider = messageProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByLogin(login);
        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException(messageProvider.getMessage("message.error.wrong-login", login));
        }

        return JwtUserDetailsFactory.produce(userOptional.get());
    }
}
