/*
 * Copyright: (c) 2012   The MITRE Corporation. All rights reserved.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MITRE  as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package org.mitre.medfacts.uima.assertion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.mitre.jcarafe.jarafe.JarafeMEDecoder;
import org.mitre.medfacts.i2b2.annotation.ConceptType;
import org.mitre.medfacts.i2b2.annotation.ScopeParser;
import org.mitre.medfacts.i2b2.api.ApiConcept;
import org.mitre.medfacts.i2b2.api.AssertionDecoderConfiguration;
import org.mitre.medfacts.i2b2.api.SingleDocumentProcessor;
import org.mitre.medfacts.i2b2.cli.BatchRunner;
import org.mitre.medfacts.i2b2.util.StringHandling;
import org.mitre.medfacts.type.Assertion;
import org.mitre.medfacts.type.Concept;
import org.mitre.medfacts.type.Concept_Type;
import org.mitre.medfacts.zoner.LineTokenToCharacterOffsetConverter;

import edu.mayo.bmi.uima.core.type.refsem.OntologyConcept;
import edu.mayo.bmi.uima.core.type.refsem.UmlsConcept;
import edu.mayo.bmi.uima.core.type.textsem.EntityMention;
import edu.mayo.bmi.uima.core.type.textsem.EventMention;
import edu.mayo.bmi.uima.core.type.textsem.IdentifiedAnnotation;

public class ConceptConverterAnalysisEngine extends JCasAnnotator_ImplBase
{
  public static final Logger logger = Logger
      .getLogger(ConceptConverterAnalysisEngine.class.getName());

  public ConceptConverterAnalysisEngine()
  {
  }

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException
  {
    logger.info("beginning of ConceptConverterAnalysisEngine.process()");
    String contents = jcas.getDocumentText();

    processForEntityType(jcas, EntityMention.type, EntityMention.class);

    processForEntityType(jcas, EventMention.type, EventMention.class);

    logger.info("end of ConceptConverterAnalysisEngine.process()");
  }

  public void processForEntityType(JCas jcas, int annotationType, Class<? extends IdentifiedAnnotation> annotationClass)
  {
    AnnotationIndex<Annotation> annotationIndex = jcas
        .getAnnotationIndex(annotationType);

    int totalAnnotationCount = jcas.getAnnotationIndex().size();
    int typeSpecificAnnotationCount = annotationIndex.size();

    logger.info(String.format("    total annotation count %d",
        totalAnnotationCount));
    logger.info(String.format("    %s annotation count %d",
        annotationClass.getName(),
        typeSpecificAnnotationCount));

    //logger.info("    before iterating over named entities...");
    for (FeatureStructure featureStructure : annotationIndex)
    {
      //logger.info("    begin single named entity");
      IdentifiedAnnotation annotation = (IdentifiedAnnotation) featureStructure;

      int begin = annotation.getBegin();
      int end = annotation.getEnd();
      String conceptText = annotation.getCoveredText();

      //logger.info(String.format("NAMED ENTITY: \"%s\" [%d-%d]", conceptText,
      //    begin, end));

      Concept concept = new Concept(jcas, begin, end);
      concept.setConceptText(conceptText);
      concept.setConceptType(null);

      concept.setOriginalEntityExternalId(annotation.getAddress());

      FSArray ontologyConceptArray = annotation
          .getOntologyConceptArr();

      ConceptType conceptType = ConceptLookup
          .lookupConceptType(ontologyConceptArray);

      //logger.info(String.format("got concept type: %s", conceptType));

      // now always generating a concept annotation whether or not the
      // conceptType is null (previously, we only generated a concept
      // annotation if the conceptType was not null)
      if (conceptType != null)
      {
        concept.setConceptType(conceptType.toString());
      }
      concept.addToIndexes();

      //logger.info("finished adding new Concept annotation. " + concept);

    }
    //logger.info("    after iterating over named entities.");
  }

}
