package br.com.techchallengefiap.techchallenge.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class PaymentController {

    @Value("${mercadopago.access-token}")
    public String token;

    PreferenceClient preferenceClient = new PreferenceClient();

    @GetMapping("p")
    public String generateQRCode() {

        // Configura chave acesso de teste da conta mercado pago
        MercadoPagoConfig.setAccessToken(token);

        try {
            // Cria um item
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id("1")
                    .title("teste teste")
                    .quantity(2)
                    .unitPrice(new BigDecimal("10"))
                    .currencyId("BRL")
                    .build();

            // Cria uma lista de itens
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            // Set o condicional em caso de sucesso e falha
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://test.com/success")
                    .failure("http://test.com/failure")
                    .pending("http://test.com/pending")
                    .build();
            // Seleciona os meios de pagamento
            PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                    .defaultPaymentMethodId("master")
                    .build();
            // Cria a solicitação em um objeto
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .backUrls(backUrls)
                    .items(items)
                    .paymentMethods(paymentMethods)
                    .build();

            // Cria o pagamento
            Preference preference = preferenceClient.create(preferenceRequest);

            // Retorna link para efetuar pagamento
            return preference.getInitPoint();
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @GetMapping("/qr")
    public void geraQRcode(HttpServletResponse response) throws IOException, WriterException {

        String sourceQRcode = testTestes();

        BitMatrix bitMatrix = new MultiFormatWriter().encode(sourceQRcode, BarcodeFormat.QR_CODE, 200, 200);

        // Converte o BitMatrix em uma imagem PNG
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        // Configura a resposta HTTP para retornar a imagem do QR code
        response.setContentType("image/png");
        response.setContentLength(qrCodeBytes.length);

        // Escreve a imagem do QR code na resposta HTTP
        OutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(qrCodeBytes);
        responseOutputStream.flush();
        responseOutputStream.close();

    }

    @GetMapping("pay")
    public void redirectUrl(HttpServletResponse response) throws IOException {
        String url = generateQRCode();

        response.sendRedirect(url);
    }

    @GetMapping("t")
    public String testTestes() {
        MercadoPagoConfig.setAccessToken(token);
        PaymentClient paymentClient = new PaymentClient();

        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .transactionAmount(new BigDecimal(10))
                .description("Teste")
                .paymentMethodId("pix")
                .payer(
                        PaymentPayerRequest.builder()
                                .email("teste@test.com")
                                .firstName("ze da mina ")
                                .lastName("Abandonada")
                                .build())
                .build();

        try {
            Payment createdPayment = paymentClient.create(paymentCreateRequest);
            return createdPayment.getPointOfInteraction().getTransactionData().getQrCode();
        } catch (MPException | MPApiException e) {
            return e.getMessage();
        }
    }

}
