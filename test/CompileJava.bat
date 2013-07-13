@echo off
if exist TestJava.class (
    del TestJava.class
)
if exist PhoneToWords.class (
	del PhoneToWords.class
)
if exist PhoneToWordsDB.class (
	del PhoneToWordsDB.class
)
@echo on

javac -d . TestJava.java ..\src\PhoneToWordsDB.java ..\src\PhoneToWords.java

pause