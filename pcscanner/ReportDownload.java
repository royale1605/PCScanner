/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcscanner;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author jatsss
 */
public class ReportDownload {
    
    
    void report(int scanid) throws IOException, ClassNotFoundException, SQLException
    {
        System.out.println("Test");
        System.out.println("Scanid 3" + scanid);
        int counter = 1;
        
        StringBuilder htmlStringBuilder=new StringBuilder();
        htmlStringBuilder.append("<html><head><title>PC Scanner Report</title></head>");
        htmlStringBuilder.append("<body>");
        
        
        String scanname;
        String scants;
        String hostdetails;
        String portdetails;
        String location;
        String modifydetails;
        String faileddetails;
        String usbdetails;
         
        Connection con = null;  
        PreparedStatement pst = null;
        Class.forName("com.mysql.jdbc.Driver");
        con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/pcscanner","root","42kaone");
        
        ResultSet rs=null;
        String query1 = "Select scan_name,scan_ts,host_details from scan_master where scan_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query1);
        pst.setInt(1,scanid);
        rs = pst.executeQuery();
        while(rs.next())    
            {
                  scanname = rs.getString(1);
                  scants = rs.getString(2);
                  hostdetails = rs.getString(3);
                  
                  htmlStringBuilder.append("<h2>"+"Your scan name:-   "+ scanname+"</h2>");
                  htmlStringBuilder.append("<h2>"+"You have started this scan at:-   "+scants+"</h2>");
                  htmlStringBuilder.append("<h2>"+"Your host Machine Details:-   "+hostdetails+"</h2>");
                  
            }
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<h3>Current Active Network Connections </h3>");
        htmlStringBuilder.append("<br/>");
        
        rs=null;
        String query2 = "Select port_details from scan_port where scan_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query2);
        pst.setInt(1,scanid);
        rs = pst.executeQuery();
        while(rs.next())    
            {
                  portdetails = rs.getString(1);
                  
                  htmlStringBuilder.append("<h5>"+ counter+".)"+ portdetails+"</h5>");
                  counter++;
                 
                  
            }
              
        
        counter = 1;
        
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<h3>Files Modified in Desktop& Documents Directory in last 15 days</h3>");
        htmlStringBuilder.append("<br/>");
        
        rs=null;
        String query3 = "Select location,modify_details from scan_modify where scan_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query3);
        pst.setInt(1,scanid);
        rs = pst.executeQuery();
        while(rs.next())    
            {
                location = rs.getString(1);  
                modifydetails = rs.getString(2);
                  
                htmlStringBuilder.append("<h5>"+ counter+".)"+ location + "-->" + modifydetails+"</h5>");
                counter++;
                 
                  
            }
        
        counter = 1;
        
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<h3>Failed Login Details</h3>");
        htmlStringBuilder.append("<br/>");
        
        rs=null;
        String query4 = "Select fail_details from scan_failed_login where scan_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query4);
        pst.setInt(1,scanid);
        rs = pst.executeQuery();
        while(rs.next())    
            {
                
                faileddetails = rs.getString(1);
                  
                htmlStringBuilder.append("<h5>"+ counter+".)"+ faileddetails+"</h5>");
                counter++;
                 
                  
            }
        
        counter = 1;
        
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<br/>");
        htmlStringBuilder.append("<h3>USB Storage Plugged In Details</h3>");
        htmlStringBuilder.append("<br/>");
        
        rs=null;
        String query5 = "Select usb_details from scan_usb where scan_id = ?";
        pst = (PreparedStatement) con.prepareStatement(query5);
        pst.setInt(1,scanid);
        rs = pst.executeQuery();
        while(rs.next())    
            {
                
                usbdetails = rs.getString(1);
                  
                htmlStringBuilder.append("<h5>"+ counter+".)"+ usbdetails+"</h5>");
                counter++;
                 
                  
            }
        
        htmlStringBuilder.append("</table></body></html>");
        WriteToFile(htmlStringBuilder.toString(),"scanreport.html");
        
        
    }
    
    void WriteToFile(String fileContent, String fileName) throws IOException {
        String projectPath = System.getProperty("user.dir");
        String tempFile = projectPath + File.separator+fileName;
        System.out.println(projectPath);
        System.out.println(tempFile);
        
        File file = new File(tempFile);
        
        if (file.exists()) {
            try {
                File newFileName = new File(projectPath + File.separator+ "backup_"+fileName);
                file.renameTo(newFileName);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       
        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
        Writer writer=new OutputStreamWriter(outputStream);
        writer.write(fileContent);
        writer.close();
        JFrame jf;
        jf=new JFrame();  
        JOptionPane.showMessageDialog(jf,"Your file has been downloaded at"+ tempFile,"Alert",JOptionPane.WARNING_MESSAGE);

    }
    
       
}
