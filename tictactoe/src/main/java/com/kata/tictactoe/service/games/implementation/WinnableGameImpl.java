package com.kata.tictactoe.service.games.implementation;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.GameStatus;
import com.kata.tictactoe.models.Winner;
import com.kata.tictactoe.service.games.WinnableGame;
import com.kata.tictactoe.service.games.context.GameContextHolder;
import com.kata.tictactoe.service.games.exception.GameNotFoundException;
import com.kata.tictactoe.service.games.exception.GameStatusException;

import java.util.UUID;

public class WinnableGameImpl implements WinnableGame {

    private static final GameContextHolder CONTEXT = GameContextHolder.getInstance();
    @Override
    public Winner winner(UUID gameId) throws GameNotFoundException, GameStatusException {
        Game game = CONTEXT.getGames().get(gameId);
        if(game == null) {
            throw new GameNotFoundException(
                    String.format("Game %s not found", gameId)
            );
        }
        if(!GameStatus.FINISHED.equals(game.getStatus())){
            throw new GameStatusException(
                    String.format("Game %s is not finished yet", gameId)
            );
        }
        return null;
    }
}
