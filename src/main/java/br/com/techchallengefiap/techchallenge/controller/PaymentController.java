package br.com.techchallengefiap.techchallenge.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

@RestController
@RequestMapping("/")
public class PaymentController {

    @Value("${mercadopago.access-token}")
    public String token;

    PreferenceClient preferenceClient = new PreferenceClient();

    @GetMapping("p")
    public String generateQRCode() {

        MercadoPagoConfig.setAccessToken(token);

        try {
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("1")
                    .title("teste teste")
                    .quantity(2)
                    .unitPrice(new BigDecimal("10"))
                    .currencyId("BRL")
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .backUrls(
                            PreferenceBackUrlsRequest.builder()
                                    .success("http://test.com/success")
                                    .failure("http://test.com/failure")
                                    .pending("http://test.com/pending")
                                    .build())
                    .items(items)
                    .build();

            Preference preference = preferenceClient.create(preferenceRequest);

            return preference.getInitPoint();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
