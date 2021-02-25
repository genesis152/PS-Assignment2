package Controller;

import Model.Parcel;
import Model.User;
import View.CoordinatorView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoordinatorViewController{
    protected User currentUser;
    private CoordinatorView coordinatorView;
    private List<Parcel> parcels;
    private Integer selectedParcelID;
    protected MainController mainController;

    public CoordinatorViewController(MainController mainController, User user){
        this.currentUser = user;
        this.coordinatorView = new CoordinatorView(this);
        this.coordinatorView.setVisible(true);
        this.mainController = mainController;
        populatePostmanTable();
    }

    private void setCurrentUser(User user){
        this.currentUser = user;
    }

    private JTable createTable(String[][] tableData, String[] tableCol) {
        DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
        final JTable mainTable = new JTable();
        mainTable.setModel(model);
        mainTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Object parcelId = mainTable.getValueAt(mainTable.getSelectedRow(), 0);
                    if (parcelId.equals(" "))
                        return;
                    selectedParcelID = Integer.parseInt((String)parcelId);
                    updateSecondaryTable(selectedParcelID);
                }
            }
        });
        return mainTable;
    }

    public void updateSecondaryTable(Integer parcelId){
        Parcel parcel = ServerCommunication.getParcelByID(parcelId);
        if(parcel!=null){
            String[][] tableData = {
                    {"Address", parcel.getAddress()},
                    {"Coordinates", Parcel.pointToString(parcel.getCoordinates())},
                    {"Assigned postman's ID", Integer.toString(parcel.getAssignedPostmanID())}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            JTable updateTable = coordinatorView.getUpdateTable();
            updateTable.setModel(model);
            coordinatorView.setUpdateTable(updateTable);
            JScrollPane tablePane = coordinatorView.getUpdateTablePane();
            tablePane.setViewportView(updateTable);
            tablePane.revalidate();
            coordinatorView.getUpdateParcelButton().setVisible(true);
            coordinatorView.setUpdateParcelButtonText("Update");
        }
    }

    public User getCurrentUser(){
        return this.currentUser;
    }

    public ActionListener viewParcelsButtonActionListener() {
        return actionEvent ->{
            parcels = ServerCommunication.getParcels();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            int size = parcels.size();
            Object[] tempArray = parcels.toArray();
            String[][] tableData = new String[size+10][6];
            for (int i = 0; i < size; i++) {
                tableData[i][0] = Integer.toString(((Parcel)tempArray[i]).getID());
                tableData[i][1] = ((Parcel)tempArray[i]).getAddress();
                tableData[i][2] = Parcel.pointToString(((Parcel)tempArray[i]).getCoordinates());
                tableData[i][3] = formatter.format(((Parcel)tempArray[i]).getDate());
                tableData[i][4] = ServerCommunication.getUserByID(((Parcel)tempArray[i]).getAssignedPostmanID()).getName();
                tableData[i][5] = Integer.toString(((Parcel)tempArray[i]).getAssignedPostmanID());
            }
            String[] tableCols = {
                    "ID", "Address", "Coordinates", "Date and Time", "Assigned Postman", "Assigned Postman ID"
            };
            JTable table = createTable(tableData,tableCols);
            coordinatorView.setDataTable(table);
            JScrollPane pane = coordinatorView.getDataTablePane();
            pane.setViewportView(table);
            pane.revalidate();
        };
    }

    public ActionListener searchParcelByIDButtonActionListener() {
        return actionEvent ->{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Parcel parcel = ServerCommunication.getParcelByID(Integer.parseInt(coordinatorView.getSearchParcelByIDText()));
            if(parcel!=null) {
                String[][] tableData = new String[1][6];
                tableData[0][0] = Integer.toString(parcel.getID());
                tableData[0][1] = parcel.getAddress();
                tableData[0][2] = Parcel.pointToString(parcel.getCoordinates());
                tableData[0][3] = formatter.format(parcel.getDate());
                tableData[0][4] = ServerCommunication.getUserByID(parcel.getAssignedPostmanID()).getName();
                tableData[0][5] = Integer.toString(parcel.getAssignedPostmanID());

                String[] tableCols = {
                        "ID", "Address", "Coordinates", "Date and Time", "Assigned Postman", "Assigned Postman ID"
                };
                JTable table = createTable(tableData, tableCols);
                coordinatorView.setDataTable(table);
                JScrollPane pane = coordinatorView.getDataTablePane();
                pane.setViewportView(table);
                pane.revalidate();
            }
        };
    }

    public ActionListener addParcelButtonActionListener() {
        return actionEvent -> {
            String[][] tableData = {
                    {"Address", ""},
                    {"Coordinates", ""},
                    {"Assigned Postman's ID", ""}
            };
            String[] tableCol = {"Fields", "Data"};
            DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
            final JTable mainTable = new JTable();
            mainTable.setModel(model);
            coordinatorView.setUpdateTable(mainTable);
            JScrollPane pane = coordinatorView.getUpdateTablePane();
            pane.setViewportView(mainTable);
            pane.revalidate();
            coordinatorView.getUpdateParcelButton().setVisible(true);
            coordinatorView.setUpdateParcelButtonText("Add");
        };
    }

    public ActionListener deleteParcelButtonActionListener() {
        return actionEvent -> {
            Parcel parcel = ServerCommunication.getParcelByID(selectedParcelID);
            if(parcel != null) {
                ServerCommunication.deleteParcel(parcel);
            }
            for(ActionListener a: coordinatorView.getViewParcelsButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
        };
    }

    public ActionListener updateParcelButtonActionListener() {
        return actionEvent -> {
            TableModel model = coordinatorView.getUpdateTable().getModel();
            String address = (String)model.getValueAt(0,1);
            Scanner in = new Scanner((String)model.getValueAt(1,1)).useDelimiter("[^0-9]+");
            Point point = new Point(in.nextInt(), in.nextInt());
            Integer postmanID = Integer.parseInt((String)model.getValueAt(2,1));
            Parcel parcel = new Parcel.ParcelBuilder()
                                    .address(address)
                                    .coordinates(point)
                                    .assignedPostmanID(postmanID)
                                    .build();

            if(coordinatorView.getUpdateParcelButtonText().contains("Add")){
                ServerCommunication.insertParcel(parcel);
            }
            else {
                parcel.setID(selectedParcelID);
                ServerCommunication.updateParcel(parcel);
            }
            for(ActionListener a: coordinatorView.getViewParcelsButton().getActionListeners()){
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,null));
            }
        };
    }

    public void populatePostmanTable(){
        List<User> users = ServerCommunication.getUsers();
        List<User> postmen = new ArrayList<User>();
        for(User user : users){
            if(user.getType() == User.Type.POSTMAN){
                postmen.add(user);
            }
        }
        String[][] tableData = new String[postmen.size() + 10][2];
        int size = postmen.size();
        for (int i = 0; i < size; i++) {
            tableData[i][0] = Integer.toString(postmen.get(i).getID());
            tableData[i][1] = postmen.get(i).getName();
        }
        JTable table = createTable(tableData, new String[]{"ID", "Name"});
        JScrollPane pane = coordinatorView.getPostmansTablePane();
        pane.setViewportView(table);
        pane.revalidate();
        table.setVisible(true);
    }

    public ComponentAdapter mapPaneComponentAdapter(){
        return new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent componentEvent) {
                coordinatorView.addContainerToMapPane(mainController.createContainerFromGraph(ServerCommunication.getGraphLayout()));
            }
        };
    }

    public ActionListener saveReportButtonActionListener(){
        return actionEvent -> {
            String option = coordinatorView.getReportTypeComboBoxText();
            if(option!=null) {
                if (option.equals("XML")) {
                    Serializer.serializeParcelsAsXML(ServerCommunication.getParcels());
                } else if (option.equals("CSV")) {
                    Serializer.serializeParcelsAsCSV(ServerCommunication.getParcels());
                } else if (option.equals("JSON")) {
                    Serializer.serializeReportAsJson(ServerCommunication.getParcels());
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
