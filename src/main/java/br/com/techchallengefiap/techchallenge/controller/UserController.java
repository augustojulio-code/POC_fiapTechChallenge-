package br.com.techchallengefiap.techchallenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techchallengefiap.techchallenge.domain.User;

@RestController
@RequestMapping("/")
public class UserController {

    @GetMapping("/ola")
    public String OlaImbecil() {

        return "Olá imbecil";
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> findAll() {

        // criar service
        List<User> list = null;

        return ResponseEntity.ok().body(list);

    }
}
