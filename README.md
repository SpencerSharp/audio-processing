My Audio Processing Codebase

TODO
- Persistence
    - Implement, test freebang
    - Global project path set/get
    - Get track and index for each device
    - Figure out how to ping all devices
- Ableton Interfacing
    - Multi-knob controller options
- Physical Interfaces
    - MIDI Handling of APC Key 25 Clip buttons
        - What functions should they have?
            - Maybe jumps to the track + device indicated, like track 1 device 3 for col 1 row 3
            - Something to do with multi-knob controllers
    - MIDI Handling of Launchpad X buttons
        - Come up with functionality scheme
- Function Interfacing
    - Allow copy pasting of LaTeX from desmos
    - Advanced viewer
    - Mapping functions to params in client
- Codebase
    - Make adding new modulators for parameters easier
    - Make adding knobs for parameters easier
- Visual Interfaces
    - Interpreting graphs as three dimensional
- Synchronization
    - Global BPM
    - Global synced clock
    - Fixing issues with possible slowness due to too many MXJ instances