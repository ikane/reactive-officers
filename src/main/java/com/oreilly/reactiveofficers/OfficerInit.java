package com.oreilly.reactiveofficers;

import com.oreilly.reactiveofficers.dao.OfficerRepository;
import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class OfficerInit implements ApplicationRunner {

    @Autowired
    private OfficerRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repository.deleteAll()
                .thenMany(Flux.just(
                        Officer.builder().rank(Rank.CAPTAIN).first("James").last("Kirk").build(),
                        Officer.builder().rank(Rank.CAPTAIN).first("Jean-Luc").last("Picard").build(),
                        Officer.builder().rank(Rank.CAPTAIN).first("Benjamin").last("Sisco").build(),
                        Officer.builder().rank(Rank.CAPTAIN).first("Kathryn").last("Janeway").build(),
                        Officer.builder().rank(Rank.CAPTAIN).first("Jonathan").last("Archer").build()
                ))
                .flatMap(repository::save)
                .thenMany(repository.findAll())
                .subscribe(officer -> log.info(officer.toString()))
                //.subscribe(System.out::println)
        ;
    }
}
