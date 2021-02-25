package Controller;

import Model.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import org.jgrapht.Graph;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class MainController {
    private LoginViewController loginViewController;
    private final String CLIENT_CONFIGURATION_FILE_PATH = "client.cfg";
    private Socket socket;

    public MainController() throws InterruptedException {
        this.loginViewController = new LoginViewController();
        clientSetup();
    }

    public void clientSetup(){
        BufferedReader reader;
        Pattern ipAddressReg = Pattern.compile("IP Address\\s*:\\s*(([0-9]{1,3}\\.){3}[0-9]{1,3})");
        Pattern portReg = Pattern.compile("Port\\s*:\\s*([0-9]+)");
        String ipAddress = null;
        int port = 0;
        try{
            reader = new BufferedReader(new FileReader(CLIENT_CONFIGURATION_FILE_PATH));
            String line = reader.readLine();
            do{
                Matcher ipAddressMatcher = ipAddressReg.matcher(line);
                Matcher portMatcher = portReg.matcher(line);
                if(ipAddressMatcher.matches()){
                    ipAddress = ipAddressMatcher.group(1);

                }
                if(portMatcher.matches()){
                    port = Integer.parseInt(portMatcher.group(1));

                }
                line=reader.readLine();
            }while(line!=null);
            if(ipAddress == null || port == 0){
                exit(0);
            }
            socket = new Socket(ipAddress,port);
            ServerCommunication.setMainController(this);
            ServerCommunication.setSocket(socket);

            //ServerCommunication.verifyLogin("Cicada","coord");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchView(User user){
        if(user.getType()== User.Type.ADMINISTRATOR){
            loginViewController.endView();
            AdministratorController administratorController = new AdministratorController(this);
        }
        if(user.getType() == User.Type.POSTMAN){
            loginViewController.endView();
            PostmanViewController postmanViewController = new PostmanViewController(this, user);
        }
        if(user.getType() == User.Type.COORDINATOR){
            loginViewController.endView();
            CoordinatorViewController coordinatorViewController = new CoordinatorViewController(this, user);
        }
    }

    public Object createContainerFromGraph(Graph graph){
        JGraphXAdapter<Parcel, GraphController.WeightedEdge> graphAdapter =
                new JGraphXAdapter<Parcel, GraphController.WeightedEdge>(graph);

        mxGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        mxGraphModel graphModel  = (mxGraphModel)graphComponent.getGraph().getModel();
        Collection<Object> cells =  graphModel.getCells().values();
        mxUtils.setCellStyles(graphComponent.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        graphComponent.setPreferredSize(new Dimension(400,400));


        return graphComponent;
    }
}
