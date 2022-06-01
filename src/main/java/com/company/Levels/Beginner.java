package com.company.Levels;

import com.company.HintQuestionnaire;
import com.company.IQuestionnaire;
import com.company.Question;
import com.company.Questionnaire;

import java.util.List;

public class Beginner extends LevelFactory{
    @Override
    public IQuestionnaire wrapQuestionnaire(int id, String questionnaireName, List<Question> questions) {
        return new HintQuestionnaire(new Questionnaire(id, questionnaireName, questions));
    }
}
