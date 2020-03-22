# DO NOT EDIT
# This makefile makes sure all linkable targets are
# up-to-date with anything they link to
default:
	echo "Do not invoke directly"

# Rules to remove targets that are older than anything to which they
# link.  This forces Xcode to relink the targets from scratch.  It
# does not seem to check these dependencies itself.
PostBuild.sdk.Debug:
/Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/lib/Debug/libsdk.a:
	/bin/rm -f /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/lib/Debug/libsdk.a


PostBuild.sdk.Release:
/Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/lib/Release/libsdk.a:
	/bin/rm -f /Users/spencersharp/Documents/Coding/Active/audio-processing/vst/build/lib/Release/libsdk.a




# For each target create a dummy ruleso the target does not have to exist
