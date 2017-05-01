package net.erbros.lottery.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LotteryBuyTicketEvent extends Event implements Cancellable
{

    private Player player;
    private int amount;
    private boolean canceled = false;
    private static final HandlerList handlers = new HandlerList();

    public LotteryBuyTicketEvent( Player player, int amount )
    {
        this.player = player;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    @Override
    public boolean isCancelled()
    {
        return canceled;
    }

    @Override
    public void setCancelled( boolean b )
    {
        canceled = true;
    }

    public int getAmount()
    {
        return amount;
    }

    public Player getPlayer()
    {
        return player;
    }
}
