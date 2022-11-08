package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.*;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameMovesException;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayableGameImplTest {

    PlayableGameImpl playbaleGame = new PlayableGameImpl();
    UUID gameId = UUID.randomUUID();

    GameContextHolder CONTEXT;

    @BeforeEach
    void createGameData(TestInfo testInfo){
        CONTEXT = GameContextHolder.getInstance();
        if(testInfo.getDisplayName().equals("testPlayGame_only_game_in_context_can_be_played()")){
            return;
        }
        simulateStartAndJoinGame(gameId);
    }

    private GamePlay createGamePlayFor(UUID gameId, TicTacToe type, int row, int col) {
        GamePlay gamePlay = new GamePlay();
        gamePlay.setGameId(gameId);
        gamePlay.setType(type);
        gamePlay.setColumnNumber(row);
        gamePlay.setRowNumber(col);
        return gamePlay;
    }


    private Game simulateStartAndJoinGame(UUID existingGameID) {
        Game simulatedExistingGame = new Game();
        simulatedExistingGame.setGameId(existingGameID);
        simulatedExistingGame.setPlayer1(new Player("player-1"));
        simulatedExistingGame.setPlayer2(new Player("player-2"));
        simulatedExistingGame.setStatus(GameStatus.IN_PROGRESS);
        CONTEXT.setGame(simulatedExistingGame);
        return simulatedExistingGame;
    }

    @Test
    void testPlayGame_only_game_in_context_can_be_played() {

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.X, 1 , 1);

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " not found", exception.getMessage());
    }

    @Test
    void testPlayGame_only_game_with_status_in_progress_can_be_played() throws GameNotFoundException, GameStatusException, GameMovesException {

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.X, 1 , 1);

        Game game = playbaleGame.playGame(gamePlay);
        game.setStatus(GameStatus.FINISHED);

        GameStatusException exception = assertThrows(GameStatusException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("Game " + gameId + " is not started yet or already finished", exception.getMessage());
    }

    @Test
    void testPlayGame_first_moves_should_be_X(){

        GamePlay gamePlay = createGamePlayFor(gameId, TicTacToe.O, 1, 1);

        GameMovesException exception = assertThrows(GameMovesException.class, () -> {
            playbaleGame.playGame(gamePlay);
        });

        assertEquals("First moves should be X", exception.getMessage());
    }

    @Test
    void testPlayGame_played_position_should_be_saved() throws GameMovesException, GameNotFoundException, GameStatusException {
        simulateStartAndJoinGame(gameId);

        GamePlay gamePlay1 = createGamePlayFor(gameId, TicTacToe.X, 1, 2);
        Game game1 = playbaleGame.playGame(gamePlay1);

        assertEquals(1, game1.getBoard()[0][1]);

        GamePlay gamePlay2 = createGamePlayFor(gameId, TicTacToe.X, 2, 2);
        Game game2 = playbaleGame.playGame(gamePlay2);

        assertEquals(1, game1.getBoard()[1][1]);

    }


}