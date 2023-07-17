package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.DollarApiResponse;
import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;
import com.bbva.wallet.enums.DollarType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@RestController
@RequestMapping("/trading")
@RequiredArgsConstructor
public class DollarSimulationController {

    private final String apiUrl = "https://api.bluelytics.com.ar/v2/latest";

    @PostMapping("/simulateUsdToArs")
    public ResponseEntity<DollarSimulationResultDTO> simulateDollarTransaction(@RequestBody DollarSimulationDTO dto) {
        RestTemplate restTemplate = new RestTemplate();
        DollarApiResponse response = restTemplate.getForObject(apiUrl, DollarApiResponse.class);
        double dollarValueInPesos = getDollarValueByType(response, dto.getDollarType());

        double impuestoPais = dollarValueInPesos * 0.3;
        double retencionGanancias = dollarValueInPesos * 0.45;
        double amountInDollars = (dto.getAmountInPesos()) / (dollarValueInPesos + impuestoPais + retencionGanancias);

        DollarSimulationResultDTO resultDTO = new DollarSimulationResultDTO(
                roundDecimalValue(dollarValueInPesos, 2),
                response.getLastUpdate(),
                roundDecimalValue(impuestoPais, 2),
                roundDecimalValue(retencionGanancias, 2),
                roundDecimalValue(amountInDollars, 2)
        );

        return ResponseEntity.ok(resultDTO);
    }

    private double getDollarValueByType(DollarApiResponse response, DollarType dollarType) {
        if (Objects.requireNonNull(dollarType) == DollarType.OFICIAL) {
            return response.getOfficial().getValueSell();
        }
        throw new IllegalArgumentException("Tipo de dólar no válido: " + dollarType);
    }

    private double roundDecimalValue(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}