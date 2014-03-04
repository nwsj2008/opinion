This is the resource directory for the jgrte engine that uses the
Gauss Runtime Engine (GRTE)/Gauss Engine. The GRTE can be shipped with
an application that uses Gauss. Users do not need to have an installed
Gauss anymore to execute precompiled Gauss code from a GCG
file. However, the developer needs to own a GRTE with a license for
shipping.

Configuration settings can be adjusted in "engine_config.xml".

DO_JGRTE_INIT: if "true" then the method "initJGRTE(graphicsPath,
               graphicsFileName, subDir)" from "jgrte.src" is called,
               only needed if graphics are to be displayed or if
               external dlls are called

GRAPHICS_DIR: if empty then temporary graphics files are stored in the
              jmulti install dir, on Linux/Unix this setting defaults
              to "/tmp", if the install dir is not writable on Win32,
              then it should point to a temporary directory but
              without whitespace characters (the jgrte engine
              complains if jmulti is installed in a directory with
              whitespaces and GRAPHICS_DIR is empty), this limitation
              is imposed by the GRTE

The compiled Gauss code should go to "jgrte.gcg" by default. It must
have been compiled with the same Gauss engine that it is shipped
with. It does not work if it was compiled with a "normal" Gauss
version.

The license file for the GRTE must go to the "flexlm"
subdirectory. Without the license file the GRTE does not execute any
procedures but gives an error.

The "sourcecode" directory of the jgrte_src package holds the sources
for compiling the dll "jgrte". This library handles graphics viewing on
win32. 


GCG Compilation on Win32
------------------------

For the compilation of the Gauss sources, the jgauss engine can (but
does not have to) be used with the batch "jgauss_gcg.bat". The file is
created in the "jgauss" directory and can be copied to the "jgrte"
directory and renamed to "jgrte.gcg". However, this does only work
when the Gauss Engine for the GRTE was used for compilation, otherwise
the GRTE will not execute any code from that GCG file.

The compiled GCG file should contain the code from "jgrte.src" to load
any dlls at the beginning and to initialize graphics handling (if
DO_JGRTE_INIT is "true").  The dll "jgrte.dll" is loaded by
default. This dll is used by the command

"dllcall showLastGraphic;"

which must be called after every Gauss graphics call (xy, bar, xyz) to
display the graphics viewer.  If other dlls are needed, then they
should also be included in the single "dlibrary" command in
"jgrte.src" and should be put in the "dlib" subdirectory.

If no graphics are used and no extra dlls are called, then
DO_JGRTE_INIT should be set to "false" and "jgrte.src" is not needed.

On Win32 it is convenient to use the jgauss engine first for testing
and debugging (especially in DEBUG mode). When the developer owns a
Gauss Engine with a GRTE license, then the Gauss code can be compiled
and shipped with that GRTE. The Gauss sourcecode does not need to be
changed whether it runs with the jgrte or the jgauss engine. It can
even be compiled on other operating systems with the respective Gauss
Engine for that OS, and it can then be shipped with a matching GRTE.
It is important to note that the GCG files have to be compiled for
every operating system.

GCG Compilation on Linux
------------------------

For other platforms where the jgauss engine is not available, the
compilation must be run manually.  In the linux distribution the
template file "compile.src" is provided. It should be called from
within the Gauss Engine with:

"compile compile.src jgrte.gcg"

Then "jgrte.gcg" must be copied to the "jgrte"
directory. "compile.src" must include all relevant Gauss
files. Required libraries can be loaded before. They will be compiled
into the GCG file as well, for example "optmum" or other
extensions. Of course, this procedure works on Win32 as well if the
jgauss engine is not used.
 

Other Platforms
---------------

The GRTE is available for a number of platforms and in principle all
of these can also be run with jstatcom. However, there is a JNI
wrapper required, which is in "gaussapi.jar" (jstatcom base
package) and the native library "gaussjavaapirt". The native
library must be compiled for the respective platform as well.
However, the code for this is not Open-Source but it could be done on
request.




