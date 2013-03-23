/* JDBC-Treiber fuer Oracle:
 * http://www.oracle.com/technetwork/database/features/jdbc/index-091264.html
 * Aktuelle Version sollte abwaertskompatibel sein
 * 
 * Einbinden ueber:
 * Project > Properties > Libraries > Java Build Path > Add External JARs
 */

// Fuer Verbindungsaufbau und Ausnahmebehandlung benoetigt
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.SQLException;
// Fuer Abfrageerzeugung und -bearbeitung benoetigt
import java.sql.Statement;
import java.sql.ResultSet; 
// Fuer Prepared Statements benoetigt
import java.sql.PreparedStatement;
// Fuer Waehrungswerte benoetigt
import java.math.BigDecimal;

public class JdbcTester {

  Connection connection = null;
	
  String driverName ="oracle.jdbc.driver.OracleDriver"; // laedt JDBC-Treiber fuer Oracle
	
//  String serverName = "194.95.45.165"; // Telematik-Oracle-Server
  String serverName = "localhost"; // lokaler Oracle-Server
  String portNumber = "1521";
//  String sid = "orcl"; // SID des Telematik-Oracle-Servers
  String sid = "xe"; // SID fuer Oracle XE
  
  String url="jdbc:oracle:thin:@"+serverName+":"+portNumber+":"+sid; // Connection-String fuer Oracle
  
  String username = "terra"; // entspr. anpassen
  String password = "terra"; // entspr. anpassen
  
//  public JdbcTester() {}
  
  public boolean doConnection(){ 
	try {
      // Lade den JDBC-Treiber
      Class.forName(driverName);
      // Erzeuge DB-Verbindung
      connection = DriverManager.getConnection(url, username, password);
    } 
    catch (ClassNotFoundException e) {
      // Kann DB-Treiber nicht finden 
      System.out.println("ClassNotFoundException : " + e.getMessage());
      return false;
    } 
    catch (SQLException e) {
      // Kann keine Verbindung zur DB herstellen
      System.out.println(e.getMessage()); 
      return false;
    }
    return true; 
  }
  
  // Ermittle Album- und Trackdaten fuer bestimmten Kuenstler
  public void printAlbumTracksByArtist(String artist) throws SQLException{
    Statement stmt = null;
    stmt = connection.createStatement(); 

    String query = "SELECT al.title, tr.trackid, tr.name, tr.unitprice "+
                   "FROM chinook.artist ar JOIN chinook.album al ON ar.artistid = al.artistid JOIN chinook.track tr ON al.albumid = tr.albumid "+
    		       "WHERE ar.name LIKE '%"+artist+"%'";
  
    ResultSet rs = stmt.executeQuery(query); 
    while (rs.next()) { 
      System.out.println("Album: " + rs.getString("title")); 
      int trackid = rs.getInt("trackid");
      String track= rs.getString("name");
      BigDecimal unitprice = rs.getBigDecimal("unitprice");
      System.out.println("[" + trackid + "] " + track + " (" + unitprice + " EUR)\n"); 
	} 
    
    rs.close();
    stmt.close();
  }  
  
  // Aktualisiere Kundenadresse -- einfach
  public void updateCustomerAddress(int customerId, String address, String city, String postalCode) throws SQLException{
    Statement stmt = connection.createStatement();
    String sql = "UPDATE chinook.customer " +
                 "SET address='"+ address +"', city='" + city + "', postalcode='" + postalCode + "' " + 
                 "WHERE customerid=" + customerId; 
    
    int rows = stmt.executeUpdate(sql);
    System.out.println(rows + " Zeile(n) betroffen.");
    
    stmt.close(); 
  }  
  
  // Aktualisiere Kundenadresse -- mit Prepared Statement
  public void updateCustomerAddressPrepared(int customerId, String address, String city, String postalCode) throws SQLException{
    PreparedStatement stmt = connection.prepareStatement("UPDATE chinook.customer " +
	                                                     "SET address = ?, city = ?, postalcode = ? " + 
	                                                     "WHERE customerid = ?"); 
	stmt.setString(1, address);
	stmt.setString(2, city);
	stmt.setString(3, postalCode);
	stmt.setInt(4, customerId);
	
	int rows = stmt.executeUpdate();
	System.out.println(rows + " Zeile(n) betroffen.");
	   
	stmt.close(); 
  }  
  
  public static void main(String arg[]){ 
    JdbcTester con =new JdbcTester();
    System.out.println("Connection : " + con.doConnection());
    try {
      con.printAlbumTracksByArtist("Santana"); 
//      con.updateCustomerAddress(36, "Leipziger Str. 100", "Berlin", "10117"); 
//      con.updateCustomerAddressPrepared(36, "Leipziger Str. 100", "Berlin", "10117"); 
         }
    catch(SQLException ex) {
      System.out.println(ex.getMessage());
    }
  } 
}