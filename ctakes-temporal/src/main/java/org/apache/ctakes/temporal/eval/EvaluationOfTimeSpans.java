/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ctakes.temporal.eval;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.ctakes.temporal.ae.ConstituencyBasedTimeAnnotator;
import org.apache.ctakes.typesystem.type.textsem.TimeMention;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.classifier.jar.JarClassifierBuilder;
import org.cleartk.classifier.libsvm.LIBSVMStringOutcomeDataWriter;
import org.cleartk.eval.AnnotationStatistics;
import org.uimafit.factory.AnalysisEngineFactory;

import com.lexicalscope.jewel.cli.CliFactory;

public class EvaluationOfTimeSpans extends EvaluationOfAnnotationSpans_ImplBase {

  public static void main(String[] args) throws Exception {
    Options options = CliFactory.parseArguments(Options.class, args);
    List<Integer> patientSets = options.getPatients().getList();
    List<Integer> trainItems = THYMEData.getTrainPatientSets(patientSets);
    List<Integer> devItems = THYMEData.getDevPatientSets(patientSets);
    EvaluationOfTimeSpans evaluation = new EvaluationOfTimeSpans(
        new File("target/eval/time-spans"),
        options.getRawTextDirectory(),
        options.getKnowtatorXMLDirectory(),
        options.getXMIDirectory(),
        options.getTreebankDirectory());
    evaluation.prepareXMIsFor(patientSets);
    evaluation.setLogging(Level.FINE, new File("target/eval/ctakes-time-errors.log"));
    AnnotationStatistics<String> stats = evaluation.trainAndTest(trainItems, devItems);
    System.err.println(stats);
  }

  public EvaluationOfTimeSpans(
      File baseDirectory,
      File rawTextDirectory,
      File knowtatorXMLDirectory,
      File xmiDirectory,
      File treebankDirectory) {
    super(baseDirectory, rawTextDirectory, knowtatorXMLDirectory, xmiDirectory, treebankDirectory, TimeMention.class);
  }

  @Override
  protected AnalysisEngineDescription getDataWriterDescription(File directory)
      throws ResourceInitializationException {
    return AnalysisEngineFactory.createAggregateDescription(
//        TimeAnnotator.createDataWriterDescription(LIBSVMStringOutcomeDataWriter.class, new File(directory, "/seq")),
        ConstituencyBasedTimeAnnotator.createDataWriterDescription(LIBSVMStringOutcomeDataWriter.class, new File(directory, "/tree")));
//    return ConstituencyBasedTimeAnnotator.createDataWriterDescription(LIBSVMStringOutcomeDataWriter.class, directory);
  }

  @Override
  protected void trainAndPackage(File directory) throws Exception {
//    JarClassifierBuilder.trainAndPackage(new File(directory, "/seq"), "-c", "10000");
    JarClassifierBuilder.trainAndPackage(new File(directory, "/tree"), "-c", "10000");
  }

  @Override
  protected AnalysisEngineDescription getAnnotatorDescription(File directory)
      throws ResourceInitializationException {
    return AnalysisEngineFactory.createAggregateDescription(
//        TimeAnnotator.createAnnotatorDescription(new File(directory, "/seq")),
//        AnalysisEngineFactory.createPrimitiveDescription(RemoveTreeAlignedMentions.class, RemoveTreeAlignedMentions.PARAM_GOLDVIEW_NAME, GOLD_VIEW_NAME),
        ConstituencyBasedTimeAnnotator.createAnnotatorDescription(new File(directory, "/tree")));
//    return ConstituencyBasedTimeAnnotator.createAnnotatorDescription(directory);
  }

  @Override
  protected Collection<? extends Annotation> getGoldAnnotations(JCas jCas) {
    return selectExact(jCas, TimeMention.class);
  }

  @Override
  protected Collection<? extends Annotation> getSystemAnnotations(JCas jCas) {
    return selectExact(jCas, TimeMention.class);
  }
}
