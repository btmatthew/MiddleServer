/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package middleserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author SirRat
 */
public class SystemConfiguration {
    private Logging log = new Logging();
    //Variables for this class
    private File config = new File("config.txt");
    protected String [] allConfig = new String[9];
    //Method which writes all the variables to the config file.
    protected void writeConfigFile(){
        try{
        if(!config.exists()){
            config.createNewFile();
            }
            FileWriter fw = new FileWriter(config.getAbsoluteFile(),false);
            BufferedWriter bw = new BufferedWriter(fw);
            String arrayToString="";
            for(int i=1;i<=8;i++){
                if(i!=8){
                    arrayToString=arrayToString+allConfig[i]+",";
                }else{
                    arrayToString=arrayToString+allConfig[i];
                }
            }
            bw.write(arrayToString);
            bw.flush();
            bw.close();
            }catch(IOException e) {
            }
    }
    //Method which read's all the variables from the config file.
    protected void readConfigFile(){
        String configFromFile=null;
        try{
                BufferedReader br = new BufferedReader(new FileReader(config));
                configFromFile = br.readLine();
                this.allConfig=configFromFile.split(",");
                br.close();
             }catch(IOException e){
                log.writeLog("IOException readConfigFile "+e);
            }
    }
    protected String check(){
        String[] checkList = new String[2];
        String checkConfigFromFile=null;
        String confirm=null;
        String [] checkAllConfig = new String[7];
         try{
             log.writeLog("starting checking configuration of database");
             //checks if config filnew Timestamp(date.getTime())e exists
             if(!config.exists()){
                 confirm="file_not_avaiable";
                 log.writeLog("Configuration file doesn't exists");
            }else{
                log.writeLog("Configuration file does exists");
                BufferedReader br = new BufferedReader(new FileReader(config));
                checkConfigFromFile = br.readLine();
                checkAllConfig=checkConfigFromFile.split(",");
                log.writeLog("Reading configuration from file into temporary variable");
                br.close();
                log.writeLog("Finished reading from configuration file");
                //check if user database is avaiable
                String host;
                String username;
                String password;
                try{
                host = "jdbc:derby://"+checkAllConfig[0];
                username = checkAllConfig[2];
                password = checkAllConfig[3];
                log.writeLog("Testing connection to user database");
                Connection conn = DriverManager.getConnection(host, username, password);
                boolean reachable = conn.isValid(10);// 10 sec
                if(reachable){
                    conn.close();
                    checkList[0]="pass";
                    log.writeLog("User database connection passed");
                }
             }catch (SQLException e) {
                checkList[0]="fail";
                log.writeLog("User database conection test failed "+ e);
            }
                //check if admin database is avaiable
                try{
                host = "jdbc:derby://"+checkAllConfig[4];
                username = checkAllConfig[6];
                password = checkAllConfig[7];
                log.writeLog("Testing connection to admin database");
                Connection conn1 = DriverManager.getConnection(host, username, password);
                boolean reachable1 = conn1.isValid(10);// 10 sec
                if(reachable1){
                    conn1.close();
                    checkList[1]="pass";
                    log.writeLog("Admin database connection passed");
                }}catch (SQLException err) {
                    checkList[1]="fail";
                    log.writeLog("Admin database conection test failed "+ err);
                }
                //performs check of the tests perfomed
                for(int i=0;i<=1;i++){
                    if(checkList[i].equals("fail")){
                        switch(i){
                        case 0:
                            confirm="testFailed"+","+"UserDBError";
                           break;
                        case 1:
                            if(confirm==null){
                                confirm="testFailed"+","+"AdminDBError";
                            }else{
                                confirm=confirm+","+"AdminDBError"; 
                            }
                            break;
                        }
                    }
                }
                if(confirm==null){
                        confirm="passed";
                        log.writeLog("Database test sucessful");
                    }
            }
            }catch(IOException e) {
                log.writeLog("IOException check method "+ e);
            }
         return confirm;
    }
    //method which collects User database credentials
    protected String getUserDatabaseCredentials(String data){
        readConfigFile();
        if(data.equals("dataWithPassword")){
            return "userDatabaseDetails"+","+allConfig[0]+","+allConfig[1]+","+allConfig[2]+","+allConfig[3];
        }else{
            return "userDatabaseDetails"+","+allConfig[0]+","+allConfig[1]+","+allConfig[2];
        }
    }
    //method which updates user database credentials
    protected String setUserDatabaseCredentials(String[] data){
        String reply=null;
            try{
                Connection conn1 = DriverManager.getConnection("jdbc:derby://"+data[1], data[3], data[4]);
                boolean reachable1 = conn1.isValid(10);// 10 sec
                if(reachable1){
                                log.writeLog("New User database credentials are correct");
                                Statement stmt = conn1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                String SQL = "select * from "+data[2];
                                ResultSet rs = stmt.executeQuery(SQL);
                                boolean lastRowOfDB = rs.last();
                                if(lastRowOfDB){
                                        log.writeLog("Database is accessible, and contains records.");
                                        for(int i=1;i<=4;i++){
                                        this.allConfig[i]=data[i];
                                        }
                                        writeConfigFile();
                                        log.writeLog("User Database credentials updated");
                                        reply="userDbTestCompleted";
                                        conn1.close();
                                }else{
                                    if(data[0].equals("updateUserDatabaseForSure")){
                                       for(int i=1;i<=4;i++){
                                        this.allConfig[i]=data[i];
                                        } 
                                        writeConfigFile();
                                        log.writeLog("User Database credentials updated, but not records avaiable into the database.");
                                        reply="userDbTestCompleted";
                                    }else{
                                        log.writeLog("Database is accessible, but no records are avaiable, asking user if to continue");
                                        reply="USER_DB_OK_NO_DATA";
                                    }
                                        
                                }
                        }else{
                    reply="wrongCredentials";
                }
                }catch (SQLException e) {
                    reply="user_database_error"+","+e;
                    log.writeLog("Admin database conection test failed "+ e);
                }
        return reply;
    }
    //method which collects Admin credentials
    protected String getAdminDatabaseCredentials(String data){
        readConfigFile();
        if(data.equals("dataWithPassword")){
            return "adminDatabaseDetails"+","+allConfig[4]+","+allConfig[5]+","+allConfig[6]+","+allConfig[7];
        }else{
            return "adminDatabaseDetails"+","+allConfig[4]+","+allConfig[5]+","+allConfig[6];
        }
    }
    //method which updates admin database credentials
    protected String setAdminDatabaseCredentials(String[] data){
        String reply=null;
            try{
                Connection conn = DriverManager.getConnection("jdbc:derby://"+data[1], data[3], data[4]);
                boolean reachable = conn.isValid(10);// 10 sec
                if(reachable){
                            log.writeLog("New Admin database credentials are correct");
                            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            String SQL = "select * from "+data[2];
                            ResultSet rs = stmt.executeQuery(SQL);
                            boolean lastRowOfDB = rs.last();
                            if(lastRowOfDB){
                                        log.writeLog("Admin Database is accessible, and contains records.");
                                        this.allConfig[5]=data[1];
                                        this.allConfig[6]=data[2];
                                        this.allConfig[7]=data[3];
                                        this.allConfig[8]=data[4];
                                        writeConfigFile();
                                        log.writeLog("Admin Database credentials updated");
                                        reply="adminDbTestCompleted";
                                        conn.close();
                                }else{
                                    if(data[0].equals("updateAdminDatabaseForSure")){
                                        this.allConfig[5]=data[1];
                                        this.allConfig[6]=data[2];
                                        this.allConfig[7]=data[3];
                                        this.allConfig[8]=data[4];
                                        writeConfigFile();
                                        if(data.length==6){//only used when old db is being replaced with new database which require details of new admin
                                        AdminCheck newAdmin=new AdminCheck();
                                        String[] newAdminArray=new String[3];
                                        newAdminArray[0]="test";
                                        newAdminArray[1]=data[5];
                                        newAdminArray[2]=data[6];
                                        newAdmin.newAdmin(newAdminArray);  
                                        }
                                        log.writeLog("Admin Database credentials updated, but not records avaiable into the database.");
                                        reply="adminDbTestCompleted";
                                    }else{
                                        log.writeLog("Database is accessible, but no records are avaiable, asking user if to continue");
                                        reply="ADMIN_DB_OK_NO_DATA";
                                    }
                                }
                }else{
                    reply="wrongCredentials";
                }
            }catch (SQLException e) {
                    reply="admin_database_error"+","+e;
                    log.writeLog("Admin database conection test failed "+ e);
                }   
        
        log.writeLog("User Database credentials updated");
        return reply;
    }
    protected String initialWriteConfigArray(String[] data){
        String userReply=null;
        String adminReply=null;
        String[] admin = new String[5];
        admin[0]="newUserCredentials";
        for(int i=5;i<=8;i++){
            admin[i-4]=data[i];
        }
        String userDB = setUserDatabaseCredentials(data);
        String adminDB = setAdminDatabaseCredentials(admin);
        switch(userDB){
            case "userDbTestCompleted":
                userReply="newUserDBAdded";
                break;
            case "USER_DB_OK_NO_DATA":
                data[0]="updateUserDatabaseForSure";
                setUserDatabaseCredentials(data);
                userReply="newUserDBAdded";
                break;
            default:
                userReply="userDBInitialError"+","+userDB;
                break;     
        }
        switch(adminDB){
            case "adminDbTestCompleted":
                adminReply="newAdminDBAdded";
                break;
            case "ADMIN_DB_OK_NO_DATA":
                admin[0]="updateAdminDatabaseForSure";
                setAdminDatabaseCredentials(admin);
                adminReply="newAdminDBAdded";
                break;
            default:
                adminReply=adminDB;
                break;     
        }
        return userReply+","+adminReply;
    }
}
