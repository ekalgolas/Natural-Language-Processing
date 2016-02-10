Instructions to Execute the Program as a JAR file:
1) The Program can be executed using a runnable jar "ComputeBigrams.jar", copy it into your directory.
2) Copy NLPCorpusTreebank2Parts.txt into your directory
3) Open Command Prompt/Putty and navigate to the folder where the jar file is located
4) Run the jar file
   We need to supply input to the program, and we give it as->java -jar ComputeBigrams.jar -file PATH_TO_CORPUS
   Run using the command
     -> java -jar ComputeBigrams.jar -file NLPCorpusTreebank2Parts.txt
   If the location of the cranfield documents is different, please specify it here.

5) If the sentences are different, supply input as java -jar ComputeBigrams.jar -file PATH_TO_CORPUS -s1 SENTENCE1 -s2 SENTENCE2
   Example:
   java -jar ComputeBigrams.jar -file NLPCorpusTreebank2Parts.txt -s1 "This is a test sentence" -s2 "This is another test sentence"

Instructions to Execute the Program with java files:
1) Copy all the java files in Assigment2/src into your directory
2) Copy all the JAR files in Assigment2/libs into your directory
3) Copy NLPCorpusTreebank2Parts.txt into your directory
4) Open Command Prompt/Putty and navigate to the folder where the above files are located
5) Run the command ->
   javac -cp D:/Github/Natural-Language-Processing/Assignment2/libs/commons-cli-1.3.1.jar;D:/Github/Natural-Language-Processing/Assignment2/libs/commons-lang3-3.4.jar *.java

6) Run the program
   We need to supply input to the program, and we give it as->java -cp D:/Github/Natural-Language-Processing/Assignment2/libs/commons-cli-1.3.1.jar;D:/Github/Natural-Language-Processing/Assignment2/libs/commons-lang3-3.4.jar;. ComputeBigrams -file PATH_TO_CORPUS
   Run using the command
     -> java -cp D:/Github/Natural-Language-Processing/Assignment2/libs/commons-cli-1.3.1.jar;D:/Github/Natural-Language-Processing/Assignment2/libs/commons-lang3-3.4.jar;. ComputeBigrams -file NLPCorpusTreebank2Parts.txt
   If the location of the cranfield documents is different, please specify it here.

7) If the sentences are different, supply input as java -jar ComputeBigrams.jar -file PATH_TO_CORPUS -s1 SENTENCE1 -s2 SENTENCE2
   Example:
   java -cp D:/Github/Natural-Language-Processing/Assignment2/libs/commons-cli-1.3.1.jar;D:/Github/Natural-Language-Processing/Assignment2/libs/commons-lang3-3.4.jar;. ComputeBigrams -file NLPCorpusTreebank2Parts.txt -s1 "This is a test sentence" -s2 "This is another test sentence"
