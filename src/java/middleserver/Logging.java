package middleserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SirRat
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logging {
    
    protected String writeLog(String data){
         try{
             File logs = new File("logs.txt");
             
        if(!logs.exists()){
            logs.createNewFile();
            }
        
            FileWriter fw = new FileWriter(logs.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            java.util.Date date= new java.util.Date();
            bw.write(new Timestamp(date.getTime())+data);
            bw.newLine();
            bw.flush();
           
            bw.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        return "all done";
    }
    
    
    
}
