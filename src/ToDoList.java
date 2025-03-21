import java.util.ArrayList;

public class ToDoList {
    private ArrayList<Task> tasks;

    public ToDoList(){
        tasks = new ArrayList<>();
    }

    public void addTask(String title){
        tasks.add(new Task(title));
    }

    public void markDone(int index){
            if(index >= 0 && index < tasks.size()) {
                index -= 1;
                tasks.get(index).markTaskDone();
            }
    }

    public void showTasks(){
        for (int i = 0 ; i < tasks.size() ; i++){
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public int size(){
        return tasks.size();
    }

    public void removeTask(int index){
        index = index - 1; //Beacuse the user will choose number from 1 - n
        tasks.remove(index);
    }




}
