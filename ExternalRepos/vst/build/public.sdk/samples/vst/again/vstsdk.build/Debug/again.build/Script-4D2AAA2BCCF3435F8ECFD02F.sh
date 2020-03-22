#!/bin/sh
make -C /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/public.sdk/samples/vst/again -f /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/public.sdk/samples/vst/again/CMakeScripts/again_postBuildPhase.make$CONFIGURATION OBJDIR=$(basename "$OBJECT_FILE_DIR_normal") all
