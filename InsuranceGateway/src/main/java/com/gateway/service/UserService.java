package com.gateway.service;

import com.gateway.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

	User save(User u);

}
