package Controller;

import Model.User;
import View.AdministratorView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class AdministratorController {
    private AdministratorView administratorView;
    private List<User> users;
    private List<String> postmans;
    private List<String> coordinators;
    private User.Type currentShowingType;
    private String selectedUserName;
    private MainController mainController;

    public AdministratorController(MainController mainController){
        this.mainController = mainController;
        administratorView = new AdministratorView(this);
        classifyUsers();
        administratorView.setVisible(true);
    }

    public void classifyUsers(){
        this.users = ServerCommunication.getUsers();
        postmans = new LinkedList<>();
        coordinators = new LinkedList<>();
        for(User user : users){
            User.Type type = user.getType();
            if(type == User.Type.POSTMAN){
                postmans.add(user.getUsername());
            }
            if(type == User.Type.COORDINATOR){
                coordinators.add(user.getUsername());
            }
        }
    }

    private JTable createTable(String[][] tableData, String[] tableCol) {
        DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
        final JTable mainTable = new JTable();
        mainTable.setModel(model);
        mainTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Object username = mainTable.getValueAt(mainTable.getSelectedRow(), 0);
                    if (username.equals(" "))
                        return;
                    updateSecondaryTable((String)username);
                    selectedUserName = (String)username;
                }
            }
        });
        return mainTable;
    }

    public ActionListener viewPostmansButtonActionListener() {
        return actionEvent ->{
            classifyUsers();
            String[][] tableData = new String[postmans.size() + 10][1];
            int size = postmans.size();
            for (int i = 0; i < size; i++) {
                tableData[i][0] = postmans.get(i);
            }

            JTable table = createTable(tableData, new String[]{"users"});
            JScrollPane pane = administratorView.getDataTablePane();
            pane.setViewportView(table);
            pane.revalidate();
            currentShowingType = User.Type.POSTMAN;
            table.setVisible(true);
        };
    }
    public ActionListener viewCoordinatorsButtonActionListener(){
        return actionEvent -> {
            String[][] tableData = new String[coordinators.size()+10][1];
            int size = coordinators.size();
            for(int i=0;i<size;i++){
                tableData[i][0] = coordinators.get(i);
            }
            JTable dataTable = createTable(tableData, new String[] {"users"});
            JScrollPane pane = administratorView.getDataTablePane();
            pane.setViewportView(dataTable);
            pane.revalidate();
            currentShowingType = User.Type.COORDINATOR;
            dataTable.setVisible(true);
        };
    }

    public ActionListener updateEntryButtonActionListener(){
    return actionEvent -> {
        String oldUsername = selectedUserName;
        TableModel model = administratorView.getUpdateTable().getModel();
        String name = (String)model.getValueAt(0,1);
        String username = (String)model.getValueAt(1,1);
        String password = (String)model.getValueAt(2,1);
        User user = new User(username, name, password, currentShowingType);
        if(administratorView.getUpdateEntryButtonText().contains("Add")){
            ServerCommunication.addUser(user);
        }
        else {
            User oldUser = ServerCommunication.getUserByUsername(oldUsername);
            ServerCommunication.updateUser(oldUser, user);
        }
        if(currentShowingType == User.Type.POSTMAN){
            for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
            }
        }else if(currentShowingType == User.Type.COORDINATOR){
            for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
            }
        }
    };
    }

    public ActionListener addEntryButtonActionListener(){
        return actionEvent -> {
            String[][] tableData = {
                    {"name", ""},
                    {"username", ""},
                    {"password", ""}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            administratorView.setUpdateTable(mainTable);
            JScrollPane pane = administratorView.getUpdateTablePane();
            pane.setViewportView(mainTable);
            pane.revalidate();
            administratorView.getUpdateEntryButton().setVisible(true);
            administratorView.setUpdateEntryButtonText("Add");
        };
    }

    public void updateSecondaryTable(String name){
        User user = ServerCommunication.getUserByUsername(name);
        if(user!=null){
            String[][] tableData = {
                    {"name", user.getName()},
                    {"username", user.getUsername()},
                    {"password", user.getPassword()}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            JTable updateTable = administratorView.getUpdateTable();
            updateTable.setModel(model);
            administratorView.setUpdateTable(updateTable);
            JScrollPane tablePane = administratorView.getUpdateTablePane();
            tablePane.setViewportView(updateTable);
            tablePane.revalidate();
            administratorView.getUpdateEntryButton().setVisible(true);
            administratorView.setUpdateEntryButtonText("Update");
        }
    }

    public ActionListener deleteButtonActionListener(){
        return actionEvent -> {
            User user = ServerCommunication.getUserByUsername(selectedUserName);
            if(user != null) {
                ServerCommunication.deleteUser(user);
            }
            if(currentShowingType == User.Type.POSTMAN){
                for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
                }
            }else if(currentShowingType == User.Type.COORDINATOR){
                for(ActionListener a: administratorView.getViewPostmansButton().getActionListeners()){
                    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
                }
            }
        };
    }

    public WindowListener windowListener(){
        return new WindowListener(){

            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                ServerCommunication.exit();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        };

    }
}
