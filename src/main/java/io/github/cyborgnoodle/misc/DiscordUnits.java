package io.github.cyborgnoodle.misc;

/**
 *
 */
public class DiscordUnits {

    public static UnitConverter.Unit getHighestUnit(double xp){

        if(xp<1000) return UnitConverter.Unit.XP;
        if(xp<25000) return UnitConverter.Unit.SMONG;
        if(xp<100000) return UnitConverter.Unit.LSD;
        if(xp<200000) return UnitConverter.Unit.MEME;

        return UnitConverter.Unit.NOOD;
    }

    public double xp(double xp, UnitConverter.Unit unit){
        try {
            return UnitConverter.convert(xp,unit, UnitConverter.Unit.XP);
        } catch (UnitConverter.IncompatibleUnitTypesException e) {
            return xp;
        }
    }

}
