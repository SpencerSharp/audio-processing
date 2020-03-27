import shutil, shlex
from pathlib import Path
import subprocess, re

classpath = '/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jitter.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jode-1.1.2-pre-embedded.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/max.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/classes/:/Users/spencersharp/Documents/Max 8/Packages/CNMAT Externals/java-classes/:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/'
classpath = re.sub(' ','\\ ',classpath)

local = Path.cwd()

max_dir = local / 'mxj-java-classes'
src_dir = local / 'src'

# shutil.copytree(src_dir, max_dir, dirs_exist_ok=True)
for fil in max_dir.iterdir():
    fil.unlink()
for fil in src_dir.iterdir():
    if fil.stem == 'Demo' and fil.suffix == '.java':
        copied = max_dir / fil.name
        if copied.exists():
            copied.unlink()
        shutil.copy(fil, max_dir)
        cmd = shlex.split('javac -classpath {} {}'.format(classpath, copied.relative_to(max_dir.parent)))
        print(cmd)
        subprocess.call(cmd)
