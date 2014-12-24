This is the code that we did the evaluation of the datasets with. The
main part is in ReportingTool.jar. The source code for it is at 
https://code.google.com/p/fau-timer/

The class src/de/fau/pi1/timerReporter/api/usage/Main.java calls the
functionality of the reporting tool. Adjust the values in there so
that they match your configuration. Then run the class to analyze the
datasets.

After you adjusted the values, you can run the script using this call:
$ java -cp .:bin:ReportingTool.jar de/fau/pi1/timerReporter/api/usage/Main
