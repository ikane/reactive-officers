package com.oreilly.reactiveofficers.dao;

import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficerRepositoryTest {

    List<Officer>  officers = Arrays.asList(
      Officer.builder().rank(Rank.CAPTAIN).first("James").last("Kirk").build(),
      Officer.builder().rank(Rank.CAPTAIN).first("Jean-Luc").last("Picard").build(),
      Officer.builder().rank(Rank.CAPTAIN).first("Benjamin").last("Sisco").build(),
      Officer.builder().rank(Rank.CAPTAIN).first("Kathryn").last("Janeway").build(),
      Officer.builder().rank(Rank.CAPTAIN).first("Jonathan").last("Archer").build()
    );

    @Autowired
    private OfficerRepository repository;

    @Before
    public void setUp() throws Exception {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .then()
                .block();
    }
}