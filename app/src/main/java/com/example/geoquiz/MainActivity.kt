package com.example.geoquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoquiz.ui.theme.GeoQuizTheme

data class Question(val text: String, val answer: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    val questions = listOf(
        Question("Canberra is the capital of Australia.", true),
        Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
        Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
        Question("The source of the Nile River is in Egypt.", false),
        Question("The Amazon River is the longest river in the Americas.", true),
        Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    // Новое состояние для показа окна с результатами
    var showResultDialog by remember { mutableStateOf(false) }

    val checkAnswer = { userAnswer: Boolean ->
        if (!isAnswered) {
            isAnswered = true
            if (userAnswer == questions[currentIndex].answer) {
                score++
            }
            // Если это был последний вопрос — показываем диалог
            if (currentIndex == questions.size - 1) {
                showResultDialog = true
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF6200EE))
                .padding(16.dp)
        ) {
            Text("GeoQuiz", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = questions[currentIndex].text, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(32.dp))

            if (!isAnswered) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { checkAnswer(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("TRUE", color = Color.White)
                    }
                    Button(
                        onClick = { checkAnswer(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("FALSE", color = Color.White)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (currentIndex < questions.size - 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (isAnswered) {
                                currentIndex++
                                isAnswered = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("NEXT >", color = Color.White)
                    }
                }
            }
        }
    }

    // Всплывающее окно с результатом
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { /* Запрещаем закрывать окно кликом мимо */ },
            title = { Text("Тест завершен!") },
            text = { Text("Ваш результат: $score из ${questions.size}") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Сбрасываем игру заново
                        showResultDialog = false
                        currentIndex = 0
                        isAnswered = false
                        score = 0
                    }
                ) {
                    Text("Начать заново")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    GeoQuizTheme {
        QuizScreen()
    }
}