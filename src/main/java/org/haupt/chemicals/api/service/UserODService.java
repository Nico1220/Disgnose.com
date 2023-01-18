package org.haupt.chemicals.api.service;

import org.haupt.chemicals.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserODService {
    @Autowired
    private UserRepository repository;

    public List<org.haupt.chemicals.api.model.User> findUsersByEmil(String title) {
        List<org.haupt.chemicals.api.model.User> users = new ArrayList<>();
        repository.findListUserByMail(title)
                .forEach(users::add);
        for(org.haupt.chemicals.api.model.User user:users)
            System.out.println(user);
        return users;
    }
}
