package com.sparkystudios.traklibrary.game.service;

import com.sparkystudios.traklibrary.game.domain.*;
import com.sparkystudios.traklibrary.game.repository.specification.GameSpecification;
import com.sparkystudios.traklibrary.game.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import javax.json.JsonMergePatch;
import java.util.Collection;

/**
 * The {@link GameService} follows the basic CRUD principle for interaction with {@link Game} entities on the persistence layer.
 * However, the {@link GameService} builds an additional layer of abstraction, with the primary purpose being checking the validity of
 * {@link Game} data being requested from the persistence layer, as well as encapsulating any domain-based objects into {@link GameDto}
 * transfer objects, for additional validation and protection.
 *
 * The {@link GameService} still follows the practise in that it will not catch or handle exceptions thrown by the persistence layer,
 * instead it will wrap them in a more reasonable response and propagate the exception to the callee.
 *
 * @since 0.1.0
 * @author Sparky Studios
 */
public interface GameService {

    /**
     * Given a {@link GameDto} instance, the service will attempt to persist the data with the underlying persistence layer.
     * If the {@link GameDto} supplied contains an Id that matches an existing entity, insertion to the persistence layer will
     * fail and a {@link javax.persistence.EntityExistsException} will be thrown. If persistence succeeds, the data is saved and the
     * saved entity is returned as a {@link GameDto}.
     *
     * The method does not allow <code>null</code> entities to be inserted, if null is provided, a {@link NullPointerException}
     * will be thrown.
     *
     * @param gameDto The {@link GameDto} instance to persist.
     *
     * @return The updated persisted entity as a {@link GameDto}.
     */
    GameDto save(GameDto gameDto);

    /**
     * Given an ID of a {@link Game} entity, this service method will query the underlying persistence layer and try and
     * retrieve the {@link Game} entity that matches the given ID and map it to a {@link GameDto}. If the Id provided does not
     * map to any known {@link Game} entity, then an exception will be thrown specifying that it can't be found.
     *
     * @param id The ID of the {@link Game} entity to try and retrieve.
     *
     * @return The {@link Game} entity matching the ID mapped to a {@link GameDto}.
     */
    GameDto findById(long id);

    /**
     * Given the ID of a {@link Genre}, this method will retrieve a {@link Page} of {@link Game}s that are associated with
     * the given {@link Genre}. If the ID provided does not map to any {@link Genre}, a {@link javax.persistence.EntityNotFoundException}
     * will be thrown. If there are no {@link Game}s associated with the {@link Genre}, an empty {@link Iterable} will be returned.
     *
     * @param genreId The ID of the {@link Genre} to retrieve {@link Game} entries for.
     * @param pageable The amount of data and the page to return.
     *
     * @return A page of {@link GameDto}s that are mapped with the given {@link Genre}.
     */
    Iterable<GameDto> findGamesByGenreId(long genreId, Pageable pageable);

    /**
     * Given the ID of a {@link Genre}, this method will retrieve the total count for how many {@link GameDto}'s have an association
     * to the given {@link Genre}. If the ID provided does not map to any {@link Genre}, {@link javax.persistence.EntityNotFoundException}
     * will be thrown.
     *
     * @param genreId The ID of the {@link Genre} to retrieve the total count of {@link Game} entries for.
     *
     * @return The total count of {@link GameDto}'s associated with the given {@link Genre}.
     */
    long countGamesByGenreId(long genreId);

    /**
     * This method is used to associate a {@link Collection} of {@link GenreDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link GenreDto} ID's don't
     * currently exist within the database, then the elements won't be persisted
     *
     * It should be noted, that this method will remove all existing {@link GenreDto} associations and replace them
     * with the new ID's provided.
     *
     * @param id The ID of the {@link GameDto} to associated {@link GenreDto}'s with.
     * @param genreIds A {@link Collection} of {@link GenreDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link GenreDto}'s have been associated with.
     */
    GameDto saveGenresForGameId(long id, @NonNull Collection<Long> genreIds);

    /**
     * This method is used to associate a {@link Collection} of {@link GenreDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link GenreDto} ID's don't
     * currently exist within the database, then the elements won't be persisted
     *
     * It should be noted, that this method will only update the {@link GameDto} with new associations, no existing
     * associations will be removed.
     *
     * @param id The ID of the {@link GameDto} to associated {@link GenreDto}'s with.
     * @param genreIds A {@link Collection} of {@link GenreDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link GenreDto}'s have been associated with.
     */
    GameDto updateGenresForGameId(long id, @NonNull Collection<Long> genreIds);

    /**
     * Given the ID of a {@link Platform}, this method will retrieve a {@link Page} of {@link Game}s that are associated with
     * the given {@link Platform}. If the ID provided does not map to any {@link Platform}, a {@link javax.persistence.EntityNotFoundException}
     * will be thrown. If there are no {@link Game}s associated with the {@link Platform}, an empty {@link Iterable} will be returned.
     *
     * @param platformId The ID of the {@link Platform} to retrieve {@link Game} entries for.
     * @param pageable The amount of data and the page to return.
     *
     * @return A page of {@link GameDto}s that are mapped with the given {@link Platform}.
     */
    Iterable<GameDto> findGamesByPlatformId(long platformId, Pageable pageable);

    /**
     * Given the ID of a {@link Platform}, this method will retrieve the total count for how many {@link GameDto}'s have an association
     * to the given {@link Platform}. If the ID provided does not map to any {@link Platform}, {@link javax.persistence.EntityNotFoundException}
     * will be thrown.
     *
     * @param platformId The ID of the {@link Platform} to retrieve the total count of {@link Game} entries for.
     *
     * @return The total count of {@link GameDto}'s associated with the given {@link Platform}.
     */
    long countGamesByPlatformId(long platformId);

    /**
     * This method is used to associate a {@link Collection} of {@link PlatformDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link PlatformDto} ID's don't
     * currently exist within the database, then the elements won't be persisted.
     *
     * It should be noted, that this method will remove all existing {@link PlatformDto} associations and replace them
     * with the new ID's provided.
     *
     * @param id The ID of the {@link GameDto} to associated {@link PlatformDto}'s with.
     * @param platformsIds A {@link Collection} of {@link PlatformDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link PlatformDto}'s have been associated with.
     */
    GameDto savePlatformsForGameId(long id, @NonNull Collection<Long> platformsIds);

    /**
     * This method is used to associate a {@link Collection} of {@link PlatformDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link PlatformDto} ID's don't
     * currently exist within the database, then the elements won't be persisted
     *
     * It should be noted, that this method will only update the {@link GameDto} with new associations, no existing
     * associations will be removed.
     *
     * @param id The ID of the {@link GameDto} to associated {@link PlatformDto}'s with.
     * @param platformIds A {@link Collection} of {@link PlatformDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link PlatformDto}'s have been associated with.
     */
    GameDto updatePlatformsForGameId(long id, @NonNull Collection<Long> platformIds);

    /**
     * Given the ID of a {@link Developer}, this method will retrieve a {@link Page} of {@link Game}s that are associated with
     * the given {@link DeveloperDto}. If the ID provided does not map to any {@link DeveloperDto}, a {@link javax.persistence.EntityNotFoundException}
     * will be thrown. If there are no {@link Game}s associated with the {@link DeveloperDto}, an empty {@link Iterable} will be returned.
     *
     * @param developerId The ID of the {@link Developer} to retrieve {@link Game} entries for.
     * @param pageable The amount of data and the page to return.
     *
     * @return A page of {@link GameDto}s that are mapped with the given {@link Developer} entity.
     */
    Iterable<GameDto> findGamesByDeveloperId(long developerId, Pageable pageable);

    /**
     * Given the ID of a {@link Developer}, this method will retrieve the total count for how many {@link GameDto}'s have an association
     * to the given {@link Developer}. If the ID provided does not map to any {@link Developer}, {@link javax.persistence.EntityNotFoundException}
     * will be thrown.
     *
     * @param developerId The ID of the {@link Developer} to retrieve the total count of {@link Game} entries for.
     *
     * @return The total count of {@link GameDto}'s associated with the given {@link Developer}.
     */
    long countGamesByDeveloperId(long developerId);

    /**
     * This method is used to associate a {@link Collection} of {@link DeveloperDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link DeveloperDto} ID's don't
     * currently exist within the database, then the elements won't be persisted.
     *
     * It should be noted, that this method will remove all existing {@link DeveloperDto} associations and replace them
     * with the new ID's provided.
     *
     * @param id The ID of the {@link GameDto} to associated {@link DeveloperDto}'s with.
     * @param developerIds A {@link Collection} of {@link DeveloperDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link DeveloperDto}'s have been associated with.
     */
    GameDto saveDevelopersForGameId(long id, @NonNull Collection<Long> developerIds);

    /**
     * This method is used to associate a {@link Collection} of {@link DeveloperDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link DeveloperDto} ID's don't
     * currently exist within the database, then the elements won't be persisted
     *
     * It should be noted, that this method will only update the {@link GameDto} with new associations, no existing
     * associations will be removed.
     *
     * @param id The ID of the {@link GameDto} to associated {@link DeveloperDto}'s with.
     * @param developerIds A {@link Collection} of {@link DeveloperDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link DeveloperDto}'s have been associated with.
     */
    GameDto updateDevelopersForGameId(long id, @NonNull Collection<Long> developerIds);

    /**
     * Given the ID of a {@link Publisher}, this method will retrieve a {@link Page} of {@link Game}s that are associated with
     * the given {@link PublisherDto}. If the ID provided does not map to any {@link DeveloperDto}, a {@link javax.persistence.EntityNotFoundException}
     * will be thrown. If there are no {@link Game}s associated with the {@link PublisherDto}, an empty {@link Iterable} will be returned.
     *
     * @param publisherId The ID of the {@link Publisher} to retrieve {@link Game} entries for.
     * @param pageable The amount of data and the page to return.
     *
     * @return A page of {@link GameDto}s that are mapped with the given {@link Publisher} entity.
     */
    Iterable<GameDto> findGamesByPublisherId(long publisherId, Pageable pageable);

    /**
     * Given the ID of a {@link Publisher}, this method will retrieve the total count for how many {@link GameDto}'s have an association
     * to the given {@link Publisher}. If the ID provided does not map to any {@link Publisher}, {@link javax.persistence.EntityNotFoundException}
     * will be thrown.
     *
     * @param publisherId The ID of the {@link Publisher} to retrieve the total count of {@link Game} entries for.
     *
     * @return The total count of {@link GameDto}'s associated with the given {@link Publisher}.
     */
    long countGamesByPublisherId(long publisherId);

    /**
     * This method is used to associate a {@link Collection} of {@link PublisherDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link PublisherDto} ID's don't
     * currently exist within the database, then the elements won't be persisted.
     *
     * It should be noted, that this method will remove all existing {@link PublisherDto} associations and replace them
     * with the new ID's provided.
     *
     * @param id The ID of the {@link GameDto} to associated {@link PublisherDto}'s with.
     * @param publisherIds A {@link Collection} of {@link PublisherDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link PublisherDto}'s have been associated with.
     */
    GameDto savePublishersForGameId(long id, @NonNull Collection<Long> publisherIds);

    /**
     * This method is used to associate a {@link Collection} of {@link PublisherDto} ID's with a given {@link GameDto}
     * that matches the provided ID. If the ID provided doesn't match an existing {@link GameDto} than an
     * {@link javax.persistence.EntityNotFoundException} will be thrown. If any of the {@link PublisherDto} ID's don't
     * currently exist within the database, then the elements won't be persisted
     *
     * It should be noted, that this method will only update the {@link GameDto} with new associations, no existing
     * associations will be removed.
     *
     * @param id The ID of the {@link GameDto} to associated {@link PublisherDto}'s with.
     * @param publisherIds A {@link Collection} of {@link PublisherDto} ID's to associate with the {@link GameDto}.
     *
     * @return The {@link GameDto} that the {@link PublisherDto}'s have been associated with.
     */
    GameDto updatePublishersForGameId(long id, @NonNull Collection<Long> publisherIds);

    /**
     * Given the ID of a {@link Franchise}, this method will retrieve a {@link Page} of {@link Game}s that are associated with
     * the given {@link FranchiseDto}. If the ID provided does not map to any {@link FranchiseDto}, a {@link javax.persistence.EntityNotFoundException}
     * will be thrown. If there are no {@link Game}s associated with the {@link FranchiseDto}, an empty {@link Iterable} will be returned.
     *
     * @param franchiseId The ID of the {@link Franchise} to retrieve {@link Game} entries for.
     * @param pageable The amount of data and the page to return.
     *
     * @return A page of {@link GameDto}s that are mapped with the given {@link Franchise} entity.
     */
    Iterable<GameDto> findGamesByFranchiseId(long franchiseId, Pageable pageable);

    /**
     * Given the ID of a {@link Franchise}, this method will retrieve the total count for how many {@link GameDto}'s have an association
     * to the given {@link Franchise}. If the ID provided does not map to any {@link Franchise}, {@link javax.persistence.EntityNotFoundException}
     * will be thrown.
     *
     * @param franchiseId The ID of the {@link Franchise} to retrieve the total count of {@link Game} entries for.
     *
     * @return The total count of {@link GameDto}'s associated with the given {@link Franchise}.
     */
    long countGamesByFranchiseId(long franchiseId);

    /**
     * Retrieves all of the {@link Game} entities stored within the persistence layer. This method should not be used within a live
     * environment, as the amount of data may cause buffer overflows and bring down the server. It should only be used within a test
     * environment and even then, with hesitancy.
     *
     * @return All of the {@link Game} entities contained within the persistence layer, wrapped as {@link GameDto}s.
     */
    Iterable<GameDto> findAll();

    /**
     * This method will retrieve an {@link Iterable} of {@link GameDto} with a response size specified by the {@link Pageable}. The
     * results can be queried and filtered by utilising the exposed specifications on the {@link GameSpecification} object. If the response
     * from the specifications is that none match, an empty {@link Iterable} will be returned.
     *
     * The {@link GameSpecification} argument can be omitted and is optional, however if the callee provides <code>null</code> for the
     * {@link Pageable}, an exception will be thrown.
     *
     * @param gameSpecification The {@link GameSpecification} to filter the query by.
     * @param pageable The size and page of data to return.
     *
     * @return An {@link Iterable} of relevant queried {@link GameDto} instances.
     */
    Iterable<GameDto> findAll(GameSpecification gameSpecification, Pageable pageable);

    /**
     * Retrieves the total number of rows that match the criteria specified within the {@link GameSpecification}. The specification
     * provided must be a valid instance, if <code>null</code> is provided, a {@link NullPointerException} will be thrown to the callee.
     *
     * @param gameSpecification The {@link GameSpecification} criteria to count the results for.
     *
     * @return The total number of rows that matches the given criteria.
     */
    long count(GameSpecification gameSpecification);

    /**
     * Given a {@link GameDto} instance, the service will attempt to the update the persisted data which matches the given {@link GameDto}
     * in the underlying persistence layer. If the {@link GameDto} supplied contains an ID that doesn't match any existing entities, then
     * the update will fail and a {@link javax.persistence.EntityNotFoundException} will be thrown. If persistence succeeds, the relevant
     * record is updated and the updated entity is returned as a {@link GameDto}.
     *
     * The method does not allow <code>null</code> entities to be inserted, if null is provided, a {@link NullPointerException}
     * will be thrown.
     *
     * @param gameDto The {@link GameDto} instance to update.
     *
     * @return The updated persisted entity as a {@link GameDto}.
     */
    GameDto update(GameDto gameDto);

    /**
     * Given a {@link JsonMergePatch} which will contain JSON information pertaining to a {@link GameDto}, this method will attempt to retrieve
     * the {@link GameDto} that matches the given ID and apply the new JSON on top of it. If the ID provided doesn't match any existing entities,
     * then the patch will fail and a {@link javax.persistence.EntityNotFoundException} will be thrown. If the {@link JsonMergePatch} contains any
     * JSON data not contained on the {@link GameDto}, it'll be ignored. If the patching succeeds, the patched record will be updated and the updated
     * entity returned as a {@link GameDto}.
     *
     * The method does not allow a null {@link JsonMergePatch} to be provided, if null is provided, a {@link NullPointerException} will be thrown.
     *
     * @param id The ID of the {@link GameDto} to patch.
     * @param jsonMergePatch The {@link JsonMergePatch} containing the JSON data to patch.
     *
     * @return The patched persisted entity as a {@link GameDto}.
     */
    GameDto patch(long id, JsonMergePatch jsonMergePatch);

    /**
     * Deletes the persisted entity that is mapped to the given ID. If the service cannot find a {@link GameDto} that is mapped to the ID,
     * then deletion will not occur and a {@link javax.persistence.EntityNotFoundException} exception will be thrown. Deletion can be
     * classes as successful if the method completes without throwing additional errors.
     *
     * @param id The ID of the {@link GameDto} to delete.
     */
    void deleteById(long id);
}
