Instructions to Execute the Program as a JAR file:
1) The Program can be executed using a runnable jar "Lesk.jar", copy it into your directory.
2) Copy file "stopwords" and folder "dict" from the data folder into your directory
3) Open Command Prompt/Putty and navigate to the folder where the jar file is located
4) Run the jar file
   We need to supply input to the program, and we give it as->java -jar -Dwordnet.database.dir=PATH_TO_DICTIONARY Lesk.jar -stop PATH_TO_STOPWORDS
   Run using the command
     -> java -jar -Dwordnet.database.dir=dict/ Lesk.jar -stop stopwords

5) If the sentence is different, supply input as java -jar -Dwordnet.database.dir=PATH_TO_DICTIONARY Lesk.jar -stop PATH_TO_STOPWORDS -text SENTENCE
   Example:
   java -jar -Dwordnet.database.dir=dict/ Lesk.jar -text stopwords -text "This is a test sentence"

Instructions to Execute the Program with java files:
1) Copy all the java files in Homework4/src into your directory
2) Copy all the JAR files in Homework4/libs into your directory
3) Copy file "stopwords" and folder "dict" from the data folder into your directory
4) Open Command Prompt/Putty and navigate to the folder where the above files are located
5) Run the command ->
   javac -cp commons-cli-1.3.1.jar:jaws-bin.jar:. *.java

6) Run the program
   We need to supply input to the program, and we give it as->java -cp commons-cli-1.3.1.jar:jaws-bin.jar:. Lesk -stop PATH_TO_STOPWORDS
   Run using the command
     -> java -cp commons-cli-1.3.1.jar:jaws-bin.jar:. -Dwordnet.database.dir=dict/ Lesk -stop stopwords

7) If the sentence is different, supply input as java -cp commons-cli-1.3.1.jar:jaws-bin.jar:. -Dwordnet.database.dir=PATH_TO_DICTIONARY Lesk -stop PATH_TO_STOPWORDS -text SENTENCE
   Example:
   java -cp commons-cli-1.3.1.jar:jaws-bin.jar:. -Dwordnet.database.dir=dict/ Lesk -text stopwords -text "This is a test sentence"