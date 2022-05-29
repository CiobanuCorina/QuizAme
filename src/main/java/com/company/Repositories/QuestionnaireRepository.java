package com.company.Repositories;

import com.company.IQuestionnaire;

import java.sql.Connection;

public class QuestionnaireRepository {
    Connection connection = DBConnection.getDbConnection().getConnection();
    IQuestionnaire questionnaire;

    public QuestionnaireRepository(IQuestionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public QuestionnaireRepository(){}
}
