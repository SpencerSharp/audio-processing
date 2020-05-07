import shutil, shlex
from pathlib import Path
import subprocess, re

classpath = '/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jitter.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jode-1.1.2-pre-embedded.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/max.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/classes/:/Users/spencersharp/Documents/Max 8/Packages/CNMAT Externals/java-classes/:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/java-classes/classes/'
classpath = re.sub(' ','\\ ',classpath)

local = Path.cwd()

max_dir = local / 'mxj-java-classes'
src_dir = local / 'src'

# shutil.rmtree(max_dir)
shutil.move(src_dir,max_dir)

compile(max_dir)

def compile(fil):
    if fil.is_dir():
        return compile(fil)
    copied = max_dir / fil.name
    cmd = shlex.split('javac -d mxj-java-classes -classpath {} {}'.format(classpath, copied.relative_to(max_dir.parent.parent)))
    # print(cmd)
    print(fil.name)
    print("\n-------------------------\n")
    subprocess.call(cmd)
    print("\n-------------------------\n")