/*    Copyright 2011 Children's Hospital Boston
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
    
* @author Tim Miller
*/
package org.chboston.cnlp.ctakes.parser.uima.ae;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.chboston.cnlp.ctakes.parser.MaxentParserWrapper;
import org.chboston.cnlp.ctakes.parser.ParserWrapper;

import edu.mayo.bmi.uima.core.resource.FileLocator;

public class ConstituencyParser extends JCasAnnotator_ImplBase {

	ParserWrapper parser = null;
	Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		
		String modelFileOrDirname = (String) aContext.getConfigParameterValue("modelFilename");
		try {
			parser = new MaxentParserWrapper(FileLocator.locateFile(modelFileOrDirname).getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Error reading parser model file/directory: " + e.getMessage());
		}
	}


	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		parser.createAnnotations(jcas);
	}
}
