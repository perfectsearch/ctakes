/*
 * Copyright: (c) 2009   Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
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
package edu.mayo.bmi.uima.pos_tagger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

import edu.mayo.bmi.uima.pos_tagger.TagDictionaryCreator;

public class TagDictionaryCreatorTests {

	@Test
	public void testCreateTagDictionary() throws FileNotFoundException, IOException {
		BufferedReader input = new BufferedReader(new FileReader("test/data/unit-test-2lines-training-data.txt"));
		HashMap<String, Set<String>> tagDictionaryData = TagDictionaryCreator.createTagDictionary(input, true);
		
		Set<String> tags = tagDictionaryData.get("IL-2");
		assertEquals(3, tags.size());
		boolean nn = false;
		boolean cc = false;
		boolean in = false;
		for(String tag : tags) {
			if(tag.equals("NN"))
				nn = true;
			if(tag.equals("CC"))
				cc = true;
			if(tag.equals("IN"))
				in = true;
		}
		assertTrue(nn);
		assertTrue(cc);
		assertTrue(in);

		input = new BufferedReader(new FileReader("test/data/unit-test-2lines-training-data.txt"));
		tagDictionaryData = TagDictionaryCreator.createTagDictionary(input, false);
		
		tags = tagDictionaryData.get("il-2");
		assertEquals(3, tags.size());
		nn = false;
		cc = false;
		in = false;
		for(String tag : tags) {
			if(tag.equals("NN"))
				nn = true;
			if(tag.equals("CC"))
				cc = true;
			if(tag.equals("IN"))
				in = true;
		}
		assertTrue(nn);
		assertTrue(cc);
		assertTrue(in);

		tags = tagDictionaryData.get("surface");
		assertEquals("NN", tags.iterator().next());
		
	}
	
	
	@Test
	public void testMain() throws FileNotFoundException, IOException{
		String[] args = new String[] { "test/data/unit-test-2lines-training-data.txt", "test/data/output/unit-test-2lines-tag-dictionary.txt", "true"};
		TagDictionaryCreator.main(args);
		BufferedReader input = new BufferedReader(new FileReader("test/data/output/unit-test-2lines-tag-dictionary.txt"));
		String line;
		int lines = 0;
		while((line = input.readLine())!= null) {
			lines++;
			if(line.startsWith("IL-2 "))
				assertEquals("IL-2 CC IN NN", line);
			if(line.startsWith("requires "))
				assertEquals("requires VBZ", line);
		}
		assertEquals(36, lines);
	}
	
}