package br.com.techchallengefiap.techchallenge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    @GetMapping("/ola")
    public String OlaImbecil() {

        return "Olá imbecil";
    }
}
