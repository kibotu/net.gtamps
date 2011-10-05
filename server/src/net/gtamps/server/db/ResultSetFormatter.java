package net.gtamps.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetFormatter {
	public static String outputPlayer(ResultSet rs){
		StringBuilder sb = new StringBuilder();
		sb.append("PLAYERS \n");
		sb.append("ID | username | password\n");
		try {
			while(rs.next()){
				sb.append(rs.getInt(1)+" | "+rs.getString(2)+" | "+rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
