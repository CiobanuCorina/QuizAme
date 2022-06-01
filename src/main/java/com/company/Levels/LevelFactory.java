package com.company.Levels;

import com.company.IQuestionnaire;
import com.company.Question;

import java.util.List;

public abstract class LevelFactory {
    public abstract IQuestionnaire wrapQuestionnaire(int id, String questionnaireName, List<Question> questions);
}
