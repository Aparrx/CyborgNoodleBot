package io.github.cyborgnoodle.server;

import de.btobastian.javacord.entities.User;

import java.sql.Time;
import java.util.TimeZone;

/**
 *
 */
public enum ServerUser {

    WONKAWOE("229083996615606272",TimeZone.getTimeZone("GMT-6")),
    TWOD("229307462895796224",TimeZone.getTimeZone("GMT-8")),
    DAX("200688166930350080",TimeZone.getTimeZone("GMT")),
    QUATRER("229001776404234241",TimeZone.getTimeZone("GMT+2")),
    TRAILER("95824087221305344",TimeZone.getTimeZone("GMT+1")),
    ENVEED("145575908881727488",TimeZone.getTimeZone("GMT+1")),
    JUSSA("199790842464960512",TimeZone.getTimeZone("GMT+1")),
    PEZ("72955546168209408",TimeZone.getTimeZone("GMT+11")),
    ROY("217783026275319810",TimeZone.getTimeZone("GMT")),
    KIU("178591608416108545",TimeZone.getTimeZone("GMT+1")),
    ;

    String id;
    TimeZone zone;

    ServerUser(String id, TimeZone zone){
        this.id = id;
        this.zone = zone;
    }

    public String getID(){
        return id;
    }

    public TimeZone getZone() {
        return zone;
    }

    public static ServerUser byID(String id){
        for(ServerUser user : values()){
            if(user.getID().equalsIgnoreCase(id)) return user;
        }
        return null;
    }

    public static ServerUser byUser(User user){
        return byID(user.getId());
    }

}
