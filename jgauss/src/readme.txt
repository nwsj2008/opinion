This folder should contain all Gauss sources for the jgauss (and jgrte)
engine. 

All code must be defined in Gauss procedures of the form (here with 2 returns):
 

 /* 
  Some procedure for ...
   
  input:
    a - ...
    b - ...
    c - ...
  
  returns:
    ret1 - ...
    ret2 - ...
 */
 proc(2)=someProc_filename(a,b,c,...);
  local ret1, ret2;
 
  ...
  
  retp(ret1, ret2) ;
 endp;


Procedure names must be unique but this cannot be enforced
programmatically. Therefore, as a rule, use the suffix

          "_filename"


for each procedure name, where "filename" is a placeholder for the 
name of the respective Gauss source file. 

The files "jgauss.src" is required by the jgauss engine. The file "jgrte.src"
is used and should be adjusted for the jgrte engine.


Markus Kraetzig, 6 May 2005

