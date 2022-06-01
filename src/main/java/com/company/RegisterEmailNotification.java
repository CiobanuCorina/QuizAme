package com.company;

import com.company.User.User;

public class RegisterEmailNotification implements Subscriber{
    @Override
    public void update(User user) {
        String content = user.getUsername() + ", welcome to QuizAme. Continue to explore our quizzes and test your knowledge in a wide variety of fields";
        EmailSender emailSender = new EmailSender(user.getEmail(), "Welcome to QuizAme", content);
        emailSender.sendEmail();
    }
}
