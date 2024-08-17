package com.example.unscramble.ui

data class GameUiState(
    var currentScrambledWord: String = "",
    var isGuessedWordWrong: Boolean = false,
    var currentWordCount:Int=1,
    var isGameOver:Boolean= false,
    var score:Int = 0
)
