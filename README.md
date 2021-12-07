# Payment-Services
Steps to run the project:
1. Open the folder in a JAVA based IDE (IntelliJ/Eclipse)
2. Use Maven's lifecycle method 'package' to install the required packages for this project
3. Open Edit Configuration in the Run menu, select the '+' button and from the drop down select compound.
4. Add both applications, ClockServerRunner and LocalClockEvaluator to the compound using the '+' button
5. Select the compound in the Run configuration and hit the 'Play' button.
6. Make sure the ClockServerRunner starts first followed by LocalClockEvaluator. If this doesn't happen, in the Run tab of ClockServerRunner hit the Rerun button and then do the same for LocalClockEvaluator
7. Specify a filename for the output log in the program arguments for LocalClockEvaluator.
7. A file(with the specified filename) is generated for LocalClock, Cristian and NTP, which contains the error for each second.
