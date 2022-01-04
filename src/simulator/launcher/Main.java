package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 300;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static boolean _consoleMode = false;
	private static int ticks;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseViewOption(line);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			if (_consoleMode) parseOutFileOption(line);
			parseTicks(line);
			

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Mode of view (gui / console)").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks of the simulation").build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		//if (_inFile == null) {
		//	throw new ParseException("An events file is missing");
		//}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicks(CommandLine line) throws ParseException {
		String aux = line.getOptionValue("t");
		if(aux != null) {
			ticks = Integer.parseInt(aux);
		}else ticks =  _timeLimitDefaultValue;
	}
	
	private static void parseViewOption(CommandLine line) throws ParseException {
		String aux = line.getOptionValue("m");
		if (aux == null) throw new ParseException("A mode of view is missing -m gui or console");
		else if (aux.equalsIgnoreCase("console")) _consoleMode = true;
		else if (!aux.equalsIgnoreCase("gui")) throw new ParseException("Invalid mode of view! Only gui or console accepted");
	}

	private static void initFactories() {
		// FACTORIAS DE CAMBIO DE SEMAFORO
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		// FACTORIAS DE EXTRACCION DE COLA
		List<Builder<DequeuingStrategy>> dqbs= new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder());
		dqbs.add(new MoveAllStrategyBuilder());
		
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		// FACTORIAS DE EVENTOS
		List<Builder<Event>> factories = new ArrayList<>();
		factories.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
		factories.add(new NewCityRoadEventBuilder());
		factories.add(new NewInterCityRoadEventBuilder());
		factories.add(new NewVehicleEventBuilder());
		factories.add(new SetWeatherEventBuilder());
		factories.add (new SetContClassEventBuilder());
		
		
		_eventsFactory = new BuilderBasedFactory<>(factories); 

	}

	private static void startBatchMode() throws Exception {
		TrafficSimulator sim = new TrafficSimulator();
		Controller contl = new Controller(sim, _eventsFactory);
		
		if (_inFile == null) throw new ParseException ("ERROR: A input File is missing");
		InputStream in = new FileInputStream(_inFile);
		contl.loadEvents(in);
		
		OutputStream out = null;
		if(_outFile != null) {
			out = new FileOutputStream(_outFile);
		}
		
		
		contl.run(ticks, out);
	}
	
	private static void startGUIMode() throws Exception {
		TrafficSimulator sim = new TrafficSimulator();
		Controller contl = new Controller(sim, _eventsFactory);
		if (_inFile != null) contl.loadEvents(new FileInputStream(_inFile)); 
		
		SwingUtilities.invokeLater(new Runnable() {
		
			public void run() {
			new MainWindow(contl, _inFile);				// se pasa el archivo inicial para el boton Reset
			}
		});
		
		

	}

	private static void start(String[] args) throws Exception {
		initFactories();
		parseArgs(args);
		if (_consoleMode)startBatchMode();
		else startGUIMode();
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	



	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
