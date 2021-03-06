package com.sparkystudios.traklibrary.game.service.impl;

import com.sparkystudios.traklibrary.game.domain.GameBarcode;
import com.sparkystudios.traklibrary.game.repository.GameBarcodeRepository;
import com.sparkystudios.traklibrary.game.service.dto.GameBarcodeDto;
import com.sparkystudios.traklibrary.game.service.mapper.GameBarcodeMapper;
import com.sparkystudios.traklibrary.game.service.mapper.GameMappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import javax.persistence.EntityNotFoundException;
import java.util.Locale;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameBarcodeServiceImplTest {

    @Mock
    private GameBarcodeRepository gameBarcodeRepository;

    @Spy
    private final GameBarcodeMapper gameBarcodeMapper = GameMappers.GAME_BARCODE_MAPPER;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GameBarcodeServiceImpl gameBarcodeService;

    @Test
    void findByBarcode_withInvalidBarcode_throwsEntityNotFoundException() {
        // Arrange
        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        Mockito.when(gameBarcodeRepository.findByBarcode(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> gameBarcodeService.findByBarcode("barcode"));
    }

    @Test
    void findByBarcode_withValidBarcode_returnsGameBarcodeDto() {
        // Arrange
        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        Mockito.when(gameBarcodeRepository.findByBarcode(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(new GameBarcode()));

        // Act
        GameBarcodeDto result = gameBarcodeService.findByBarcode("barcode");

        // Assert
        Assertions.assertNotNull(result, "The mapped result should not be null if the barcode was found.");
    }
}
