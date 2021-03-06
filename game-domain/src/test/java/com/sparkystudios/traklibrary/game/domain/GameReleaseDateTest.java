package com.sparkystudios.traklibrary.game.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.time.LocalDate;

@DataJpaTest
class GameReleaseDateTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void persist_withNullRegion_throwsPersistenceException() {
        // Arrange
        GameReleaseDate gameReleaseDate = new GameReleaseDate();
        gameReleaseDate.setRegion(null);
        gameReleaseDate.setReleaseDate(LocalDate.now());

        Game game = new Game();
        game.setTitle("test-title");
        game.setDescription("test-description");
        game.setAgeRating(AgeRating.MATURE);
        game.addReleaseDate(gameReleaseDate);

        // Assert
        Assertions.assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(() -> testEntityManager.persistFlushFind(game));
    }

    @Test
    void persist_withValidGameReleaseDateAndRelationship_mapsGameReleaseDate() {
        // Arrange
        GameReleaseDate gameReleaseDate = new GameReleaseDate();
        gameReleaseDate.setRegion(GameRegion.PAL);
        gameReleaseDate.setReleaseDate(LocalDate.now());

        Game game = new Game();
        game.setTitle("test-title");
        game.setDescription("test-description");
        game.setAgeRating(AgeRating.MATURE);
        game.addReleaseDate(gameReleaseDate);

        // Act
        game = testEntityManager.persistFlushFind(game);
        GameReleaseDate result = game.getReleaseDates().iterator().next();

        // Assert
        Assertions.assertThat(result.getId()).isPositive();
        Assertions.assertThat(result.getGame().getId())
                .isEqualTo(game.getId());
        Assertions.assertThat(result.getRegion()).isEqualTo(gameReleaseDate.getRegion());
        Assertions.assertThat(result.getReleaseDate()).isEqualTo(gameReleaseDate.getReleaseDate());
        Assertions.assertThat(result.getCreatedAt()).isNotNull();
        Assertions.assertThat(result.getUpdatedAt()).isNotNull();
        Assertions.assertThat(result.getVersion()).isNotNull().isNotNegative();
    }
}
