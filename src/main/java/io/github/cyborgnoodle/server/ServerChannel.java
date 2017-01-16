package io.github.cyborgnoodle.server;

/**
 * Channels on the server
 */
public enum ServerChannel {

    CYBORGLABS("236518682405109760"),
    GENERAL("229000154936639488"),
    CYBORGTEST("237199236305911808"),
    NEWS("236492526431764480"),
    MUSEUM("248089767130955778"),
    TRASH("229009187085090817"),
    ;

    String id;

    ServerChannel(String id){
        this.id = id;
    }

    public String getID(){
        return id;
    }

}
