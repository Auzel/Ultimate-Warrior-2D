package com.UltimateWarrior2D.tilegame.sprites;

import com.UltimateWarrior2D.graphics.Mode;

public class WarriorFactory {

    public static Warrior createWarrior(String warrior,Mode idle, Mode walk, Mode run, Mode jump, Mode attack, Mode die, Mode hurt){
        if(warrior == null)
            return null;

        
        if(warrior.equalsIgnoreCase("Hunter"))
            return new Hunter(idle, walk, run, jump, attack, die, hurt);

        else if(warrior.equalsIgnoreCase("Hail"))
            return new Hail(idle, walk, run, jump, attack, die, hurt);

        else if(warrior.equalsIgnoreCase("Thunder"))
            return new Thunder(idle, walk, run, jump, attack, die, hurt);
      
        return null;
    }
    
}
