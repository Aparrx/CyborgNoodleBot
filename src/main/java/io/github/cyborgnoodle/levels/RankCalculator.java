package io.github.cyborgnoodle.levels;

import io.github.cyborgnoodle.server.ServerRole;

import java.util.HashMap;

/**
 * get the rank role for a level
 */
public class RankCalculator {

    HashMap<Integer,ServerRole> roles;

    public RankCalculator(){
        roles = new HashMap<>();
        create();
    }

    private void create(){

        roles.put(3,ServerRole.RANK_JANITOR);
        roles.put(7,ServerRole.RANK_LIFEBOATGUY);
        roles.put(12,ServerRole.RANK_DAREMECH);
        roles.put(19,ServerRole.RANK_TRUCKDRIVER);
        roles.put(26,ServerRole.RANK_WINDMILLINSPECTOR);
        roles.put(35,ServerRole.RANK_CEO);

    }

    public ServerRole getRoleforLevel(int lvl){

        ServerRole role = null;
        while(lvl>0){
            role = roles.get(lvl);
            if(role==null){
                lvl--;
            }
            else break;
        }

        if(role==null) return null;
        else return role;
    }

    public HashMap<Integer, ServerRole> getRoles() {
        return roles;
    }
}
