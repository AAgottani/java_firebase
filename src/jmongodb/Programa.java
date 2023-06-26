package jmongodb;




import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Programa {

	public static void main(String[] args) throws SQLException {

		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.OFF);
		Utils.menu();
	}
}
