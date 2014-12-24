package de.fau.pi1.timerReporter.api.usage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import de.fau.pi1.timerReporter.dataset.Dataset;
import de.fau.pi1.timerReporter.evaluation.StatisticEvaluation;
import de.fau.pi1.timerReporter.plots.PlotPool;
import de.fau.pi1.timerReporter.reader.ReaderCsv;
import de.fau.pi1.timerReporter.tools.Conf;
import de.fau.pi1.timerReporter.tools.FileId;
import de.fau.pi1.timerReporter.tools.Folder;
import de.fau.pi1.timerReporter.writer.WritePDF;



public class Main {

	/**
	 * This main class should describe how the fau-timer reporter works as library.
	 * 
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args){
		
		Conf.getInstance();
		ArrayList<String> inputFiles = new ArrayList<String>();
		
		for(int i = 1000000000; i >= 1; i /= 10) {
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_10.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_20.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_40.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_80.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_160.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_320.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_640.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_1280.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_2560.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_5120.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_10240.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_20480.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_40960.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_81920.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_163840.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_3276680.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_6553360.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_13106720.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_26213440.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_52426880.csv");
			inputFiles.add("/Users/basti/Dropbox/29c3/delay_" + i + "/results_104853760.csv");
		}
		
		for (String inputFile : inputFiles) {

			Conf.put("inputFile", inputFile);
			Conf.put("gnuplot", "/opt/local/bin/gnuplot");
			Conf.put("makeindexPath", "/opt/local/bin/makeindex");
			Conf.put("pdflatex", "/opt/local/bin/pdflatex");
			
			
			System.out.println("############# -->" + inputFile);

			// create new data set with secrets and times
			ReaderCsv reader = new ReaderCsv();
			Dataset dataset = new Dataset(reader);
			dataset.setName("New Measurement");
			String report = de.fau.pi1.timerReporter.main.Main.getReport();

			// create plot pool to multi threaded the plots
			PlotPool plotPool = new PlotPool(report, dataset);

			// plot the data set with the lower bound of 0.0 and the upper bound of 1.0
			// plotPool.plot("Unfiltered Measurements", 0.0, 1.0);

			// plot the data set with the user input lower bound and upper bound
			// plotPool.plot("Filtered Measurments: User Input", new Double(0.2), new Double(0.3));

			// build the evaluation phase
			StatisticEvaluation statisticEvaluation = new StatisticEvaluation(dataset, plotPool);
			
			/*
			 * this part shows how an optimal box is set by a user
			 */
			/*double[] userInputOptimalBox = new double[2];
			userInputOptimalBox[0] = new Double(0.19);
			userInputOptimalBox[1] = new Double(0.21);
			statisticEvaluation.setOptimalBox(userInputOptimalBox);*/
			
			// Manually set the minimum amount of measurements
			// statisticEvaluation.onlyValidationPhase(1000);
			
			// Automatically determine minimum amount of measurements
			statisticEvaluation.calibrationPhase();
			
			// print the box test results into a csv file
			statisticEvaluation.printBoxTestResults(new File(report + Folder.getFileSep() + FileId.getId() +  "-" + "BoxTestResult.csv"));

			/*
			 * this part shows the getter of the evaluation results
			 * 
			for (BoxTestResults result : statisticEvaluation.getBoxTestResults()) {
				System.out.println(result.getInputFile() + " [" + result.getSecretA().getName() + "<" + result.getSecretB().getName() + "]: " + result.getOptimalBox()[0] + "-" + result.getOptimalBox()[1]);

				// iterate above all tested smallest sizes
				for(int i = 0; i < result.getSmallestSize().size(); ++i) {
					System.out.println("Minimum amouont of measurements: " + result.getSmallestSize().get(i) + "\nConfidence Interval: " + result.getConfidenceInterval().get(i));
					System.out.println("Subset A overlaps: " + Folder.convertArrayListToString(result.getSubsetOverlapA().get(i)));
					System.out.println("Subset B overlaps: " + Folder.convertArrayListToString(result.getSubsetOverlapB().get(i)));
					System.out.println("Subset A and B significant different: " + Folder.convertArrayListToString(result.getSignificantDifferent().get(i)));

				}
			}
 			*/
			
			// store the time lines
			ArrayList<String> timelineNames = statisticEvaluation.storeTimelines(report + Folder.getFileSep() + "images" + Folder.getFileSep());

			// close the thread pool
			plotPool.close();

			// write results in html and pdf
			// new WriteHTML(dataset, report, plotPool).write();

			try {
				new WritePDF(dataset, report, plotPool, timelineNames).write();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error while writing the pdf.");
			}

			// delete the folder tmp
			Folder.deleteTmp();
		}
	}
}