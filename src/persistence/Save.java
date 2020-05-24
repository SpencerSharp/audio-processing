/*

Needs to be specific to project
    this.patcher.filepath
Needs to be specific to channel

Needs to be specific to device in channel
*/

/*
Gets path from live.drop
When making a new project, just consolidate an audio clip, drag it into live.drop
Then this function gets the path out of it
Now we know where to save settings

BUT HEY, WAIT, HOW DO WE KNOW WHERE TO GET IT FROM
I think live.drop persists the file
So each time u reopen, it should auto send the file out
nice!
*/
public void anything(String message, Atom args[]) {
    String path = message.substring(message.indexOf(":")+1,message.length());
}

/*
Next, we need to figure out how to alert all devices to this file
*/

/*
How do we actually save and load to the file.
MaxObject ids probably arent necessarily persisted across sessions

LOM
    Set
        Track
            Devices

So, track + device ind uniquely identifies each device

BUT WHAT IF WE WANT TO MOVE STUFF AROUND?

That's fine, we're going to save all this data before we exit anyways
So it'll always reflect the current setup.
We don't need to reconcile with past saved data or anything
*/

/*
How to trigger save?
prob freebang
*/