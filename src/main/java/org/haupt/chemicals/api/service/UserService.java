package org.haupt.chemicals.api.service;

import org.haupt.chemicals.api.model.Product;
import org.haupt.chemicals.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    /**
     * provide user information for the user identified by the given user name
     * @param username username of the requested user
     * @return user details of the requested user
     * @throws UsernameNotFoundException in case the user does not exist in the database
     */
    @Transactional(propagation= Propagation.REQUIRED)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<org.haupt.chemicals.api.model.User> optional = repository.findById(username);
        if (optional.isEmpty()) throw new UsernameNotFoundException("no such user");
        org.haupt.chemicals.api.model.User user = optional.get();
        return User.withUsername(username)
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
