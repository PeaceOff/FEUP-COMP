**PROJECT TITLE: SIMPLE: A compiler of Simple to Java Bytecodes

**GROUP: G27

(Names, numbers, self assessment, and contribution of
the members of the group to the project according to:)

NAME1: David Azevedo, NR1: 201405846, GRADE1: <0 to 20 value>, CONTRIBUTION1: 33,3%

NAME2: João Ferreira, NR2: 201404332, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>

NAME3: Marcelo Ferreira, NR3: 201405323, GRADE3: <0 to 20 value>, CONTRIBUTION3: <0 to 100 %>

(Note that the sum of the CONTRIBUTION? values must be 100 %)

** SUMMARY: (Describe what your tool does and its main features.)
The goal of this project was to develop a "mini compiler" which should be able to compile SIMPLE code to JVM instructions accepted by Jasmin in order to create a '.class' file with the result code. With that in mind, we had to implement the different fases that a regular compiler goes through, such as 'Lexical Analysis', 'Syntatic Analysis', 'Semantic Analysis', 'Intermidiate Represantation', 'Code Generation', 'Dataflow Analysis' and 'Register Alocation', which corresponds to the entire Compilers Course at FEUP. For that, we started by building a LL(1) grammar for the programming language SIMPLE, the basic grammar was given to us by the teachers, although it was our task to transform it into a LL(1) grammar. Not only does the project compile the code, but it also gives warnings and errors to it's users that a normal, considered good, compiler would.
 

** EXECUTE: (indicate how to run your tool)
To run our tool, after compiling the source code, you can specify the file which will be used to generate the bytecodes and you can also choose the number of registers(stack size) used.
java Simple2.java [-r=#registers] file.yal
The output should be a new file with the same name as the input file but with exstension .j eg: file.j.
Until this point is what our compiler was supposed to do, now to test it, you should compile the jasmine file into a .class file, in order to do that, first you must have the jasmin.jar, second, run the command 'java -jar [jasmin.jar relative path] [file].j' this command will generate the '[file].class' which can be run with the java command, like so 'java [file]'.


**DEALING WITH SYNTACTIC ERRORS: (Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)
JavaCC was used in our project so all of the syntactic analysis and error recovery was left by default for the JavaCC normal operation as the group felt it should focus more on building a powerfull and complete compiler, which gets the job done, the main reason being the lack of time to implement the project.
 

**SEMANTIC ANALYSIS: (Refer the possible semantic rules implemented by your tool.)
Our group implemented all of the semantic rules imposed by the SIMPLE language, passed onto us by the teachers.
 

**INTERMEDIATE REPRESENTATIONS (IRs): (for example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)
To better perform the 'Semantic Analysis', the 'Code Generation' and the construct of the Symbol Table we generated a HLIR consisting on a AST, this intermediate representation made easy the fases explain in the begining, we also created, in parallel, a symbol table relative to the whole module. To achieve this, we opted to use the 'visitor' pattern which let's us traverse the abstract syntax tree with ease. Note : The design pattern 'visitor' was used a lot throughout our implementation due to it's ability to travel along the AST and 'visit' every node in a orderly fashion.
 

**CODE GENERATION: (when applicable, describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)
The Code Generation in our project makes use of two different classes. Firstly, a class that implements the 'visitor' pattern in order to visit all the nodes, and the second class, responsible for writing JVM instructions to the output file. This last one represents a set of basics functions responsible to write small bits of code at a time, ie. load a variable, print a label, etc. In this part of the project we were very careful to choose low cost instructions and templates (for loops) that are optimized with a minimum number of conditional jumps. Aswell as differentiating small constants from bigger values so that we can choose either the 'bipush' or the 'iconst_' instruction.


**OVERVIEW: (refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)
The main tool used in this project was JavaCC for the syntatic analysis aswell as a builder for the AST, in particular with the jjtree extension. The approach used was a waterfall development module as indicated in the "Description of the yal2jvm project [English version]" file that can be found in the moodle page for the compiler's class. In terms of algorithms we used the Left Edge Algorithm presented in the theoretical classes, it was used to alocate the program variables into a minimum number of registers with out collisions.
 

**TESTSUITE AND TEST INFRASTRUCTURE: (Describe the content of your testsuite regarding the number of examples, the approach to automate the test, etc.)
To test the work developed, the group made use of the testsuit available specific to this project in the moddle page, our code passed in all tests provided and identified the correct errors on the files made with that obvjetive. We not only tests with the teachers files, but also made some testes of our own to better exploit and test what was made directly in our implementation. Such as, the verification of the function call parameters, warnings about unused parameters in functions, construction of the symbol table and the variables live range.
With the tests provided being very in depth and covering many aspects of the project, we made only 3 tests of our own which complete the previous one's even further. To automate testing, we created 2 scripts one to completly compile the project (jjtree, javacc, javac) and another one to test a specific file , we would feed the script the name of the file and it would run our compiler with it.
 

**TASK DISTRIBUTION: (Identify the set of tasks done by each member of the project.)
David Azevedo : Conversion of the grammar to a LL(1) format. Contribuition to the construction of the AST. Responsible for the checking the function call parameters with the respective arguments. Responsible for the loops and if's in the code generation. Testing and debugging the project. Brainstorming for the liveness analysis implementation aswell as fixing bugging related to it.
João Ferreira :
Marcelo Ferreira :

**PROS: (Identify the most positive aspects of your tool)
Our tool's positive aspects include : LL(1) Grammar, a very good semantic analysis and error detection, automatic otimizations in terms of code generation, use of optimized 'templates' for conditional statements and loops, construction of a very helpful symbol table aswell as printing it for debugging, good register allocation algorithm.

**CONS: (Identify the most negative aspects of your tool)
Although we believe our implementation to be very good in terms of what was asked of us, it could still be better in some aspects, which the group considers as future improvements that were not implemented due to this semester's timetable being very strict, and the work load was imense from all the classes. That being said, the more negative aspects are : not stoping the syntatic analysis at the first error, not translating the language to portuguese, this idea was dropped has the teachers would not evalute this and we wanted to be in compliance with the specification first and leave secondary enhancements such as this to last (if we had the time). Finally, we believe that the code otimization could have been a little more strong than it is at the moment.