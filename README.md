## CyborgNoodle

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

- rework XP / Leveling system
- rework Commands