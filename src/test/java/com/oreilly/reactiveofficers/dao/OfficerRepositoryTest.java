package com.oreilly.reactiveofficers.dao;

import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

    @Test
    public void save() {
        Officer lorca = Officer.builder().rank(Rank.CAPTAIN).first("Gabriel").last("Lorca").build();
        StepVerifier.create(repository.save(lorca))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }

    @Test
    public void findAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void findById() {
        officers.stream()
                .map(Officer::getId)
                .forEach(id ->
                        StepVerifier.create(repository.findById(id))
                                .expectNextCount(1)
                                .verifyComplete());
    }

    @Test
    public void findByIdNotExist() {
        StepVerifier.create(repository.findById("xyz"))
                .verifyComplete();
    }

    @Test
    public void count() {
        StepVerifier.create(repository.count())
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    public void findByRank() {
        StepVerifier.create(
                repository.findByRank(Rank.CAPTAIN)
                        .map(Officer::getRank)
                        .distinct())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(
                repository.findByRank(Rank.ENSIGN)
                        .map(Officer::getRank)
                        .distinct())
                .verifyComplete();
    }

    @Test
    public void findByLast() {
        officers.stream()
                .map(Officer::getLast)
                .forEach(lastName ->
                        StepVerifier.create(repository.findByLast(lastName))
                                .expectNextMatches(officer ->
                                        officer.getLast().equals(lastName))
                                .verifyComplete());
    }
}