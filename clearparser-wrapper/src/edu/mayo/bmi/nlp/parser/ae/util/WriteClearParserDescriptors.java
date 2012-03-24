package edu.mayo.bmi.nlp.parser.ae.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;
import org.cleartk.util.Options_ImplBase;
import org.kohsuke.args4j.Option;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.xml.sax.SAXException;

import edu.mayo.bmi.nlp.parser.ae.ClearParserDependencyParserAE;
import edu.mayo.bmi.nlp.parser.ae.ClearParserSemanticRoleLabelerAE;


/**
 * Use this to generate UIMA xml descriptor files for the ClearParser analysis engines.
 *
 */
public class WriteClearParserDescriptors {
	public static final String SIMPLE_SEGMENTER_PATH = "../clinical documents pipeline/desc/analysis_engine/SimpleSegmentAnnotator.xml";
	public static final String TOKENIZER_PATH="../core/desc/analysis_engine/TokenizerAnnotator.xml";
	public static final String POS_TAGGER_PATH="../POS tagger/desc/POSTagger.xml";
	public static final String SENTENCE_DETECTOR_PATH="../core/desc/analysis_engine/SentenceDetectorAnnotator.xml";
	public static final String LVG_BASE_TOKEN_ANNOTATOR_PATH="desc/analysis_engine/LvgBaseTokenAnnotator.xml";
	public static final String DEP_NAME="ClearParserDependencyParser";
	public static final String SRL_NAME="ClearParserSRL";

	public static class Options extends Options_ImplBase {
		@Option(name = "-o",
				aliases = "--outputRoot",
				usage = "specify the directory to write out descriptor files",
				required = false)
		public File outputRoot = new File("desc/analysis_engine");

		@Option(name = "-m",
				aliases = "--modelFile",
				usage = "specify the path to the relation extractor model jar file",
				required = false)
		public File modelFile = new File("model.jar");
	}


	/**
	 * @param args
	 * @throws IOException 
	 * @throws UIMAException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws IOException, UIMAException, SAXException {
		Options options = new Options();
		options.parseOptions(args);

		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescriptionFromPath("../common-type-system/desc/common_type_system.xml");

		AnalysisEngineDescription clearparserSRLDesc = AnalysisEngineFactory.createPrimitiveDescription(
				ClearParserSemanticRoleLabelerAE.class,
				typeSystem
				/*,
				ClearParserSemanticRoleLabelerAE.PARAM_PARSER_MODEL_FILE_NAME,
				new File("/Users/lbecker/Development/workspace/SHARPn-cTAKES/clearparser-wrapper/resources/srl/dummy.srl.mod.jar").toString()
				*/
				);

		AnalysisEngineDescription clearparserDepParserDesc = AnalysisEngineFactory.createPrimitiveDescription(
				ClearParserDependencyParserAE.class,
				typeSystem
				/*
				ClearParserDependencyParserAE.PARAM_PARSER_MODEL_FILE_NAME,
				new File("/Users/lbecker/Development/workspace/SHARPn-cTAKES/clearparser-wrapper/resources/dependency/dummy.dep.mod.jar").toString()
				*/
				);

		System.out.println((new File("desc/analysis_engine")).getAbsolutePath());

		// Write standalone description files
		clearparserDepParserDesc.toXML(new FileOutputStream(new File(options.outputRoot, "ClearParserDependencyParserAE.xml")));
		clearparserSRLDesc.toXML(new FileOutputStream(new File(options.outputRoot, "ClearParserSemanticRoleLabelerAE.xml")));

		// Write aggregate plaintext description files
		AggregateBuilder aggregateBuilder = getPlaintextAggregateBuilder();
		writeAggregateDescriptions(aggregateBuilder, clearparserDepParserDesc, clearparserSRLDesc, options.outputRoot, "PlaintextAggregate.xml");

		aggregateBuilder = getTokenizedAggregateBuilder();
		writeAggregateDescriptions(aggregateBuilder, clearparserDepParserDesc, clearparserSRLDesc, options.outputRoot, "TokenizedAggregate.xml");

		aggregateBuilder = getTokenizedInfPosAggregateBuilder();
		writeAggregateDescriptions(aggregateBuilder, clearparserDepParserDesc, clearparserSRLDesc, options.outputRoot, "TokenizedInfPosAggregate.xml");

	}

	public static AggregateBuilder getPlaintextAggregateBuilder() throws InvalidXMLException, IOException {
		AggregateBuilder aggregateBuilder = new AggregateBuilder();
		aggregateBuilder.add(getDescription(SIMPLE_SEGMENTER_PATH));
		aggregateBuilder.add(getDescription(TOKENIZER_PATH));
		aggregateBuilder.add(getDescription(SENTENCE_DETECTOR_PATH));
		aggregateBuilder.add(getDescription(LVG_BASE_TOKEN_ANNOTATOR_PATH));
		aggregateBuilder.add(getDescription(POS_TAGGER_PATH));
		return aggregateBuilder;
	}


	public static AggregateBuilder getTokenizedAggregateBuilder() throws InvalidXMLException, IOException {
		AggregateBuilder aggregateBuilder = new AggregateBuilder();
		aggregateBuilder.add(getDescription(SIMPLE_SEGMENTER_PATH));
		aggregateBuilder.add(getDescription(LVG_BASE_TOKEN_ANNOTATOR_PATH));
		return aggregateBuilder;
	}

	public static AggregateBuilder getTokenizedInfPosAggregateBuilder() throws InvalidXMLException, IOException {
		AggregateBuilder aggregateBuilder = new AggregateBuilder();
		aggregateBuilder.add(getDescription(SIMPLE_SEGMENTER_PATH));
		aggregateBuilder.add(getDescription(LVG_BASE_TOKEN_ANNOTATOR_PATH));
		aggregateBuilder.add(getDescription(POS_TAGGER_PATH));
		return aggregateBuilder;
	}


	public static AnalysisEngineDescription getDescription(String pathToDescription) throws IOException, InvalidXMLException {
		File file = new File(pathToDescription);
		XMLParser parser = UIMAFramework.getXMLParser();
		XMLInputSource source = new XMLInputSource(file);
		AnalysisEngineDescription desc = parser.parseAnalysisEngineDescription(source);
		return desc;
	}
	
	private static void writeAggregateDescriptions(
			AggregateBuilder preprocessing, 
			AnalysisEngineDescription clearparserDepParserDesc, 
			AnalysisEngineDescription clearparserSRLDesc,
			File outputRoot,
			String aggregateSuffix) throws ResourceInitializationException, FileNotFoundException, SAXException, IOException {

		// Append Dependency Parser into aggregate and write description file
		preprocessing.add(clearparserDepParserDesc);
		preprocessing.createAggregateDescription().toXML(new FileOutputStream(new File(outputRoot, DEP_NAME + aggregateSuffix))); 
		// Append SRL Parser into aggregate and write description file
		preprocessing.add(clearparserSRLDesc);
		preprocessing.createAggregateDescription().toXML(new FileOutputStream(new File(outputRoot, SRL_NAME + aggregateSuffix))); 

	}


}
