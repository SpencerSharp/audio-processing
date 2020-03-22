#!/bin/sh
make -C /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/public.sdk/samples/vst/note_expression_synth -f /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/public.sdk/samples/vst/note_expression_synth/CMakeScripts/noteexpressionsynth_postBuildPhase.make$CONFIGURATION OBJDIR=$(basename "$OBJECT_FILE_DIR_normal") all
