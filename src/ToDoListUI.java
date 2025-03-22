import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import org.jdatepicker.impl.*;

public class ToDoListUI {
    public static void main(String[] args){

        //Enable to set Background to the frame.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("To-Do List 📝"); // title for the frame
        frame.setSize(600,550); //Window size 350x350
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final LocalDate[] selectedDueDate = {null}; // Variable to store user selecetd Date for task.

        //Buttons & TextFields

        JTextField inputField = new JTextField(20); // field where user can name the task

        JButton addBtn = new JButton("Add Task"); //Button so user can add task

        JButton doneBtn = new JButton("Done"); //Button so user cam mark task as Done.

        JButton delBtn = new JButton("Delete"); // Button so user can delete a task

        JButton saveBtn = new JButton("Save"); // Button to Save list

        JButton loadBtn = new JButton("Load"); //Button to load saved list

        JButton dateBtn = new JButton("Pick Date 📅");

        DefaultListModel<Task> taskListModel = new DefaultListModel<>();
        JList<Task> taskList = new JList<>(taskListModel);

        //Render for enable Done and Yet Done icons near each Task the user enter
        taskList.setCellRenderer(new DefaultListCellRenderer(){
            private final ImageIcon doneTaskIcon = new ImageIcon(new ImageIcon("icons/doneTask.png").getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH));
            private final ImageIcon yetDoneTaskIcon = new ImageIcon(new ImageIcon("icons/yetDoneTask.png").getImage().getScaledInstance(16,16,Image.SCALE_SMOOTH));

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value , int index , boolean isSelected,
                                                          boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
                label.setFont(new Font("Ariel",Font.PLAIN,14));

                if(value instanceof Task task){
                    label.setText(task.toString());
                    label.setIcon(task.isDone() ? doneTaskIcon : yetDoneTaskIcon);
                    label.setBackground(task.isDone() ? new Color(220,255,220) : Color.white);
                }
                if (isSelected){
                    label.setBackground(new Color(33,150,243));
                    label.setForeground(Color.white);
                }else{
                    label.setBackground(Color.white);
                    label.setForeground(Color.black);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList); //Scroll pane so if there is lot of tasks the user can scroll to see it

        ToDoList toDoList = new ToDoList(); // Create new ToDoList instence using its class constructor


        // Buttons & TextField Actions

        //Add Btn Action
        addBtn.addActionListener(e -> {
            String title = inputField.getText().trim();
            if ((!title.isEmpty())&& (selectedDueDate[0] != null)){
                Task task = new Task(title,selectedDueDate[0]);
                toDoList.addTask(task);
                taskListModel.addElement(task);
                inputField.setText("");
                selectedDueDate[0] = null;
                dateBtn.setText("Pick Date 📅");
            }else{
                JOptionPane.showConfirmDialog(frame,"Please enter a task name and pick a date");
            }
        });

        //Done Btn Action
        doneBtn.addActionListener(e -> {
            int index = taskList.getSelectedIndex(); //get the task index
            if (index != -1){
                Task task = taskListModel.getElementAt(index);
                task.markTaskDone();
                taskList.repaint();
            }
        });

        //Delete Btn Action
        delBtn.addActionListener(e ->{
            int index = taskList.getSelectedIndex();
            if (index != -1){
                toDoList.removeTask(index);
                taskListModel.remove(index);
            }
        });

        //Save Btn Action
        saveBtn.addActionListener(e -> {
                toDoList.saveToFile("tasks.json");
                JOptionPane.showMessageDialog(frame, "Saved successfully!");
        });

        //Load Btn Action
        loadBtn.addActionListener(e -> {
                toDoList.loadFromFile("tasks.json");
                taskListModel.clear();
                for (Task task : toDoList.getTasks()) {
                    taskListModel.addElement(task);}
                JOptionPane.showMessageDialog(frame, "Loaded successfully!");
        });

        //Date Btn Action
        dateBtn.addActionListener(e -> {
            UtilDateModel model = new UtilDateModel();

            // Text for the date picker UI
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");

            // Set today's date as default
            Date today = new Date();
            model.setValue(today);
            model.setSelected(true);

            // Create the date picker panel and component
            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            // Prevent selecting past dates
            datePanel.addActionListener(e1 -> {
                Date selected = (Date) datePicker.getModel().getValue();
                if (selected != null && selected.before(today)) {
                    JOptionPane.showMessageDialog(null, "❌ Cannot pick a past date!", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    model.setValue(today);// Reset to today
                }
            });

            // Open the date picker dialog
            int result = JOptionPane.showConfirmDialog(
                    null, datePicker, "Select Due Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            // Save selected date if valid
            if (result == JOptionPane.OK_OPTION) {
                Date selectedDate = (Date) datePicker.getModel().getValue();
                if (selectedDate != null && !selectedDate.before(today)) {
                    LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    selectedDueDate[0] = localDate;
                    dateBtn.setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    JOptionPane.showMessageDialog(null, "⚠ Please pick a valid future date.");
                }
            }
        });


        JPanel inputPanel = new JPanel(); //For the input buttons(add,date,task title)
        JPanel buttonsPanel = new JPanel(); //For the buttons


        inputPanel.add(addBtn);
        inputPanel.add(inputField);
        inputPanel.add(dateBtn);


        buttonsPanel.add(doneBtn);
        buttonsPanel.add(delBtn);
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(loadBtn);


        //Icons For Buttons

        ImageIcon Icon1 = new ImageIcon("icons/load.png");
        Image image = Icon1.getImage();
        Image load = image.getScaledInstance(19,19,Image.SCALE_SMOOTH);

        ImageIcon Icon2 = new ImageIcon("icons/save.png");
        Image image1 = Icon2.getImage();
        Image save = image1.getScaledInstance(19,19,Image.SCALE_SMOOTH);

        ImageIcon Icon3 = new ImageIcon("icons/done.png");
        Image image2 = Icon3.getImage();
        Image done = image2.getScaledInstance(19,19,Image.SCALE_SMOOTH);

        ImageIcon Icon4 = new ImageIcon("icons/add.png");
        Image image3 = Icon4.getImage();
        Image add = image3.getScaledInstance(15,15,Image.SCALE_SMOOTH);

        ImageIcon Icon5 = new ImageIcon("icons/delete.png");
        Image image4 = Icon5.getImage();
        Image delete = image4.getScaledInstance(19,19,Image.SCALE_SMOOTH);



        //Buttons , List , TextField & Frame Styling
        frame.getContentPane().setBackground(new Color(136,171,180)); //Set App Background
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        inputPanel.setBackground(new Color(136,171,180));
        buttonsPanel.setBackground(new Color(136,171,180));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        taskList.setBackground(new Color(255,255,255)); //white Background for the list


        //Add Btn Styling
        addBtn.setBackground(new Color(0,200,83));
        addBtn.setForeground(Color.white);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);
        addBtn.setFont(new Font("SansSerif",Font.BOLD,12));
        addBtn.setIcon(new ImageIcon(add));
        addBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        addBtn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                addBtn.setBackground(new Color(0,164,68));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                addBtn.setBackground(new Color(0,200,83));
            }
        });

        //Done Btn Styling
        doneBtn.setBackground(new Color(30,123,230));
        doneBtn.setForeground(Color.white);
        doneBtn.setFocusPainted(false);
        doneBtn.setOpaque(true);
        doneBtn.setBorderPainted(false);
        doneBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        doneBtn.setIcon(new ImageIcon(done));
        doneBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        doneBtn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                doneBtn.setBackground(new Color(16,76,146));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                doneBtn.setBackground(new Color(30,123,230));
            }
        });

        //Delete Btn Styling
        delBtn.setBackground(new Color(211,47,47));
        delBtn.setForeground(Color.white);
        delBtn.setFocusPainted(false);
        delBtn.setOpaque(true);
        delBtn.setBorderPainted(false);
        delBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        delBtn.setIcon(new ImageIcon(delete));
        delBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        delBtn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                delBtn.setBackground(new Color(193,39,39));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                delBtn.setBackground(new Color(211,47,47));
            }
        });

        //Save Btn Styling
        saveBtn.setBackground(new Color(123,31,162));
        saveBtn.setForeground(Color.white);
        saveBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        saveBtn.setFocusPainted(false);
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);
        saveBtn.setIcon(new ImageIcon(save));
        saveBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                saveBtn.setBackground(new Color(103,33,151));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                saveBtn.setBackground(new Color(123,31,162));
            }
        });



        //Load Btn Styling
        loadBtn.setBackground(new Color(255,150,0));
        loadBtn.setForeground(Color.white);
        loadBtn.setFocusPainted(true);
        loadBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        loadBtn.setFocusPainted(false);
        loadBtn.setOpaque(true);
        loadBtn.setBorderPainted(false);
        loadBtn.setIcon(new ImageIcon(load));
        loadBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        loadBtn.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                loadBtn.setBackground(new Color(223,131,0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                loadBtn.setBackground(new Color(223,150,0));
            }
        });

        //Date Btn Styling
        dateBtn.setBackground(new Color(255,255,255));
        dateBtn.setFocusPainted(false);
        dateBtn.setFont(new Font("Ariel",Font.PLAIN,12));




        //Set Fonts size and family for the Texts of the Buttons
        inputField.setFont(new Font("SansSertif",Font.PLAIN,14));
        taskList.setFont(new Font("Ariel",Font.PLAIN,16));


        //Add the Buttons & Input Buttons into the frame.
        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonsPanel,BorderLayout.SOUTH);
        frame.setVisible(true);

    }
}
