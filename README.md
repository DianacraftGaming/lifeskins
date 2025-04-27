# Life Skins
A mod for changing player skins depending on the life amount without needing to restart Minecraft! Requires the [Life Series](https://modrinth.com/mod/life-series/) mod for lives tracking! Also requires [Fabric Tailor](https://modrinth.com/mod/fabrictailor/) to run properly (Hoping to make it optional in a future update)

## How to use
It's quite simple, actually! `/lifeskins reloadskin`, `/skin reload` or `/lives` will update your skin according to the amount of lives that you have! It requires a bit of setting up though, how to do that is described below!

`/lifeskins info` will show setup instructions for skins

`/lifeskins skins` will show the skins the mod recognises! Great for checking if everything is set up correctly!

## How to set up
In the config folder create a folder called "lifeskins", then in it create another, with your username as its name.

Put your skins inside, and name them after the amount of lives you have

For example if your username is AtlasTheDummy and you want a skin for when you have one life left, it would be located at `/config/lifeskins/AtlasTheDummy/1.png`

Automatically the skins are assigned the classic model, if you want the slim one, create a file called "skins.json" in your skin folder and put the following text inside: `{ "slim": true }`
### Advanced
"skins.json" has more settings than just if it should use the slim model. This section assumes you already know how to work with json files. 

## Extra info
I plan to mainly release versions I myself am going to play on, so I will most likely skip a few. If there's a particular version you'd like me to release a version of the mod for, feel free to contact me either on Discord(my username is dianacraft) or by creating an issue on [GitHub](https://github.com/DianacraftGaming/lifeskins/issues), and I'll get that done for you!
# Thank you for enjoying the mod!
