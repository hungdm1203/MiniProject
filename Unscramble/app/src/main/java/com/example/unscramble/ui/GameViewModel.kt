package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val _uiState= MutableStateFlow(GameUiState())


private lateinit var currentWord:String
private var useWords:MutableSet<String> = mutableSetOf()


class GameViewModel: ViewModel() {
    val uiState:StateFlow<GameUiState> = _uiState.asStateFlow()
    var userGuess by mutableStateOf("")
        private set

    fun checkUserGuess(){
        if(userGuess.equals(currentWord,ignoreCase = true)){
            val updatedScore= _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }else{
            _uiState.update { currentState->currentState.copy(isGuessedWordWrong = true) }
        }
        updateUserGuess("")
    }

    fun updateUserGuess(guessWord: String){
        userGuess=guessWord
    }

    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    fun resetGame(){
        useWords.clear()
        _uiState.value= GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    init {
        resetGame()
    }
}

private fun pickRandomWordAndShuffle():String{
    currentWord= allWords.random()
    while(useWords.contains(currentWord)){
        currentWord= allWords.random()
    }
    useWords.add(currentWord)
    return shuffleCurrentWord(currentWord)
}

private fun shuffleCurrentWord(word: String): String{
    var tmp=word.toCharArray()
    while (String(tmp).equals(word)){
        tmp.shuffle()       //xao tron mang
    }
    return String(tmp)
}


private fun updateGameState(updatedScore: Int) {
    if(useWords.size== MAX_NO_OF_WORDS){
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                score = updatedScore,
                isGameOver = true
            )
        }
    }else{
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentWordCount = currentState.currentWordCount.inc(),
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updatedScore
            )
        }
    }
}