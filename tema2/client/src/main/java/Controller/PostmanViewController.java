package Controller;

import Model.Parcel;
import Model.User;
import View.PostmanView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostmanViewController {
    protected User currentUser;
    private PostmanView postmanView;
    private List<Parcel> parcels;
    protected MainController mainController;

    public PostmanViewController(MainController mainController, User user) {
        this.currentUser = user;
        this.postmanView = new PostmanView(this);
        this.postmanView.setVisible(true);
        this.mainController = mainController;
        this.parcels = ServerCommunication.getParcelsByPostmanID(user.getID());
    }

    private JTable createTable(String[][] tableData, String[] tableCol) {
        DefaultTableModel model = new DefaultTableModel(tableData, tableCol);
        final JTable mainTable = new JTable();
        mainTable.setModel(model);
        return mainTable;
    }

    private void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public ActionListener viewParcelsButtonActionListener() {
        return actionEvent -> {
            parcels = ServerCommunication.getParcelsByPostmanID(currentUser.getID());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            int size = parcels.size();
            Object[] tempArray = parcels.toArray();
            String[][] tableData = new String[size + 10][4];
            for (int i = 0; i < size; i++) {
                tableData[i][0] = Integer.toString(((Parcel) tempArray[i]).getID());
                tableData[i][1] = ((Parcel) tempArray[i]).getAddress();
                tableData[i][2] = Parcel.pointToString(((Parcel) tempArray[i]).getCoordinates());
                tableData[i][3] = formatter.format(((Parcel) tempArray[i]).getDate());
            }
            String[] tableCols = {
                    "ID", "Address", "Coordinates", "Date and Time"
            };
            JTable table = createTable(tableData, tableCols);
            postmanView.setDataTable(table);
            JScrollPane pane = postmanView.getDataTablePane();
            pane.setViewportView(table);
            pane.revalidate();
        };
    }

    public ActionListener searchParcelByIDButtonActionListener() {
        return actionEvent -> {
            parcels = ServerCommunication.getParcelsByPostmanID(currentUser.getID());
            int size = parcels.size();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Parcel parcel = ServerCommunication.getParcelByID(Integer.parseInt(postmanView.getSearchParcelByIDText()));
            if (parcel != null) {
                String[][] tableData = new String[1][4];
                tableData[0][0] = Integer.toString(parcel.getID());
                tableData[0][1] = parcel.getAddress();
                tableData[0][2] = Parcel.pointToString(parcel.getCoordinates());
                tableData[0][3] = formatter.format(parcel.getDate());
                String[] tableCols = {
                        "ID", "Address", "Coordinates", "Date and Time"
                };
                JTable table = createTable(tableData, tableCols);
                postmanView.setDataTable(table);
                JScrollPane pane = postmanView.getDataTablePane();
                pane.setViewportView(table);
                pane.revalidate();
            }
        };
    }

    public ComponentAdapter mapPaneComponentAdapter() {
        return new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent componentEvent) {
                postmanView.addContainerToMapPane(mainController.createContainerFromGraph(ServerCommunication.getGraphLayout()));
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

