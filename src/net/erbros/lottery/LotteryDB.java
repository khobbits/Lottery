package net.erbros.lottery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
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
        plugin.getLotteryConfig().debugMsg( "Initiating task" );
        addWinToDBTask task = new addWinToDBTask( conn, stmt, username, ticketsBought, amountWon, totalTicketsBought );
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        BukkitTask bukkitTask = scheduler.runTaskAsynchronously( plugin, task );
        plugin.getLotteryConfig().debugMsg( bukkitTask.getTaskId()+" <- should be task ID" );
        plugin.getLotteryConfig().debugMsg( "isQueued "+scheduler.isQueued( bukkitTask.getTaskId() ) );
        plugin.getLotteryConfig().debugMsg( "isCurrentlyRunning "+scheduler.isCurrentlyRunning( bukkitTask.getTaskId() ) );
    }
}

class addWinToDBTask extends TimerTask
{
    private Connection        conn;
    private PreparedStatement stmt;
    private String            username;
    private int               ticketsBought;
    private double            amountWon;
    private int               totalTicketsBought;

    public addWinToDBTask( Connection conn, PreparedStatement stmt, String username, int ticketsBought, double amountWon, int totalTicketsBought )
    {
        this.conn = conn;
        this.stmt = stmt;
        this.username = username;
        this.ticketsBought = ticketsBought;
        this.amountWon = amountWon;
        this.totalTicketsBought = totalTicketsBought;
    }

    @Override
    public void run()
    {
        try
        {
            String query = "INSERT INTO `wins`(`username`, `ticketsbought`, `amountwon`, `totalticketsbought`) VALUES( ?, ?, ?, ?)";
            stmt = conn.prepareStatement( query );
            stmt.setString( 1, username );
            stmt.setInt( 2, ticketsBought );
            stmt.setDouble( 3, amountWon );
            stmt.setInt( 4, totalTicketsBought );
            stmt.execute();
            stmt.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }
}
