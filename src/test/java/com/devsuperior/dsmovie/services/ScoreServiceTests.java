package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ScoreServiceTests {

    @InjectMocks
    private ScoreService service;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScoreRepository scoreRepository;

    private ScoreDTO scoreDTO;
    private MovieEntity movie;
    private ScoreEntity score;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scoreDTO = ScoreFactory.createScoreDTO();
        movie = MovieFactory.createMovieEntity();
        user = UserFactory.createUserEntity();
        score = ScoreFactory.createScoreEntity();

		Mockito.when(userService.authenticated()).thenReturn(user);
    }

    @Test
    public void saveScoreShouldReturnMovieDTO() {

        movie.getScores().add(score);
		
        Mockito.when(movieRepository.findById(scoreDTO.getMovieId())).thenReturn(Optional.of(movie));
        Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);
        Mockito.when(movieRepository.save(any())).thenReturn(movie);

        MovieDTO result = service.saveScore(scoreDTO);

        assertNotNull(result);
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getId(), result.getId());
    }

    @Test
    public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

        Mockito.when(movieRepository.findById(scoreDTO.getMovieId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.saveScore(scoreDTO);
        });
    }
}
