package Controller;

import Model.Parcel;
import Model.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


import java.io.*;
import java.net.Socket;
import java.util.*;

public class Serializer {
    private static String parcelFilename = "parcels.ser";
    private static String usersFilename = "users.ser";

    public static void serializeParcels(Map<Integer, Parcel> parcels) {
        try {
            FileOutputStream file = new FileOutputStream(parcelFilename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(parcels);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Map<Integer,Parcel> deserializeParcels(){
        Map<Integer,Parcel> parcel = new HashMap<Integer,Parcel>();
        try {
            FileInputStream file = new FileInputStream(parcelFilename);
            ObjectInputStream in = new ObjectInputStream(file);

            parcel = (Map<Integer, Parcel>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parcel;
    }
    public static void serializeUsers(Map<String, User> users) {
        try {
            FileOutputStream file = new FileOutputStream(usersFilename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(users);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Map<String,User> deserializeUsers(){
        Map<String,User> users = new HashMap<String,User>();
        try {
            FileInputStream file = new FileInputStream(usersFilename);
            ObjectInputStream in = new ObjectInputStream(file);

            users = (Map<String, User>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static <T> void serialize(T object, Socket socket){
        try{
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static <T> T deserialize(Socket socket) {
        try{
            T object;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            object = (T)in.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void serializeParcelsAsXML(List<Parcel> parcels){
        XmlMapper mapper = new XmlMapper();
        FileWriter fileWriter = null;
        List<String> list = new ArrayList<String>();
        try{
            fileWriter = new FileWriter(new File("reports/report.xml"));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Collection parcelsCol = parcels;
            for(Object obj : parcelsCol){
                Parcel parcel = (Parcel)obj;
                list.add(mapper.writeValueAsString(parcel));
            }
            for(String each : list){
                fileWriter.write(each);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void serializeParcelsAsCSV(List<Parcel> parcels){
        FileWriter writer = null;
        try {
            writer = new FileWriter("reports/report.csv");
            String[] header = {"ID", "address", "coordinates", "assignedPostmanID", "date"};
            for(int i = 0; i<header.length-1;i++){
                writer.write(header[i]+",");
            }
            writer.write(header[header.length-1]+"\n");
            Collection parcCol = parcels;
            for(Object parcel : parcCol){
                Parcel currentParcel = (Parcel)parcel;
                writer.write(currentParcel.getID()+",");
                writer.write(currentParcel.getAddress()+",");
                writer.write(Parcel.pointToString(currentParcel.getCoordinates())+",");
                writer.write(currentParcel.getAssignedPostmanID()+",");
                writer.write(currentParcel.getDate().toString());
                writer.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void serializeReportAsJson(List<Parcel> parcels){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("reports/report.json"), parcels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
