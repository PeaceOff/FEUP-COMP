echo "Removing Old Files" ; rm -rf ../output/* ; rm Simple2.jj ; rm Simple2.java ; echo JJTREE: ; jjtree Simple2.jjt ; echo JAVACC: ; javacc Simple2.jj ;  echo JAVAC: ; javac -d ../output *.java

