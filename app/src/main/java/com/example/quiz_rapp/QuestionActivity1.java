package com.example.quiz_rapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuestionActivity1 extends AppCompatActivity {
    int flag = 0;
    int marks = 0;
    public static int correct = 0;
    int wrong = 0;

    String[] questions = {"1. What is the size of an int variable in Java?",
            "2. Which of the following is not a Java keyword?",
            "3. What will be the output of this code? int x = 5;\n" +
                    "System.out.println(x++ + ++x);",
            "4. Which of the following is the correct way to create an object in Java?",
            "5. Which interface must be implemented by a class to support multithreading?"};
    String[] options =
            {"8 bits", "16 bits", "32 bits", "64 bits",
                    "static", "Boolean", "void", "try",
                    "11", "12", "10", "13", "9",
                    "MyClass obj = MyClass();", " MyClass obj = new MyClass;", "MyClass obj = new MyClass();", "obj = new MyClass();",
                    " Runnable", "Threadable", " Serializable", "Multithreadable"};
    String[] answers =
            {"32 bits",
                    "Boolean",
                    "11",
                    "MyClass obj = new MyClass();",
                    " Runnable"};

    TextView quitBtn, dispNo, score, question;
    Button next;
    RadioGroup radio_g;
    RadioButton rb1, rb2, rb3, rb4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quitBtn = findViewById(R.id.quitBtn);
        question = findViewById(R.id.question);
        score = findViewById(R.id.score);
        dispNo = findViewById(R.id.dispNo);
        next = findViewById(R.id.nextBtn);
        radio_g = findViewById(R.id.answerGroup);
        rb1 = findViewById(R.id.radioBtn1);
        rb2 = findViewById(R.id.radioBtn2);
        rb3 = findViewById(R.id.radioBtn3);
        rb4 = findViewById(R.id.radioBtn4);

        question.setText(questions[flag]);
        rb1.setText(options[0]);
        rb2.setText(options[1]);
        rb3.setText(options[2]);
        rb4.setText(options[3]);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_g.getCheckedRadioButtonId() == -1){
                    Toast.makeText(QuestionActivity1.this, "please select an option", Toast.LENGTH_LONG).show();
                    return;
                }
                RadioButton uAnswer = findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uAnswer.getText().toString();

                if(ansText.equals(answers[flag])){
                    correct++;
                    Toast.makeText(QuestionActivity1.this, "Hurray!! it was correct", Toast.LENGTH_LONG).show();

                }else {
                    wrong++;
                    Toast.makeText(QuestionActivity1.this, "Ohh!! it was incorrect", Toast.LENGTH_LONG).show();
                }
                flag++;
                if(score!= null){
                    score.setText(correct);
                    if(flag<questions.length){
                        question.setText(questions[flag]);
                        rb1.setText(options[flag*4]);
                        rb2.setText(options[flag*4 + 1]);
                        rb3.setText(options[flag*4 + 2]);
                        rb4.setText(options[flag*4 + 3]);

                        dispNo.setText((flag + 1) + "/" + questions.length);
                    }else {
                        marks = correct;
                        Intent intent = new Intent(QuestionActivity1.this, ResultActivity.class);
                        intent.putExtra("attempted", flag);
                        intent.putExtra("correct", correct);
                        intent.putExtra("wrong", wrong);
                        startActivity(intent);
                        finish();
                    }
                    radio_g.clearCheck();
                }

            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(QuestionActivity1.this, ResultActivity.class));
                intent.putExtra("attempted", flag);
                intent.putExtra("correct", correct);
                intent.putExtra("wrong", wrong);
                startActivity(intent);
                finish();
            }
        });


    }
}