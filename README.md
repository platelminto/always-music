# always-music

Plays music from preferred audio player when no other sounds are being output from the device. 

Currently only for OS X & works for iTunes & Spotify, although other players are easy to add (or you can just create an issue and it will be added promptly)

## Dependencies

JNA - needed to easily use OS X native functions.

    <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna</artifactId>
        <version>4.2.2</version>
    </dependency>
