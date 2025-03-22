import java.time.LocalDate;

public class Task {
    private String title;
    private boolean isDone;
    private LocalDate dueDate;

    public Task(String title , LocalDate dueDate){
        this.title = title;
        this.isDone = false;
        this.dueDate = dueDate;
    }

    public void markTaskDone(){
        isDone = true;
    }

    public boolean isDone(){
        return isDone;
    }

    public String getTask(){
        return this.title;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    @Override
    public String toString(){
        return   title + " " +  " (" + dueDate + ")";
    }
}

