**PROJECT TITLE: SIMPLE: A compiler of Simple to Java Bytecodes

**GROUP: G27

(Names, numbers, self assessment, and contribution of
the members of the group to the project according to:)

NAME1: David Azevedo, NR1: 201405846, GRADE1: <0 to 20 value>, CONTRIBUTION1: <0 to 100 %>

NAME2: João Ferreira, NR2: 201404332, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>

NAME3: Marcelo Ferreira, NR3: 201405323, GRADE3: <0 to 20 value>, CONTRIBUTION3: <0 to 100 %>

(Note that the sum of the CONTRIBUTION? values must be 100 %)

** SUMMARY: (Describe what your tool does and its main features.)
The goal of this project was to develop a "mini compiler" which should be able to compile SIMPLE code to JVM instructions accepted by Jasmin in order to create a '.class' file with the result code. With that in mind, we had to implement the different fases that a regular compiler goes through, such as 'Lexical Analysis', 'Syntatic Analysis', 'Semantic Analysis', 'Intermidiate Represantation', 'Code Generation', 'Dataflow Analysis' and 'Register Alocation', which corresponds to the entire Compilers Course at FEUP. For that, we started by building a LL(1) grammar for the programming language SIMPLE, the basic grammar was given to us by the teachers, although with was our task to transform it into a LL(1) grammar.
 

** EXECUTE: (indicate how to run your tool)
To run our tool, you can specify the file which will be used to generate the bytecodes and you can also choose the number of registers(stack size) used.
java Simple2.java [-r=#registers] file.yal
The output should be a new file with the same name as the input file but with exstension .j eg: file.j


**DEALING WITH SYNTACTIC ERRORS: (Describe how the syntactic error recovery of your tool does work. Does it exit after the first error?)
JavaCC was used in our project so all of the syntactic analysis and error recovery was left by default for the JavaCC normal operation as the group felt it should focus more on building a powerfull and complete compiler, which gets the job done, the main reason being the lack of time to implement the project.
 

**SEMANTIC ANALYSIS: (Refer the possible semantic rules implemented by your tool.)
Our group implemented all of the semantic rules imposed by the SIMPLE language, passed onto us by the teachers.
 

**INTERMEDIATE REPRESENTATIONS (IRs): (for example, when applicable, briefly describe the HLIR (high-level IR) and the LLIR (low-level IR) used, if your tool includes an LLIR with structure different from the HLIR)
To better perform the 'Semantic Analysis' and the 'Code Generation' we generated a HLIR consisting on a AST, this intermediate representation made easy both of the fases explain in the begining, we also created, in parallel, a symbol table relative to the whole module. To achieve this, we opted to use the 'visitor' pattern which let's us traverse the abstract syntax tree with ease. Note : The design pattern 'visitor' was used a lot throughout our implementation due to it's ability to travel along the AST and 'visit' every node in a orderly fashion.
 

**CODE GENERATION: (when applicable, describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)
The Code Generation in our project makes use of two different classes. Firstly, a class that implements the 'visitor' pattern in order to visit all the nodes, and the second class, responsible for write JVM instructions to the output file. This last one represents a set of basics functions responsible to write small bits of code at a time, ie. load a variable, print a label, etc. In this part of the project we were very careful to choose low cost instructions and templates (for loops) that are optimized with a minimum number of conditional jumps. Aswell as differentiating small constants from bigger values so that we can choose either the 'bipush' or the 'iconst_' instruction.


**OVERVIEW: (refer the approach used in your tool, the main algorithms, the third-party tools and/or packages, etc.)
Left Edge Algorithm for time to live analysis. JavaCC.
 

**TESTSUITE AND TEST INFRASTRUCTURE: (Describe the content of your testsuite regarding the number of examples, the approach to automate the test, etc.)

 

**TASK DISTRIBUTION: (Identify the set of tasks done by each member of the project.)

 

**PROS: (Identify the most positive aspects of your tool)

 

**CONS: (Identify the most negative aspects of your tool)