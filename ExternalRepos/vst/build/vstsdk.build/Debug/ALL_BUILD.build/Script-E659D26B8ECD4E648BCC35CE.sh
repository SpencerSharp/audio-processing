#!/bin/sh
make -C /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build -f /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/CMakeScripts/ALL_BUILD_cmakeRulesBuildPhase.make$CONFIGURATION OBJDIR=$(basename "$OBJECT_FILE_DIR_normal") all
