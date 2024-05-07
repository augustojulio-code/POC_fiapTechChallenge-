package br.com.techchallengefiap.techchallenge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.resources.preference.Preference;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    /*
     * @Value("${mercadopago.client-id}")
     * public String clientId;
     * 
     * @Value("${mercadopago.client-secret}")
     * public String clientSecret;
     * 
     * @GetMapping
     * public String generateQRCode() {
     * 
     * MercadoPagoConfig.setAccessToken(clientId);
     * 
     * Preference preference = new Preference();
     * 
     * preference.getInitPoint();
     * return null;
     * }
     */
}
