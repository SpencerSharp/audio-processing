/*
GENERAL PLAN

Use setup template
live.drop persists the file within it, on each load it sends the path to our "Setup" object
The "Setup" object sets some global variable for the path, and pings all other mxjs to let them know
Each MXJ loads all of its preset data from the file
On freebang, each MXJ saves all of its preset data to the file
MXJs are unique by combo of track# + index in track
They somehow inherit from "PersistentDevice" and declare attributes in a way that works with this polymorphic paradigm
*/

