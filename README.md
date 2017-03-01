## CyborgNoodle

### Features

- shorten URLs (with goo.gl)
- let the bot decide between different things or flip a coin
- quote messages (with a lot of options)
- help and descriptions for every single command
- tell people to have some tea
- tell people to do their chores
- sue someone
- generate typical sentences for users using markov chains and a database of all messages
- generate random sentences using predefined words (non markov chain based)
- count words and see what words were used the most (per-user and with graphs!)
- XP and leveling system with a lot of options and a leaderbord
- randomly drop xp bombs
- show user time zones (configurable)
- convert units
- make a poll and have users vote for things
- download a users avatar image
- save messages as reactions and quote them with a shortcut
- show statistics with graphs (user count, online users count, message count, activity, ...)
- show file sizes
- change bot settings with commands
- simulate a chat with markov chains
- ...

### Project Structure

#### Dependencies

- Java `1.8.0_92` or newer
- JavaCord `2.0.14` or newer: https://github.com/BtoBastian/JavaCord
- EmojiLibrary by vdurmont: https://github.com/vdurmont/emoji-java
- Gson `2.7` or newer: https://github.com/google/gson
- Okio `1.11` or newer: https://github.com/square/okio
- JRaw `0.9` or newer: https://github.com/thatJavaNerd/JRAW

#### Specific /r/gorillaz server classes

- `ServerUser` enum of regular users and their IDs + timezone
(use `CyborgNoodle.getUser(ServerUser)` to get the API User)

- `ServerChannel` enum of all channels on the server and their IDs
(use `CyborgNoodle.getChannel(ServerChannel)` to get the API Channel)

- `ServerRole` enum of all roles on the server and their IDs
(use `CyborgNoodle.getRole(ServerRole)` to get the API Role)

### Todo list
(feel free to implement this yourself, as I am probably too lazy to do it)

- XP gambling