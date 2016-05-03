Instructions to Execute the Program as a JAR file:
1) The Program can be executed using a runnable jar "CKYParser.jar", copy it into your directory.
2) Copy all the .pcfg files from the data folder into your directory
3) Open Command Prompt/Putty and navigate to the folder where the jar file is located
4) Run the jar file
   We need to supply input to the program, and we give it as->java -jar CKYParser.jar -pcfg PATH_TO_GRAMMAR_FILE -text SENTENCE
   Run using the command
     -> java -jar CKYParser.jar -pcfg "grammar.pcfg" -text "This is a test sentence"
5) For bigger grammar files, allow 15-20 minutes for the program to run.
