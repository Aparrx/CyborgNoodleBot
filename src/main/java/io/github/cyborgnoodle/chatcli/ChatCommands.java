package io.github.cyborgnoodle.chatcli;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.permissions.Role;
import io.github.cyborgnoodle.CyborgNoodle;
import io.github.cyborgnoodle.Log;
import io.github.cyborgnoodle.Meta;
import io.github.cyborgnoodle.Random;
import io.github.cyborgnoodle.levels.LevelConverser;
import io.github.cyborgnoodle.misc.*;
import io.github.cyborgnoodle.misc.funtance.stories.GenericSentenceGenerator;
import io.github.cyborgnoodle.msg.ConversationMessages;
import io.github.cyborgnoodle.msg.SystemMessages;
import io.github.cyborgnoodle.server.ServerChannel;
import io.github.cyborgnoodle.server.ServerRole;
import io.github.cyborgnoodle.server.ServerUser;
import me.postaddict.instagramscraper.exception.InstagramException;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Chat commands
 */
public class ChatCommands {

    CyborgNoodle noodle;

    public ChatCommands(CyborgNoodle noods){
        this.noodle = noods;
    }

    public void execute(String cmd, User author, Channel channel, List<User> mentions, Message dmsg){

        Log.info(author.getName()+" issued commmand: !"+cmd);

        String[] args = cmd.split(" ");

        if(CommandBlocker.isBlocked(author)){
            channel.sendMessage(SystemMessages.getBlocked());
            return;
        }

        if(cmd.equals("rank") && !cmd.contains(" ")){
            long xptotal = noodle.getLevels().getRegistry().getXP(author.getId());
            int level = noodle.getLevels().getRegistry().getLevel(author.getId());

            long xpnext = LevelConverser.getXPforLevel(level+1);
            long xpcurrent = LevelConverser.getXPforLevel(level);
            long xpleft = xptotal - xpcurrent;
            long xpfornext = xpnext - xpcurrent;

            long giftleft = noodle.getLevels().getRegistry().getGiftStamp(author.getId())-System.currentTimeMillis();

            String tilgift;
            if(giftleft>=0){
                tilgift = Util.toHMS(giftleft) + " GTO";
            }
            else tilgift = "no GTO";

            String visual = "`"+getVisualisation(xpleft,xpfornext)+"`";

            channel.sendMessage(author.getMentionTag()+": "+xpleft+" / "+xpfornext+" XP"+" ["+xptotal+" XP"+" total] | Level "+level+" | "+tilgift+" | "+visual);
            return;
        }

        if(cmd.startsWith("rank") && cmd.contains(" ")){

            String[] msg = cmd.split(" ");
            String user = msg[1];

            User u = null;
            for(User usr : noodle.getAPI().getUsers()){
                if(usr.getMentionTag().equalsIgnoreCase(user)){
                    u = usr;
                    break;
                }
            }

            if(u==null) channel.sendMessage("I could not find \""+user+"\"");
            else{
                long xptotal = noodle.getLevels().getRegistry().getXP(u.getId());
                int level = noodle.getLevels().getRegistry().getLevel(u.getId());


                long xpnext = LevelConverser.getXPforLevel(level+1);
                long xpcurrent = LevelConverser.getXPforLevel(level);
                long xpleft = xptotal - xpcurrent;
                long xpfornext = xpnext - xpcurrent;

                long giftleft = noodle.getLevels().getRegistry().getGiftStamp(author.getId())-System.currentTimeMillis();

                String tilgift;
                if(giftleft>=0){
                    tilgift = Util.toHMS(giftleft) + " GTO";
                }
                else tilgift = "no GTO";

                String visual = "`"+getVisualisation(xpleft,xpfornext)+"`";

                channel.sendMessage(author.getMentionTag()+": "+xpleft+" / "+xpfornext+" XP"+" ["+xptotal+" "+"XP"+" total] | Level "+level+" | "+tilgift+" | "+visual);
            }
            return;
        }

        if(cmd.equals("testembed")){
            channel.sendMessage("Command disabled");
            return;
        }

        if(cmd.equals("sub")){

            Boolean sub = noodle.getRole(ServerRole.NEWS_SUB).getUsers().contains(author);


            if(sub){
                //is subscribed
                noodle.getRole(ServerRole.NEWS_SUB).removeUser(author);
                channel.sendMessage(author.getMentionTag()+" You unsubscribed from notifications on "+noodle.getChannel(ServerChannel.NEWS).getMentionTag()+"!");
            }
            else {
                //is not subscribed
                noodle.getRole(ServerRole.NEWS_SUB).addUser(author);
                channel.sendMessage(author.getMentionTag()+" You subscribed to notifications on "+noodle.getChannel(ServerChannel.NEWS).getMentionTag()+"!");
            }
            return;
        }

        if(args[0].equalsIgnoreCase("word")){
            if(args.length==2 && args[0].equalsIgnoreCase("word")){
                String word = BadWords.adjustMsg(args[1].toLowerCase());
                if(noodle.getWordStats().getData().getEntries().containsKey(word)){
                    Long count = noodle.getWordStats().getData().getEntries().get(word).getCount();
                    channel.sendMessage("**"+word+"** - "+count+"x");
                } else channel.sendMessage("Nobody ever said this word on here or it is on the exception list! "+author.getMentionTag());
            } else channel.sendMessage(ConversationMessages.getNotUnderstood());
            return;
        }

        if(args[0].equalsIgnoreCase("wstats")){
            channel.sendMessage("**Registered different words:** "+noodle.getWordStats().getData().getMap().size());
            channel.sendMessage("**Registered exceptions:** "+ WordStats.EXCEPT.length);
            return;
        }

        if(args[0].equalsIgnoreCase("avatar")){
            if(args.length==2){
                String mention = args[1];

                User user = null;
                for(User u : noodle.getAPI().getUsers()){
                    if(u.getMentionTag().equalsIgnoreCase(mention)) user = u;
                }

                if(user==null){
                    Log.info("Can't find user!");
                    return;
                }

                channel.sendMessage(user.getAvatarUrl().toString());

            } else channel.sendMessage("Wrong argument count: Usage: `!avatar @mentiontag`");
        }

        if(args[0].equalsIgnoreCase("block")){
            if(args.length==2){
                String id = args[1];

                User user = null;
                try {
                    user = noodle.getAPI().getUserById(id).get();
                } catch (InterruptedException | ExecutionException ignored) {}

                if(user==null){
                    Log.info("Can't find user!");
                    return;
                }
                if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(user)){
                    Log.info("Permission error.");
                    return;
                }

                if(CommandBlocker.isBlocked(user)){
                    CommandBlocker.unblock(user);
                    channel.sendMessage("Executed. Result: `false`");
                } else{
                    CommandBlocker.block(user);
                    channel.sendMessage("Executed. Result: `true`");
                }

            } else channel.sendMessage("Wrong argument count");

            dmsg.delete();
            return;
        }

        if(args[0].equalsIgnoreCase("words")){

            int max;
            if(args.length==2){
                if(args[1].equalsIgnoreCase("max")){
                    if(noodle.getRole(ServerRole.OWNER).getUsers().contains(author)){
                        max = noodle.getWordStats().getData().getMap().size();
                    }
                    else max = 20;
                }
                else {
                    try {
                        Integer num = Integer.valueOf(args[1]);
                        if(num<=70){
                            max = num;
                        } else{
                            if(noodle.getRole(ServerRole.OWNER).getUsers().contains(author)){
                                max = num;
                            }
                            else {
                                Log.warn("entered too big number: "+num);
                                max = 20;
                            }

                        }
                    } catch (NumberFormatException e) {
                        Log.warn("entered misformed number: "+args[1]);
                        max = 20;
                    }
                }

            }
            else max = 20;

            int i = 1;
            String msg = "*Most used words:*\n";
            for(String word : noodle.getWordStats().getWordBoard().keySet()){
                long num = noodle.getWordStats().getWordBoard().get(word);
                msg = msg + ""+word+" - "+num+"x\n";
                if(msg.length()>1800){
                    Log.info("Word Printer: reached msg limit, starting new...");
                    channel.sendMessage(msg);
                    msg = "";
                }
                i++;
                if(i>max) break;
            }

            channel.sendMessage(msg);
            return;
        }

        if(args[0].equals("time")){


            String message = "**User Time Zones**\n```\n";

            for(ServerUser su : ServerUser.values()){
                message = message + getSingleUserTime(su);
            }

            message = message+"```";

            channel.sendMessage(message);
            return;


        }

        if(cmd.equals("flip")){

            if(Random.choose()){
                channel.sendMessage(SystemMessages.getHeads());
            }
            else {
                channel.sendMessage(SystemMessages.getTails());
            }
            return;
        }

        if(cmd.startsWith("what")){

            String rest = cmd.replace("what","");

            if(rest.contains("or")){
                String[] words = rest.split(" ");
                if(words.length>2){
                    String[] sides = rest.split("or");
                    String left = sides[0];
                    String right = sides[1];

                    if(Random.choose()){
                        channel.sendMessage(author.getMentionTag()+" "+left);
                    }
                    else {
                        channel.sendMessage(author.getMentionTag()+" "+right);
                    }
                }
                else channel.sendMessage(ConversationMessages.getNotUnderstood());
            }
            else channel.sendMessage(ConversationMessages.getNotUnderstood());
            return;
        }


        if(cmd.equals("ranks")){

            channel.sendMessage("Available Ranks: ");

            String s = "";

            for(int lvl : noodle.getLevels().getCalculator().getRoles().keySet()){
                ServerRole sr = noodle.getLevels().getCalculator().getRoles().get(lvl);
                String rname = noodle.getRole(sr).getName();
                s = s + "Level "+lvl+" - "+rname+"\n";
            }

            channel.sendMessage(s);
            return;
        }

        if(cmd.equals("roles")){

            String s = "";

            for(Role r : noodle.getAPI().getServerById(noodle.getServer().getId()).getRoles()){
                if(!r.getId().equalsIgnoreCase("229000154936639488")){
                    s = s + r.getName()+" "+r.getId()+"\n@"+r.getName();
                }
                else s = s + "EVERYONE"+" "+r.getId();

            }

            channel.sendMessage(s);
            return;
        }


        if(cmd.equals("levels")){

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat deciformat = new DecimalFormat("#,###", symbols);

            int i = 1;
            String msg = "**Leaderbord**\n```";
            msg = msg + "    "+new Formatter().format("%-20s%20s%6s","NAME","XP","LEVEL") + "\n";
            msg = msg + "\n";
            for(String uid : noodle.getLevels().getLeaderboard().keySet()){
                Long xp = noodle.getLevels().getLeaderboard().get(uid);

                String sxp = deciformat.format(xp);
                Integer level = noodle.getLevels().getRegistry().getLevel(uid);
                String ifiller = getWhitespaces(2-Integer.valueOf(i).toString().length());
                String name;
                try {
                    User user = noodle.getAPI().getUserById(uid).get();
                    name = user.getNickname(noodle.getServer());
                    if(name==null){
                        name = user.getName();
                        if(name==null) name = "UNKNOWN";
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    name = "UNKNOWN";
                }

                name = ellipsize(name,19);

                String filler = getWhitespaces(20-name.length());
                String xpfiller = getWhitespaces(10-xp.toString().length());

                Formatter formatter = new Formatter();

                msg = msg + "#"+ifiller+i+" "+formatter.format("%-20s%20s%6s",name,sxp,level.toString()) + "\n";

                //msg = msg + "#"+ifiller+i+" "+name+filler+"  -  "+xp+xpfiller+" "+"XP"+"  -  Level "+level+ "\n";

                i++;
                if(i>20) break;
            }

            msg = msg + "```";


            channel.sendMessage(msg);
            return;
        }

        if(args[0].equalsIgnoreCase("fuxkit")){
            noodle.getAPI().setGame("with herself");

            Boolean force = false;
            if(args.length>1){
                if(args[1].equalsIgnoreCase("force")){
                    if(noodle.getRole(ServerRole.STAFF).getUsers().contains(author)) force = true;
                }
            }

            int i = Random.randInt(0,9);

            if(i==0 || force){
                String message = SystemMessages.getSmut();

                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(191,191,191));
                embed.setImage("https://goo.gl/59skAR");

                channel.sendMessage(message,embed);
            }
            return;
        }

        if(cmd.equals("stats")){
            channel.sendMessage("**Total Message count: **"+noodle.getLevels().getRegistry().getMsgs());
            return;
        }

        if(cmd.equals("when")){
            channel.sendMessage("**Next Gift in:** "+Util.toTimeFormat(noodle.getLevels().getRegistry().getNextBounty()-System.currentTimeMillis()));
            return;
        }

        if(cmd.startsWith("insta")){
            String[] msg = cmd.split(" ");

            if(msg.length==2){
                String user = msg[1];

                try {
                    noodle.getInstagram().listPosts(user);
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                } catch (InstagramException e) {
                    e.printStackTrace(System.out);
                }
            }
            else channel.sendMessage("No user!");

            return;

        }

        if(cmd.startsWith("short")){
            String[] msg = cmd.split(" ");

            if(msg.length==2){
                String url = msg[1];
                try {
                    try {
                        String shorturl = GoogleURLShortening.shortenUrl(url);
                        channel.sendMessage(author.getMentionTag()+" I shortened your URL for you: "+shorturl);
                    } catch (MalformedURLException e) {
                        channel.sendMessage("This is not a valid URL "+author.getMentionTag());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else channel.sendMessage("No user!");

            return;
        }

        if(cmd.equals("version")){
            channel.sendMessage("**"+Meta.getVersion()+"** Changelog:\n"+Meta.getChangelog(),Meta.getEmbed());
            return;
        }

        if(cmd.startsWith("makeme")){
            String[] msg = cmd.split(" ");

            if(msg.length==2 || msg.length==3){
                String rs = msg[1];

                User user;

                if(msg.length==3){

                    if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
                        channel.sendMessage("You are not allowed to set other peoples character! "+author.getMentionTag());
                        return;
                    }

                    String usrstr = msg[2];

                    User u = null;
                    for(User usr : noodle.getAPI().getUsers()){
                        if(usr.getMentionTag().equalsIgnoreCase(usrstr)){
                            u = usr;
                            break;
                        }
                    }

                    if(u==null){
                        channel.sendMessage("I never heard of \""+usrstr+"\". "+author.getMentionTag());
                        return;
                    }
                    else user = u;
                }
                else{

                    if(noodle.getLevels().getRegistry().getLevel(author.getId())<16){
                        channel.sendMessage("Sorry you need at least **Level 16** to change your own character! "+author.getMentionTag());
                        return;
                    }


                    user = author;
                }

                if(noodle.getRole(ServerRole.GORILLAZ).getUsers().contains(user)){
                    channel.sendMessage("You have an **Official Character Role**. You cant change your character! "+user.getMentionTag());
                    return;
                }



                Boolean murdoc = rs.equalsIgnoreCase("murdoc");
                Boolean russel = rs.equalsIgnoreCase("russel");
                Boolean noods = rs.equalsIgnoreCase("noodle");
                Boolean twod = rs.equalsIgnoreCase("2d");
                Boolean nothing = rs.equalsIgnoreCase("nothing");

                if(murdoc || russel || noods || twod || nothing){
                    Role[] toremove = new Role[]{
                            noodle.getRole(ServerRole.MURDOC),
                            noodle.getRole(ServerRole.RUSSEL),
                            noodle.getRole(ServerRole.NOODLE),
                            noodle.getRole(ServerRole.TWOD)
                    };
                    List<Role> trmv = Arrays.asList(toremove);
                    Collection<Role> rls = user.getRoles(noodle.getServer());
                    rls.removeAll(trmv);
                    try {
                        noodle.getServer().updateRoles(user, rls.toArray(new Role[rls.size()])).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    ServerRole role = null;
                    String repl = "";
                    if(murdoc){
                        role = ServerRole.MURDOC;
                        repl = "You are a real Murdoc now "+user.getMentionTag()+"!";
                    }
                    if(russel){
                        role = ServerRole.RUSSEL;
                        repl = "Big Russel "+user.getMentionTag()+"!";
                    }
                    if(noods){
                        role = ServerRole.NOODLE;
                        repl = "Now you are a real Noodle ... *and I'll never never be like that* "+user.getMentionTag()+"!";
                    }
                    if(twod){
                        role = ServerRole.TWOD;
                        repl = "Now you are a 2D *and have this eye fracture* "+user.getMentionTag()+"!";
                    }
                    if(rs.equalsIgnoreCase("nothing")){
                        channel.sendMessage("Aaand you are nothing again "+user.getMentionTag()+".");
                        Log.info("Changed character role of "+user.getName()+" to nothing.");
                        return;
                    }

                    try {
                        noodle.getRole(role).addUser(user).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    channel.sendMessage(repl);
                    Log.info("Changed character role of "+user.getName()+" to "+role.name());

                }
                else channel.sendMessage("You have to tell me who you want to be: 2D, Murdoc, Russel, Noodle or Nothing! "+author.getMentionTag());

            }
            else channel.sendMessage("You have to tell me who you want to be: 2D, Murdoc, Russel, Noodle or Nothing! "+author.getMentionTag());

            return;
        }

        if(cmd.equals("tea")){

            String message = noodle.getAPI().getCachedUserById("217783026275319810").getMentionTag()+" have some tea";

            channel.sendMessage(message);

            return;
        }

        if(args[0].equalsIgnoreCase("poll")){

            if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
                channel.sendMessage("You are not allowed to start a poll! "+author.getMentionTag());
                return;
            }

            if(!cmd.contains("#")){
                String[] choices = Arrays.copyOfRange(args, 1, args.length);

                noodle.getPolls().start(choices,channel,null);
            }
            else {

                String[] parts = cmd.replace("poll ","").replace(" # ","#").split("#");
                String choicepart = parts[0];
                String descpart = parts[1];

                String[] choices = choicepart.split(" ");

                noodle.getPolls().start(choices,channel,descpart);

            }





            return;
        }

        if(args[0].equalsIgnoreCase("tpoll")){

            if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
                channel.sendMessage("You are not allowed to start a poll! "+author.getMentionTag());
                return;
            }

            if(args.length<3){
                channel.sendMessage("You need to specify a timeout!");
                return;
            }

            String ts = args[1];
            try {
                Integer seconds = Integer.valueOf(ts);

                int millis = seconds*1000;

                noodle.doLater(() -> {
                    EmbedBuilder em = noodle.getPolls().result();
                    channel.sendMessage("",em);
                },millis);

            } catch (NumberFormatException e) {
                channel.sendMessage(ts+" is not a number!");
                return;
            }

            String[] choices = Arrays.copyOfRange(args, 2, args.length);

            noodle.getPolls().start(choices,channel,null);

            return;
        }

        if(args[0].equalsIgnoreCase("vote")){

            if(args.length>1){
                String choice = args[1];
                noodle.getPolls().vote(author,choice,channel,dmsg);
            }
            else channel.sendMessage("You need to specify what you are voting for!");

            return;
        }

        if(args[0].equalsIgnoreCase("result")){

            if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
                channel.sendMessage("You are not allowed to stop a poll! "+author.getMentionTag());
                return;
            }

            EmbedBuilder em = noodle.getPolls().result();

            channel.sendMessage("",em);

            return;
        }

        if(args[0].equalsIgnoreCase("units")){

            String msg = "```\n";

            for(UnitConverter.UnitType type : UnitConverter.UnitType.values()){
                String name = type.getName();
                msg = msg.concat(">> "+name+"\n");

                List<UnitConverter.Unit> units = new ArrayList<>(UnitConverter.Unit.getByType(type));
                Collections.sort(units, (o1, o2) -> {
                    UnitConverter.UnitSystem sys1 = o1.getSystem();
                    UnitConverter.UnitSystem sys2 = o2.getSystem();

                    if(sys1.equals(sys2)) return 0;
                    else {
                        if(sys1.equals(UnitConverter.UnitSystem.METRIC)) return 1;
                        else return -1;
                    }
                });

                for(UnitConverter.Unit unit : units){
                    String unitname = unit.getName();
                    String unitsystem = unit.getSystem().getName();
                    String unitsymbol = unit.getSymbol();
                    String row = String.format("%-20s%-6s%-10s", unitname, unitsymbol, unitsystem);
                    msg = msg.concat(" "+row+"\n");
                }
                msg = msg.concat("\n");
            }

            msg = msg.concat("```");

            channel.sendMessage(msg);

            return;

        }

        if(args[0].equalsIgnoreCase("convert")){
            //!convert 3230 kg to ml
            //    0      1   2  3  4  = length(5)
            if(args.length==5){

                String amountstr = args[1];
                String unitfromsym = args[2];
                String unittosym = args[4];

                try {
                    Double amount = Double.valueOf(amountstr);

                    UnitConverter.Unit from = UnitConverter.Unit.getBySymbol(unitfromsym);

                    if(from==null){
                        channel.sendMessage("This is not a valid unit: `"+unitfromsym+"`");
                        return;
                    }

                    UnitConverter.Unit to = UnitConverter.Unit.getBySymbol(unittosym);

                    if(to==null){
                        channel.sendMessage("This is not a valid unit: `"+unittosym+"`");
                        return;
                    }

                    try {
                        double converted = UnitConverter.convert(amount, from, to);
                        channel.sendMessage("`"+amount+" "+from.getSymbol()+"  =  "+converted+" "+to.getSymbol()+"`");
                        return;

                    } catch (UnitConverter.IncompatibleUnitTypesException e) {
                        channel.sendMessage("You can not convert a "+from.getType().getName()+" to a "+to.getType().getName()+"!");
                        return;
                    }

                } catch (NumberFormatException e) {
                    channel.sendMessage("This is not a valid number: `"+amountstr+"`");
                    return;
                }

            } else channel.sendMessage("Usage: `!convert <number> <unit> to <unit2>`");

            return;
        }

        if(args[0].equalsIgnoreCase("fun")){
            channel.sendMessage(GenericSentenceGenerator.create());
            return;
        }

        if(args[0].equalsIgnoreCase("userwords")){
            if(args.length==2){

                String mentiontag = args[1];

                User user = null;
                for(User u : noodle.getAPI().getUsers()){
                    if(u.getMentionTag().equalsIgnoreCase(mentiontag)) user = u;
                }

                if(user==null){
                    Log.info("Can't find user!");
                    return;
                }

                HashMap<String,Long> toplist = new HashMap<>();
                for(String word : noodle.getWordStats().getData().getEntries().keySet()){
                    WordStatsEntry entry = noodle.getWordStats().getData().getEntries().get(word);
                    if(entry.getUsers().containsKey(user.getId())){
                        Long wordcount = entry.getUsers().get(user.getId());
                        toplist.put(word,wordcount);
                    }
                }

                String name = user.getNickname(noodle.getServer());
                if(name==null) name = user.getName();
                if(name==null) name = "UNKNOWN NAME";

                String msg = "Words for "+name+"```\n";
                List<String> listentries = new ArrayList<>();

                toplist.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(20).forEach(entry -> {
                            String word = entry.getKey();
                            Long cl = entry.getValue();
                            String count = cl.toString();
                            word = ellipsize(word,19);
                            String wordspace = getWhitespaces(20-word.length());
                            listentries.add(word+wordspace+count);
                        });

                for(String entry : listentries){
                    msg = msg + entry + "\n";
                }

                msg = msg + "\n```";

                channel.sendMessage(msg);


            }
            else channel.sendMessage("Wrong Argument count! Usage: `!userwords @MentionTag`");

            return;
        }

        // NOT CATCHED

        channel.sendMessage("["+author.getName()+" - invalid command]");
        dmsg.delete();

    }

    public void executePrivate(String cmd, User author, Message msg){

        Log.info(author.getName()+" issued commmand [private]: !"+cmd);

        if(!noodle.getRole(ServerRole.STAFF).getUsers().contains(author)){
            msg.reply("hey man, cou cant message me in private!");
            return;
        }

        if(cmd.startsWith("short")){
            String[] spmsg = cmd.split(" ");

            if(spmsg.length==2){
                String url = spmsg[1];
                try {
                    try {
                        String shorturl = GoogleURLShortening.shortenUrl(url);
                        msg.reply(author.getMentionTag()+" I shortened your URL for you: "+shorturl);
                    } catch (MalformedURLException e) {
                        msg.reply("This is not a valid URL "+author.getMentionTag());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else msg.reply("No user!");



        }

        if(cmd.startsWith("say")){
            String[] spmsg = cmd.split(" ");

            if(spmsg.length>2 && cmd.contains("#")){
                String channel = spmsg[1];

                String s = cmd.replace("say","");
                s = s.replace(channel,"");

                for(String word : spmsg){

                    if(word.startsWith("+")){

                        String nword = word.replace("+","");

                        User u = null;
                        for(User usr : noodle.getAPI().getUsers()){
                            if(usr.getName().equalsIgnoreCase(nword)){
                                u = usr;
                                break;
                            }
                        }

                        if(u!=null) s = s.replace(word,u.getMentionTag());
                        else s = s.replace(word,nword);
                    }


                }




                String chstr = channel.replace("#","");

                ServerChannel sc =  ServerChannel.valueOf(chstr);

                if(sc!=null) noodle.getChannel(sc).sendMessage(s);
                else msg.reply("Channel is null!");

            }
            else{
                msg.reply("Specify Message pls");
            }



        }

    }


    public void onMessage(Message msg){
        String msgs = msg.getContent();
        if(msgs.startsWith("!")){
            String cmd = msgs.replace("!","");

            if(!msg.isPrivateMessage()){
                execute(cmd,msg.getAuthor(),msg.getChannelReceiver(),msg.getMentions(),msg);
            }
            else executePrivate(cmd,msg.getAuthor(),msg);


        }
    }

    private String getVisualisation(long of, long full){

        double zc = (double) of/full;
        double ta = zc*10;


        String s = "[";
        int i = 1;

        while (i<=10){
            if(ta>i) s = s + "█";
            else s = s + " ";
            i++;
        }

        s = s + "] "+(ta*10)+"%";

        return s;

    }

    private String getWhitespaces(int length){
        String s = "";
        while (length>0){
            length--;
            s = s + " ";
        }
        return s;
    }

    public String ellipsize(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength-3) + "...";
    }

    public String getSingleUserTime(ServerUser su){

        User user = noodle.getUser(su);
        String name = user.getNickname(noodle.getServer());
        if(name==null) name = user.getName();
        if(name==null) name = "UNKNOWN";
        name = ellipsize(name,29);
        String zname = su.getZone().getDisplayName(Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(su.getZone());
        Integer hour = calendar.get(Calendar.HOUR);
        Integer minute = calendar.get(Calendar.MINUTE);
        Integer ampm = calendar.get(Calendar.AM_PM);

        int nlength = name.length();
        String filler = getWhitespaces(30-nlength);

        String suffix;
        if(ampm == Calendar.AM) suffix = "AM";
        else suffix = "PM";

        String sh;
        if(hour>9) sh = hour.toString();
        else sh = "0"+hour.toString();

        String sm;
        if(minute>9) sm = minute.toString();
        else sm = "0"+minute.toString();

        return name+filler+sh+":"+sm+" "+suffix+"     ["+zname+"]\n";
    }



}
