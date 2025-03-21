public class Task {
    private String title;
    private boolean isDone;

    public Task(String title){
        this.title = title;
        this.isDone = false;
    }

    public void markTaskDone(){
        this.isDone = true;
    }

    public boolean taskProgress(){
        return this.isDone;
    }

    public String getTask(){
        return this.title;
    }

    @Override
    public String toString(){
        return   title + " " + (isDone ? "[✔]" : "[ ]");
    }
}
