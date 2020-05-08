import shutil, shlex
from pathlib import Path
import subprocess, re, os

to_compile = []

classpath = '/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jitter.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jode-1.1.2-pre-embedded.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/max.jar:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/classes/:/Users/spencersharp/Documents/Max 8/Packages/CNMAT Externals/java-classes/:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/:/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/java-classes/classes/'
classpath = '''
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jitter.jar:
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/jode-1.1.2-pre-embedded.jar:
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/lib/max.jar:
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/classes/:
/Users/spencersharp/Documents/Max 8/Packages/CNMAT Externals/java-classes/:
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/packages/max-mxj/java-classes/:
/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/java-classes/classes/'''
classpath = re.sub('\n','',classpath)
classpath = re.sub(' ','\\ ',classpath)

def compile_dir(fil):
    for sub in fil.iterdir():
        # print(sub)
        if sub.is_dir():
            compile_dir(sub)
            continue
        cmd = shlex.split('javac -d mxj-java-classes -classpath {} {}'.format(classpath, sub.relative_to(max_dir.parent)))
        # print(cmd)
        if sub.name in to_compile:
            print(sub.name)
            # print(cmd)
            print("\n-------------------------\n")
            subprocess.call(cmd)
            print("\n-------------------------\n")

local = Path.cwd()

with open(local / 'to_build.txt','r') as incl:
    for line in incl:
        to_compile.append(line.rstrip())

sym = Path('/Applications/Ableton Live 10.1 Beta.app/Contents/App-Resources/Max/Max.app/Contents/Resources/C74/java-classes/classes/')
max_dir = local / 'mxj-java-classes'
src_dir = local / 'src'

shutil.rmtree(sym)
shutil.copytree(src_dir,sym)

max_dir.unlink()
max_dir.symlink_to(sym)

# os.chdir(max_dir)

compile_dir(max_dir)

