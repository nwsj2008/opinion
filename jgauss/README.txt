This folder contains the jgauss engine to run GAUSS 3.2 - GAUSS 6.0 together with
jstatcom. This engine is not required when the Gauss Runtime Engine / Gauss Engine is used instead.

To use the jgauss engine, a version of Gauss must be installed on the computer.

Configuration settings can be adjusted in "engine_config.xml". It is necessary to 
set the correct Gauss version and the path to the Gauss executable.

All Gauss code should go in the "src" sub directory. Native dlls should be put
in "dlib". Those can be called via the "dllcall" methods.

The jgauss engine supports two Gauss modes: library and gcg. The library mode is choosen
when an application is started with the -DEBUG="true" option. In this mode, Gauss 
loads .lcg libraries from its "lib" folder that can be generated before with the tool:

jgauss_lib.bat / lib.xml (holds library names and sourcefiles for library creation)

This mode allows to change the underlying Gauss sources in "src" on-the-fly if the method 
signatures do not change. This is very helpful for debugging, but also slower than
gcg mode.  

The gcg mode (if not in DEBUG mode, the default) uses a compiled version of all 
used Gauss sources. The name of that file is typically "jgauss.gcg", but could 
also be changed in "engine_config.xml". To generate that gcg file, the tool

jgauss_gcg.bat / compile.xml (holds names of all sourcefiles to compile)

can be used. This is also handy to compile the gcg file needed to run with the 
GRTE. For that it is required to have the Gauss Engine installed that matches the 
GRTE version to be used for shipping. The generated file "jgauss.gcg" must then 
be copied to the "jgrte" directory of the jgrte engine.

The jgauss engine comes with some file in the "src" folder that should not be changed.
"jgauss.src" is needed for the communication. "jgrte.src" is required only, when the 
generated gcg should be used with the GRTE engine later. The other files provide
helper methods for plotting that could be used by other Gauss procedures.

The sourcecode for the communications libraries of the jgauss engine is in 
the "sourcecode" directory of the jgauss_src package. It is only available for
Win32. 

