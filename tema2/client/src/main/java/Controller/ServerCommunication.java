package Controller;

import Model.Parcel;
import Model.User;
import org.jgrapht.Graph;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerCommunication {
    public static Socket socket = null;
    public static MainController mainController = null;

    public static void setMainController(MainController mainController1){
        mainController = mainController1;
    }
    public static void setSocket(Socket clientSocket){
        socket = clientSocket;
    }

    public static List<User> getUsers(){
        CommunicationProtocol<Object> protocol = new CommunicationProtocol<Object>(
                CommunicationProtocol.GET_ALL_USERS,null);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<User> response =  Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_ALL_USERS);
        return response.objects;
    }

    public static List<Parcel> getParcels()  {
        CommunicationProtocol<Object> protocol = new CommunicationProtocol<Object>(
                CommunicationProtocol.GET_ALL_PARCELS,null);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Parcel> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_ALL_PARCELS);
        return response.objects;
    }

    public static User verifyLogin(String username, String password){
        List<String> list = new ArrayList<>(2);
        list.add(username);
        list.add(password);
        CommunicationProtocol<String> protocol = new CommunicationProtocol<String>(
                CommunicationProtocol.VERIFY_LOGIN, list);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<User> response =  Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.VERIFY_LOGIN);
        User user = response.objects.get(0);
        if(user!=null) {
            mainController.switchView(user);
            return user;
        }
        return null;
    }

    public static List<Parcel> getParcelsByPostmanID(int ID){
        List<Integer> args = new ArrayList<>(1);
        args.add(ID);
        CommunicationProtocol<Integer> protocol = new CommunicationProtocol<Integer>(
                CommunicationProtocol.GET_PARCELS_BY_POSTMAN_ID,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Parcel> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_PARCELS_BY_POSTMAN_ID);
        return response.objects;
    }

    public static Parcel getParcelByID(int ID){
        List<Integer> args = new ArrayList<>(1);
        args.add(ID);
        CommunicationProtocol<Integer> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.GET_PARCELS_BY_ID,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Parcel> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_PARCELS_BY_ID);
        return response.objects.get(0);
    }

    public static Graph getGraphLayout(){
        CommunicationProtocol<User> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.GET_GRAPH_LAYOUT,null);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Graph> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_GRAPH_LAYOUT);
        return response.objects.get(0);
    }

    public static Integer addUser(User user){
        List<User> args = new ArrayList<>(1);
        args.add(user);
        CommunicationProtocol<User> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.INSERT_USER,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Integer> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.INSERT_USER);
        return response.objects.get(0);
    }

    public static void updateUser(User oldUser, User user){
        List<User> args = new ArrayList<>(2);
        args.add(oldUser);
        args.add(user);
        CommunicationProtocol<User> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.UPDATE_USER,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Object> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.UPDATE_USER);
    }

    public static void deleteUser(User user){
        List<User> args = new ArrayList<>(1);
        args.add(user);
        CommunicationProtocol<User> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.DELETE_USER,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Object> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.DELETE_USER);
    }
    public static void exit(){
        CommunicationProtocol<Object> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.EXIT,null);
        Serializer.serialize(protocol,socket);
    }

    public static User getUserByUsername(String username){
        List<String> args = new ArrayList<>(1);
        args.add(username);
        CommunicationProtocol<String> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.GET_USER_BY_USERNAME,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<User> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_USER_BY_USERNAME);
        return response.objects.get(0);
    }

    public static User getUserByID(int id){
        List<Integer> args = new ArrayList<>(1);
        args.add(id);
        CommunicationProtocol<Integer> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.GET_USER_BY_ID,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<User> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.GET_USER_BY_USERNAME);
        return response.objects.get(0);
    }

    public static int insertParcel(Parcel parcel){
        List<Parcel> args = new ArrayList<>(1);
        args.add(parcel);
        CommunicationProtocol<Parcel> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.INSERT_PARCEL,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Integer> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.INSERT_PARCEL);
        return response.objects.get(0);
    }
    public static void updateParcel(Parcel parcel){
        List<Parcel> args = new ArrayList<>(1);
        args.add(parcel);
        CommunicationProtocol<Parcel> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.UPDATE_PARCEL,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Object> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.UPDATE_PARCEL);
    }
    public static void deleteParcel(Parcel parcel){
        List<Parcel> args = new ArrayList<>(1);
        args.add(parcel);
        CommunicationProtocol<Parcel> protocol = new CommunicationProtocol<>(
                CommunicationProtocol.DELETE_PARCEL,args);
        Serializer.serialize(protocol,socket);
        CommunicationProtocol<Object> response = Serializer.deserialize(socket);
        assert(response.communicationPurpose == -CommunicationProtocol.DELETE_PARCEL);
    }

}
