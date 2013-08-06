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
package org.apache.ctakes.temporal.ae;

import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ctakes.core.ae.SHARPKnowtatorXMLReader;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.util.ViewURIUtil;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.AnalysisEngineFactory;

public class THYMEKnowtatorXMLReader extends SHARPKnowtatorXMLReader {

  public static final String PARAM_KNOWTATOR_XML_DIRECTORY = "knowtatorXMLDirectory";

  @ConfigurationParameter(name = PARAM_KNOWTATOR_XML_DIRECTORY, mandatory = true)
  protected File knowtatorXMLDirectory;

  public static AnalysisEngineDescription getDescription(File knowtatorXMLDirectory)
      throws ResourceInitializationException {
    return AnalysisEngineFactory.createPrimitiveDescription(
        THYMEKnowtatorXMLReader.class,
        THYMEKnowtatorXMLReader.PARAM_KNOWTATOR_XML_DIRECTORY,
        knowtatorXMLDirectory);
  }

  @Override
  protected URI getKnowtatorXML(JCas jCas) throws AnalysisEngineProcessException {
    URI uri = ViewURIUtil.getURI(jCas);
    File file = new File(uri.getPath());
    String subDir = file.getParentFile().getName();
    Matcher matcher = Pattern.compile("^doc(\\d+)$").matcher(subDir);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Unrecognized subdirectory naming: " + subDir);
    }
    subDir = String.format("Set%02d", Integer.parseInt(matcher.group(1)));
    String fileName = file.getName() + ".knowtator.xml";
    return new File(new File(this.knowtatorXMLDirectory, subDir), fileName).toURI();
  }

  @Override
  protected String[] getAnnotatorNames() {
    return new String[] { "consensus set annotator team", "consensus set_rel annotator team" };
  }

}