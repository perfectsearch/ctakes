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
package edu.mayo.bmi.fsm.token.adapter;

import edu.mayo.bmi.fsm.token.WordToken;
import edu.mayo.bmi.nlp.tokenizer.Token;

/**
 * Adapts a Context Free Tokenizer token into a Context Dependent Tokenizer
 * WordToken.
 * 
 * @author Mayo Clinic
 * 
 */
public class WordTokenAdapter extends BaseTokenAdapter implements WordToken {
	private Token iv_tok;

	public WordTokenAdapter(Token tok) {
		super(tok);
		iv_tok = tok;
	}

	public String getText() {
		return iv_tok.getText();
	}

	public byte getCaps() {
		byte caps = iv_tok.getCaps();
		switch (caps) {
		case Token.CAPS_ALL:
			return WordToken.CAPS_ALL;
		case Token.CAPS_FIRST_ONLY:
			return WordToken.CAPS_FIRST_ONLY;
		case Token.CAPS_MIXED:
			return WordToken.CAPS_MIXED;
		case Token.CAPS_NONE:
			return WordToken.CAPS_NONE;
		default:
			return WordToken.CAPS_UNKNOWN;
		}
	}

	public byte getNumPosition() {
		byte numPos = iv_tok.getNumPosition();
		switch (numPos) {
		case Token.NUM_FIRST:
			return WordToken.NUM_FIRST;
		case Token.NUM_LAST:
			return WordToken.NUM_LAST;
		case Token.NUM_MIDDLE:
			return WordToken.NUM_MIDDLE;
		default:
			return WordToken.NUM_NONE;
		}
	}
}