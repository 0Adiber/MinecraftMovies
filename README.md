# Minecraft Movies

This is a Spigot Plugin, which lets you watch videos in vanilla Minecraft. This is done using Item Frames and some quick Maths. (actually the maths is kinda not quick). **Note, that this is not usable for real life application, but rather as a proof of concept.** It is also important to note, that this should not encourage anyone to watch or distribute pirated movies using Minecraft. 

## How it works

The process to display videos in the Client is kinda tedious, so read carefully. 

1. First of all we know, that Minecraft works with 20 ticks/s, so I figured I will stick to one frame per tick, meaning to a total of 20 fps. 
2. To read videos, I used ffmpeg at the beginning (well a library for it), but since this library is >200MB I ended up using Images only for dev. => this meant using ffmpeg (in the console) to split the video into its frames. (here it is important, that we make sure that the video only has 20fps)
3. Then we read those images one by one and "render" them. And with render I mean we first split the images into 128*128 chunks (for the Item Frames), after which we need to match the RGBA colors of the PNG to Minecraft Map Colors (MMC). The latter process of which takes a lot of processing power, which makes real time rendering not possible. Finally we also need to calculate the relative Positions of those Item Frames, so that the Video Frames are displayed correctly.
4. Last but not least we need to save the file, so that we can load it at a later point.
5. Displaying those rendered frames is done using Packets -> [Protocol](https://wiki.vg/Protocol#Map_Data) 
6. To now update them, we go through each Location, send a packet to destroy the frame that is currently there and then send a Packet for the new frame to show. (simplified). 
7. To provide Audio I created a little Discord Bot, which uses JDA (Java Discord API). 



## Performance

This is a really interesting section, since we have a few problems regarding it:

1. Rendering
2. Files
3. Networking



### Rendering

Rendering a video is really hard work for the computer. As much as I would have liked real time rendering (and displaying), it just does not work, because the function, which matches RGBA to MMC is on the lower end of the spectrum when talking about performance. I tried using a different algorithm (which also would not have made real time rendering possible), but this resulted in wrong colors, because there is a certain red shift we would need to calculate for the MMC. 

### Files

Because Real Time Rendering is not possible, I decided to save the files, since rendering is (as I mentioned several times) really hard work. This does not sound that bad, until you realize, the MMC types have a size of 1B. Now you have that for each Pixel. Meaning that if you have a video of 1920x1080, you would get >2Million Pixels => >2MB per Video Frame! If you now have a 1 minute Video with 20FPS and 1080p you would end up with almost 2,5GB. And then you also have 2B for the relative position. This relative position is only for each Item Frame though, so for a 1080p video you would end up with 120 Item Frames, each of those containing 128 Pixels.

### Networking

All those things above could be condoned, but THIS crushes everything. We know that each Item Frame contains 128x128 Pixels, and that each of those Pixels has a MMC, which has a size of 1B. This means, that each Item Frame has a size of over 16KB!! Since we have 120 Item Frames per Video Frame, we need to transmit ~2MB of Pixel Data. Since we want a Video and not a static image, we now need to do that 20 times a second, leading us to 40MB/s!!!! And this is only per Player. Meaning you would need a server with a good internet connection and then you need users, who all have at least 40MB/s of Download Speed. 

### Conclusion

The Networking part really crushed this whole Project, but it is still possible on localhost. It is probably also possible in your local Network. Also important to mention is, that because of all those Packets, you will time out after some time. The Server handles it with no problems, but the Client somehow cannot send any Packets to the Server anymore. 



## Usage

To use the Plugin you first of all either need to download it from the [Releases](https://github.com/0Adiber/MinecraftMovies/releases) or you clone the Repo and [build](https://github.com/0Adiber/MinecraftMovies#build) it yourself. You will also need a Spigot-1.16.2 Server

### Setup

Put the plugin in your plugins folder, reload the Server and then open  `plugins/MinecraftMovies/config.yml`. In this file you need to specify your Discord Bot Token (which you have to create of course). You will also need to make sure, that your Bot is on the Server (at least needs permissions to Join and Speak)

### Create a Canvas

Use `/cc` to create a Canvas and remember the `CID`!

![Create Canvas](/images/cc.png)

### Render Video

To render a video there are a few more steps needed.

1. Download FFMPEG 
2. Go to `<your server>/plugins/MinecraftMovies/movies/`, create a new folder `FOLDER` and paste in your video file `FILE`.
3. Use FFMPEG to get the video to 20FPS `ffmpeg -i FILE -filter:v fps=fps=20 out.mp4`
4. Then split the `FILE` intro separate images using `ffmpeg -i out.mp4 -qscale:v 1 %d.png`
5. Now get the audio of your video: `ffmpeg -i out.mp4 FOLDER.mp3` (it is important, that the name of the MP3 is `FOLDER`)
6. Cut and Paste the `FOLDER.mp3` into `plugins/MinecraftMovies/saves/audio` (create folder if not exists)
7. Now we can actually render the Video in MC by typing `/render CID folder FOLDER` => the progress can be seen in the Server Console

![Render](/images/rendr.png)

Note that this process will take some time depending on the length of the video and make sure you have enough RAM allocated to the server. 

### Verify

After the Render has finished you will get a notification in the console and in your Client. Before we can play a video, we need to do one last thing, which is verifying with Discord. This is done in two steps:

1. Use the `/verify` command in Minecraft to get a code `CODE`

   ![VerifyCmd](/images/verify.png)

2. Go to Discord, open the direct message for the Bot and type `m!verify CODE`

3. Now your MC UUID is linked with your Discord User ID (at the moment this is not being saved though, so you need to do that every time you restart the server)

### Playing

Finally we can play the Video. Before you do that, make sure you are in a Discord Channel on which the Bot is (if you don't, the video won't start). Now we simply execute `/start CID FOLDER`. And we hear the Bot joining the Channel and a Wall of Frames displaying in Minecraft. 

![video](/images/vid.png)

And there you have the Marvel Intro from [Captain America: The Winter Soldier](https://www.youtube.com/watch?v=LIBg2oUYcAQ). And it looks surprisingly good. (And the audio in discord is also clean)

## Build

Building the Plugin requires Maven. And the Spigot API as well as the Spigot Server file. 

### Aquire Spigot files

1. Download Spigot Build Tools [here](https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar)
2. Execute `java -jar BuildTools.jar --rev 1.16.2` => this will build Spigot (and other versions) and copy them into your local Maven repository. 

### Create .jar

All you need to do now is package the source code using maven. (I did this using IntelliJ). The command would be `mvn package` (if you have maven actually installed)


## TODO
- [ ] dynamically load Videos
- [ ] save Videos while render (otherwise memory goes boom)
- [ ] find a way to compress the video file size
