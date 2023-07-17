package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.DollarApiResponse;
import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;
import com.bbva.wallet.enums.DollarType;
import com.bbva.wallet.services.DollarSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor

public class DollarSimulationServiceImpl implements DollarSimulationService {

    private final String apiUrl = "https://api.bluelytics.com.ar/v2/latest";
    private final RestTemplate restTemplate;

    @Value("${app.impuestoPais}")
    private Double impuestoPais;
    @Value("${app.retencionGanancias}")
    private Double retencionGanancias;


    @Override
    public DollarSimulationResultDTO simulateDollarTransaction(DollarSimulationDTO dto) {
        DollarApiResponse response = restTemplate.getForObject(apiUrl, DollarApiResponse.class);
        double dollarValueWithTax = getDollarValueByType(response, dto.getDollarType());
        dollarValueWithTax = dollarValueWithTax * (1 + impuestoPais + retencionGanancias);
        double amountInDollars = (dto.getAmountInPesos() / dollarValueWithTax );

        return new DollarSimulationResultDTO(
                roundDecimalValue(getDollarValueByType(response, dto.getDollarType()), 2),
                roundDecimalValue(dollarValueWithTax, 2),
                response.getLastUpdate(),
                roundDecimalValue(impuestoPais, 2),
                roundDecimalValue(retencionGanancias, 2),
                roundDecimalValue(amountInDollars, 2)
        );
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
