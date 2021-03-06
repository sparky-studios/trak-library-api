package com.sparkystudios.traklibrary.game.service.impl;

import com.sparkystudios.traklibrary.game.domain.Game;
import com.sparkystudios.traklibrary.game.repository.GameRepository;
import com.sparkystudios.traklibrary.game.repository.GenreRepository;
import com.sparkystudios.traklibrary.game.repository.specification.GameSpecification;
import com.sparkystudios.traklibrary.game.service.dto.GameDetailsDto;
import com.sparkystudios.traklibrary.game.service.mapper.GameDetailsMapper;
import com.sparkystudios.traklibrary.game.service.mapper.GameMappers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExtendWith(MockitoExtension.class)
class GameDetailsServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GenreRepository genreRepository;

    @Spy
    private final GameDetailsMapper gameDetailsMapper = GameMappers.GAME_DETAILS_MAPPER;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GameDetailsServiceImpl gameDetailsService;

    @Test
    void findByGameId_withEmptyOptional_throwsEntityNotFoundException() {
        // Arrange
        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> gameDetailsService.findByGameId(0L));
    }

    @Test
    void findByGameId_withValidGame_returnsGameDetailsDto() {
        // Arrange
        Game game = new Game();
        game.setId(1L);
        game.setTitle("test-title");
        game.setDescription("test-description");
        game.setVersion(1L);

        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        Mockito.when(gameRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(game));

        // Act
        GameDetailsDto result = gameDetailsService.findByGameId(0L);

        // Assert
        Assertions.assertNotNull(result);
    }

    @Test
    void findByGenreId_withNonExistentGenre_throwsEntityNotFoundException() {
        // Arrange
        Mockito.when(genreRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(false);

        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        Pageable pageable = Mockito.mock(Pageable.class);

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> gameDetailsService.findByGenreId(0L, pageable));
    }

    @Test
    void findByGenreId_withNoGames_returnsEmptyList() {
        // Arrange
        Mockito.when(genreRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);

        Mockito.when(gameRepository.findByGenresId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        // Act
        List<GameDetailsDto> result = StreamSupport.stream(gameDetailsService.findByGenreId(0L, Mockito.mock(Pageable.class)).spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        Assertions.assertTrue(result.isEmpty(), "The result should be empty if no games are returned.");

        Mockito.verify(gameDetailsMapper, Mockito.never())
                .gameToGameDetailsDto(ArgumentMatchers.any());
    }

    @Test
    void findByGenreId_withMultipleGames_returnsList() {
        // Arrange

        Mockito.when(genreRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);

        Mockito.when(gameRepository.findByGenresId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(new Game(), new Game())));

        // Act
        List<GameDetailsDto> result = StreamSupport.stream(gameDetailsService.findByGenreId(0L, Mockito.mock(Pageable.class)).spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        Assertions.assertFalse(result.isEmpty(), "The result should not be empty if games are returned.");

        Mockito.verify(gameDetailsMapper, Mockito.atMost(2))
                .gameToGameDetailsDto(ArgumentMatchers.any());
    }

    @Test
    void countByGenreId_withNonExistentGenre_throwsEntityNotFoundException() {
        // Arrange
        Mockito.when(genreRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(false);

        Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
                .thenReturn("");

        // Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> gameDetailsService.countByGenreId(0L));
    }

    @Test
    void countByGenreId_withGenre_invokesGameGenreXrefRepository() {
        // Arrange
        Mockito.when(genreRepository.existsById(ArgumentMatchers.anyLong()))
                .thenReturn(true);

        Mockito.when(gameRepository.countByGenresId(ArgumentMatchers.anyLong()))
                .thenReturn(0L);

        // Act
        gameDetailsService.countByGenreId(0L);

        // Assert
        Mockito.verify(gameRepository, Mockito.atMostOnce())
                .countByGenresId(ArgumentMatchers.anyLong());
    }

    @Test
    void findAll_withNullPageable_throwsNullPointerException() {
        // Arrange
        GameSpecification gameSpecification = Mockito.mock(GameSpecification.class);

        // Assert
        Assertions.assertThrows(NullPointerException.class, () -> gameDetailsService.findAll(gameSpecification, null));
    }

    @Test
    void findAll_withNoGames_returnsEmptyList() {
        // Arrange
        Mockito.when(gameRepository.findAll(ArgumentMatchers.any(GameSpecification.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(Page.empty());

        GameSpecification gameSpecification = Mockito.mock(GameSpecification.class);
        Pageable pageable = Mockito.mock(Pageable.class);

        // Act
        List<GameDetailsDto> result = StreamSupport.stream(gameDetailsService.findAll(gameSpecification, pageable).spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        Assertions.assertTrue(result.isEmpty(), "The result should be empty if no pages game results were found.");
    }

    @Test
    void findAll_withGames_returnsGamesAsGameDetailsDtos() {
        // Arrange
        Page<Game> games = new PageImpl<>(Arrays.asList(new Game(), new Game()));

        Mockito.when(gameRepository.findAll(ArgumentMatchers.any(GameSpecification.class), ArgumentMatchers.any(Pageable.class)))
                .thenReturn(games);

        GameSpecification gameSpecification = Mockito.mock(GameSpecification.class);
        Pageable pageable = Mockito.mock(Pageable.class);

        // Act
        List<GameDetailsDto> result = StreamSupport.stream(gameDetailsService.findAll(gameSpecification, pageable).spliterator(), false)
                .collect(Collectors.toList());

        // Assert
        Assertions.assertFalse(result.isEmpty(), "The result shouldn't be empty if the repository returned games.");
    }

    @Test
    void count_withNullGameSpecification_throwsNullPointerException() {
        // Assert
        Assertions.assertThrows(NullPointerException.class, () -> gameDetailsService.count(null));
    }

    @Test
    void count_withValidGameSpecification_invokesCount() {
        // Arrange
        Mockito.when(gameRepository.count(ArgumentMatchers.any(GameSpecification.class)))
                .thenReturn(0L);

        // Act
        gameDetailsService.count(Mockito.mock(GameSpecification.class));

        // Assert
        Mockito.verify(gameRepository, Mockito.atMostOnce())
                .count(Mockito.any(GameSpecification.class));
    }
}
