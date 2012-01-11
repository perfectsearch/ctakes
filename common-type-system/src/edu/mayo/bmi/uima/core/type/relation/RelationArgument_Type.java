
/* First created by JCasGen Wed Jan 11 14:37:37 EST 2012 */
package edu.mayo.bmi.uima.core.type.relation;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Wed Jan 11 14:37:37 EST 2012
 * @generated */
public class RelationArgument_Type extends TOP_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (RelationArgument_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = RelationArgument_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new RelationArgument(addr, RelationArgument_Type.this);
  			   RelationArgument_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new RelationArgument(addr, RelationArgument_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = RelationArgument.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.mayo.bmi.uima.core.type.relation.RelationArgument");
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated */ 
  public int getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    return ll_cas.ll_getIntValue(addr, casFeatCode_id);
  }
  /** @generated */    
  public void setId(int addr, int v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    ll_cas.ll_setIntValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_argument;
  /** @generated */
  final int     casFeatCode_argument;
  /** @generated */ 
  public int getArgument(int addr) {
        if (featOkTst && casFeat_argument == null)
      jcas.throwFeatMissing("argument", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    return ll_cas.ll_getRefValue(addr, casFeatCode_argument);
  }
  /** @generated */    
  public void setArgument(int addr, int v) {
        if (featOkTst && casFeat_argument == null)
      jcas.throwFeatMissing("argument", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    ll_cas.ll_setRefValue(addr, casFeatCode_argument, v);}
    
  
 
  /** @generated */
  final Feature casFeat_role;
  /** @generated */
  final int     casFeatCode_role;
  /** @generated */ 
  public String getRole(int addr) {
        if (featOkTst && casFeat_role == null)
      jcas.throwFeatMissing("role", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    return ll_cas.ll_getStringValue(addr, casFeatCode_role);
  }
  /** @generated */    
  public void setRole(int addr, String v) {
        if (featOkTst && casFeat_role == null)
      jcas.throwFeatMissing("role", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    ll_cas.ll_setStringValue(addr, casFeatCode_role, v);}
    
  
 
  /** @generated */
  final Feature casFeat_participatesIn;
  /** @generated */
  final int     casFeatCode_participatesIn;
  /** @generated */ 
  public int getParticipatesIn(int addr) {
        if (featOkTst && casFeat_participatesIn == null)
      jcas.throwFeatMissing("participatesIn", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    return ll_cas.ll_getRefValue(addr, casFeatCode_participatesIn);
  }
  /** @generated */    
  public void setParticipatesIn(int addr, int v) {
        if (featOkTst && casFeat_participatesIn == null)
      jcas.throwFeatMissing("participatesIn", "edu.mayo.bmi.uima.core.type.relation.RelationArgument");
    ll_cas.ll_setRefValue(addr, casFeatCode_participatesIn, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public RelationArgument_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.Integer", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_argument = jcas.getRequiredFeatureDE(casType, "argument", "uima.tcas.Annotation", featOkTst);
    casFeatCode_argument  = (null == casFeat_argument) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_argument).getCode();

 
    casFeat_role = jcas.getRequiredFeatureDE(casType, "role", "uima.cas.String", featOkTst);
    casFeatCode_role  = (null == casFeat_role) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_role).getCode();

 
    casFeat_participatesIn = jcas.getRequiredFeatureDE(casType, "participatesIn", "uima.cas.FSList", featOkTst);
    casFeatCode_participatesIn  = (null == casFeat_participatesIn) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_participatesIn).getCode();

  }
}



    