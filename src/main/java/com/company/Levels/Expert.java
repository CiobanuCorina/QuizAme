package com.company.Levels;

import com.company.IQuestionnaire;
import com.company.Question;
import com.company.Questionnaire;
import com.company.TimedQuestionnaire;

import java.util.List;

public class Expert extends LevelFactory{
    @Override
    public IQuestionnaire wrapQuestionnaire(int id, String questionnaireName, List<Question> questions) {
        return new TimedQuestionnaire(new Questionnaire(id, questionnaireName, questions));
    }
}
