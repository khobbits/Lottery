package net.erbros.lottery;

import java.util.*;

public class RandomCollection<E>
{
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RandomCollection()
    {
        this( new Random() );
    }

    public RandomCollection( Random random )
    {
        this.random = random;
    }

    public void add( double weight, E result )
    {
        if ( weight <= 0 )
        {
            return;
        }
        total += weight;
        map.put( total, result );
    }

    public E next()
    {
        double value = random.nextDouble() * total;
        return map.ceilingEntry( value ).getValue();
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }


    public List<E> values()
    {
        return new ArrayList<>( map.values() );
    }

    public String toString()
    {
        return map.toString();
    }
}