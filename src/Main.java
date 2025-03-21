import java.util.Scanner;
public class Main {
    static  void welcomeNote(){
        System.out.println("\n========= To-D0 List =========");
        System.out.println("\n1.Add Task");
        System.out.println("\n2.Show Tasks");
        System.out.println("\n3.Mark task as done");
        System.out.println("\n4.Remove Task");
        System.out.println("\n5.Exit");
    }

    public static void main(String[] args) {
        ToDoList todo = new ToDoList();
        Scanner scanner = new Scanner(System.in);
        boolean runing = true;

        while(runing){
            welcomeNote();
            int choice = -1;
            try{
                choice = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){
                System.out.println("You must enter a number from 1 - 5");
                scanner.nextLine();
                continue;
            }
            if (choice > 5){
                System.out.println("You must enter a number from 1 - 5");
                continue;
            }
            switch (choice){
                case 1 -> {
                    System.out.println("Enter task: ");
                    String title = scanner.nextLine();
                    todo.addTask(title);
                    break;
                }

                case 2 -> {
                    todo.showTasks();
                    break;
                }

                case 3 ->{
                    todo.showTasks();
                    System.out.println("Choose the number of task u want to mark as done: ");
                    try{
                        int index = scanner.nextInt();
                        todo.markDone(index);
                    }
                    catch (Exception e) {
                        System.out.println("Choose from the tasks number");
                        scanner.nextLine();
                    }
                }
                case 4 ->{
                    todo.showTasks();
                    System.out.println("Choose the task u want to remove: ");
                    try{
                        int index = scanner.nextInt();
                        scanner.nextLine();
                        todo.removeTask(index);
                    }catch (Exception e){
                        System.out.println("Invalid input. choose a number the list");
                    }
                }
                case 5 -> {
                    System.out.println("Thanks You for Using our To-Do List App");
                    runing = false;

                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }
}
