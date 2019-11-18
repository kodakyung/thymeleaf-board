package com.example.demo.domain;

import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles(value = "test")
public class BaseEntityTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void id값생성테스트() {
        User user = userRepository.save(User.builder().account("testId").password("testPasswd").build());
        log.info(user.toString());
        assertNotNull(user.getIdx());
    }

    @Test
    public void jpaAuditing_createdDate_테스트() {
        User user = userRepository.save(User.builder().account("testId2").password("testPasswd").build());
        log.info(user.toString());
        assertNotNull(user.getCreatedDate());
    }

    @Test
    public void jpaAuditing_updatedDate_테스트() {
        // given
        User user = userRepository.save(User.builder().account("testId3").password("testPasswd").build());
        User foundUser = userRepository.findById(user.getIdx()).orElseThrow(IllegalArgumentException::new);
        log.info("foundUser create date:{}", foundUser.getCreatedDate());
        log.info("foundUser updated date:{}", foundUser.getUpdatedDate());

        // when
        foundUser.setPassword("1234");
        User changedUser = userRepository.save(foundUser);

        // then
        assertNotNull(changedUser.getCreatedDate());
        log.info("changedUser create date:{}", changedUser.getCreatedDate().toString());
        log.info("changedUser update date:{}", changedUser.getUpdatedDate().toString());
        assertNotEquals(changedUser.getCreatedDate(), changedUser.getUpdatedDate());
    }
}