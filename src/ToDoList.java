import java.time.LocalDate;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import  com.google.gson.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.lang.reflect.Type;

public class ToDoList {
    private ArrayList<Task> tasks;

    public ToDoList(){
        tasks = new ArrayList<>();
    }

    public void addTask(Task task){
        tasks.add(task);
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
       if (index >= 0 && index < tasks.size()){
           tasks.remove(index);
       }
    }

   public void saveToFile(String filename){
       Gson gson = new GsonBuilder()
               .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                       new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
               .setPrettyPrinting()
               .create();
       try (FileWriter writer = new FileWriter(filename)) {
           gson.toJson(tasks, writer);
       } catch (IOException e) {
           System.out.println("❌ Failed to save: " + e.getMessage());
       }
   }

    public void loadFromFile(String filename) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .create();
        try (FileReader reader = new FileReader(filename)) {
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("❌ Failed to load: " + e.getMessage());
        }
    }

    public ArrayList <Task> getTasks(){
        return tasks;
    }


}
