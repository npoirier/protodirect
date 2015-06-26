package com.proto.repository;

import com.proto.ProtodirectApplication;
import com.proto.entity.AppUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProtodirectApplication.class)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUuid() throws Exception {
        AppUser user = new AppUser();
        user.setUuid("dummy-uuid");
        user.setEmail("npoirier@gmail.com");

        userRepository.save(user);

        AppUser savedUser = userRepository.findOne("dummy-uuid");
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(savedUser.getEmail(), "npoirier@gmail.com");
    }

}
