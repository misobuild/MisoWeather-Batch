package com.misoweather.misoweatherservice.global.utils;

import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryExtends <T extends JpaRepository <?, Long>>{
    private T repository;

    public void printOut(T repository){
        this.repository = repository;
    }

    public T get(){
        return repository;
    }
}
