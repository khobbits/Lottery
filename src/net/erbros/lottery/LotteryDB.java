package net.erbros.lottery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class LotteryDB
{
    private final String     url;
    private final String     user;
    private final String     password;
    private       Connection conn;
    private       PreparedStatement  stmt;
    private Lottery plugin;

    public LotteryDB( String host, int port, String database, String user, String password, Lottery plugin ) throws ClassNotFoundException, SQLException
    {
        this.plugin = plugin;
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8";
        this.user = user;
        this.password = password;
        Class.forName( "com.mysql.jdbc.Driver" );
        createConnection();
        createTablesIfNotExist();
    }

    private void createConnection() throws SQLException
    {
        conn = DriverManager.getConnection( url, user, password );
    }

    private void createTablesIfNotExist() throws SQLException
    {
        String query = "CREATE TABLE IF NOT EXISTS `wins` (\n" +
                       "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                       "  `username` varchar(20) COLLATE utf8_bin NOT NULL,\n" +
                       "  `ticketsbought` int(11) NOT NULL,\n" +
                       "  `amountwon` int(11) NOT NULL,\n" +
                       "  `totalticketsbought` int(11) NOT NULL,\n" +
                       "  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                       "  PRIMARY KEY (`id`)\n" +
                       ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
        stmt = conn.prepareStatement( query );
        stmt.execute();
        stmt.close();
    }


    public void addWinToDB( String username, int ticketsBought, double amountWon, int totalTicketsBought ) throws SQLException
    {
        plugin.getLotteryConfig().debugMsg( "Trying to add win to DB" );
        String query = "INSERT INTO `wins`(`username`, `ticketsbought`, `amountwon`, `totalticketsbought`) VALUES( ?, ?, ?, ?)";
        stmt = conn.prepareStatement(query);
        stmt.setString( 1, username );
        stmt.setInt( 2, ticketsBought );
        stmt.setDouble( 3, amountWon );
        stmt.setInt( 4, totalTicketsBought );
        stmt.execute();
        stmt.close();
        plugin.getLotteryConfig().debugMsg( "Win successfully added to DB" );
    }
}
