package View;

import Controller.CoordinatorViewController;

import javax.swing.*;
import java.awt.*;


public class CoordinatorView extends JFrame{

    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JTable dataTable;
    private JButton viewParcelsButton;
    private JButton searchParcelByIDButton;
    private JTextField parcelIDTextField;
    private JScrollPane dataTablePane;
    private JPanel mapPane;
    private JButton addParcelButton;
    private JButton deleteParcelButton;
    private JTable updateTable;
    private JButton updateParcelButton;
    private JScrollPane updateTablePane;
    private JTabbedPane tabbedPane2;
    private JTable postmansTable;
    private JScrollPane postmansTablePane;
    private JPanel viewParcelsPane;
    private JComboBox reportTypeComboBox;
    private JButton saveReportButton;

    public CoordinatorView(CoordinatorViewController coordinatorViewController){//, GraphController graphController){
        this.setTitle("Coordinator View");
        this.setSize(1080,840);
        this.add(rootPanel);

        viewParcelsButton.addActionListener(coordinatorViewController.viewParcelsButtonActionListener());
        searchParcelByIDButton.addActionListener(coordinatorViewController.searchParcelByIDButtonActionListener());
        addParcelButton.addActionListener(coordinatorViewController.addParcelButtonActionListener());
        updateParcelButton.addActionListener(coordinatorViewController.updateParcelButtonActionListener());
        deleteParcelButton.addActionListener(coordinatorViewController.deleteParcelButtonActionListener());
        mapPane.addComponentListener(coordinatorViewController.mapPaneComponentAdapter());
        this.addWindowListener(coordinatorViewController.windowListener());
        saveReportButton.addActionListener(coordinatorViewController.saveReportButtonActionListener());

        LayoutManager layoutManager = new FlowLayout();
        this.mapPane.setLayout(layoutManager);
    }


    public String getReportTypeComboBoxText(){
        return (String)this.reportTypeComboBox.getSelectedItem();
    }
    public void addContainerToMapPane(Object layout) {
        for (Component component : this.mapPane.getComponents()) {
            if (component instanceof Container) {
                this.mapPane.remove(component);
            }
        }
        //((Component)layout).setPreferredSize(new Dimension(this.mapPane.getWidth(),this.mapPane.getHeight()));
        this.mapPane.add((Component) layout);
        this.mapPane.revalidate();
        this.mapPane.repaint();
    }

    public JScrollPane getUpdateTablePane() { return this.updateTablePane; }

    public JTable getUpdateTable() { return this.updateTable; }

    public void setUpdateTable(JTable updateTable){
        this.updateTable = updateTable;
    }

    public void setUpdateParcelButtonText(String text){ updateParcelButton.setText(text); }

    public JButton getUpdateParcelButton(){ return this.updateParcelButton; }

    public String getUpdateParcelButtonText() { return this.updateParcelButton.getText(); }

    public JButton getViewParcelsButton() { return this.viewParcelsButton; }

    public JScrollPane getPostmansTablePane() { return this.postmansTablePane; }

    public JScrollPane getDataTablePane(){
        return this.dataTablePane;
    }

    public JTable getDataTable(){
        return this.dataTable;
    }

    public void setDataTable(JTable dataTable){
        this.dataTable = dataTable;
    }

    public String getSearchParcelByIDText(){
        return this.parcelIDTextField.getText();
    }

    private void createUIComponents(){
        // TODO: place custom component creation code here
    }
}
