package com.proto.service;

import com.proto.entity.AppUser;
import com.proto.exception.UserAlreadyExistsException;
import com.proto.model.event.User;
import com.proto.repository.UserRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class UserService {
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public List<AppUser> findByAccountIdentifier(String accountIdentifier) {
        return userRepository.findByAccountIdentifier(accountIdentifier);
    }

    public AppUser findByUuid(String uuid) {
        return userRepository.findOne(uuid);
    }

    /**
     * Finds a user by its authentication token
     *
     * @param authentication
     * @return
     */
    public AppUser findByAuthenticationToken(OpenIDAuthenticationToken authentication) {
        for (OpenIDAttribute attribute : authentication.getAttributes()) {
            if ("uuid".equals(attribute.getName())) {
                String uuid = attribute.getValues().get(0);
                AppUser user = userRepository.findOne(uuid);

                if (user != null) {
                    return user;
                } else {
                    LOGGER.error("User not found for uuid {}", uuid);
                }
            }
        }

        LOGGER.error("Invalid authentication token {}", authentication);
        throw new IllegalStateException("Invalid authentication token");
    }

    /**
     * Creates and saves a user from an event.
     *
     * @param eventUser
     * @param accountIdentifier
     * @return
     * @throws UserAlreadyExistsException if a user with that uuid already exists
     */
    public AppUser createUser(User eventUser, String accountIdentifier) throws UserAlreadyExistsException {
        // Throw an exception if the user already exists
        if (userRepository.exists(eventUser.getUuid())) {
            throw new UserAlreadyExistsException();
        }

        // Save new user
        AppUser user = mapper.map(eventUser, AppUser.class);
        user.setAccountIdentifier(accountIdentifier);
        userRepository.save(user);

        LOGGER.info("Saved user {}", user);

        return user;
    }

    /**
     * Delete all users associated with an account identifier
     *
     * @param accountIdentifier
     */
    public void deleteByAccountIdentifier(String accountIdentifier) {
        userRepository.deleteByAccountIdentifier(accountIdentifier);
    }

    /**
     * Delete all users associated with an account identifier
     *
     * @param uuid
     */
    public void deleteByUuid(String uuid) {
        userRepository.delete(uuid);
    }
}
