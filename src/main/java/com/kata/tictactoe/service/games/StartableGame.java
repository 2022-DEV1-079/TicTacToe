package com.kata.tictactoe.service.games;

import com.kata.tictactoe.models.Game;
import com.kata.tictactoe.models.Player;

public interface StartableGame {

    Game startGame(Player player1);
}
