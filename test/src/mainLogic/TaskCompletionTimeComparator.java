package mainLogic;

import java.util.Calendar;
import java.util.Comparator;

import entity.TaskEntity;

public class TaskCompletionTimeComparator implements Comparator<TaskEntity> {
    private final String ERROR_DATE_NULL = "Error at TaskCompletionTime: Completion date of one date is null";
    
    public int compare(TaskEntity task1, TaskEntity task2) {
        if(task1.getCompletionDate() == null || task2.getCompletionDate() == null) {
            System.out.println(ERROR_DATE_NULL);
            return 0;
        }
        return task1.getCompletionDate().compareTo(task2.getCompletionDate());
    }
}