package subobjectjava.model.component;

import java.util.ArrayList;
import java.util.List;

import jnome.core.expression.invocation.ConstructorInvocation;
import jnome.core.type.AnonymousInnerClass;

import org.rejuse.association.Association;
import org.rejuse.association.SingleAssociation;
import org.rejuse.logic.ternary.Ternary;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationContainerSkipper;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategySelector;
import chameleon.core.member.Member;
import chameleon.core.member.MemberImpl;
import chameleon.core.statement.Block;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.ClassBody;
import chameleon.oo.type.DeclarationWithType;
import chameleon.oo.type.RegularType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.TypeWithBody;
import chameleon.util.Util;

public class ComponentRelation extends MemberImpl<ComponentRelation,Element,SimpleNameSignature, ComponentRelation> implements DeclarationWithType<ComponentRelation,Element,SimpleNameSignature, ComponentRelation>, Definition<ComponentRelation,Element,SimpleNameSignature, ComponentRelation>{

	public ComponentRelation(SimpleNameSignature signature, TypeReference type) {
		setSignature(signature);
		setComponentType(type);
	}
	
	@Override
	public ComponentRelation clone() {
		ComponentRelation result = new ComponentRelation(signature().clone(), componentTypeReference().clone());
		ConfigurationBlock configurationBlock = configurationBlock();
		if(configurationBlock != null) {
		  result.setConfigurationBlock(configurationBlock.clone());
		}
		ComponentType t = componentTypeDeclaration();
		if(t != null) {
			result.setComponentTypeDeclaration(t.clone());
		}
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		LookupStrategy result = parent().lexicalLookupStrategy(this);
		result = new ComponentTypeLookupStrategy(result, nearestAncestor(Type.class));
//    if(child.nearestAncestor(ClassBody.class).sameAs(body())) {
//    	LookupStrategy componentStrategy = componentType().localStrategy();
//    	final LookupStrategy lexical = result;
//    	result = language().lookupFactory().createLexicalLookupStrategy(componentStrategy, this, new LookupStrategySelector(){
//				public LookupStrategy strategy() throws LookupException {
//					return lexical;
//				}
//			});
//    }
		return result;
	}

  //PAPER: customize lookup
	public static class ComponentTypeLookupStrategy extends LookupStrategy {

		public ComponentTypeLookupStrategy(LookupStrategy parentStrategy, Type type) {
			_parentStrategy = parentStrategy;
			_type = type;
		}
		
		private Type _type;

		@Override
		public <D extends Declaration> D lookUp(DeclarationSelector<D> selector) throws LookupException {
			return _parentStrategy.lookUp(new DeclarationContainerSkipper<D>(selector, _type));
		}
		
		private LookupStrategy _parentStrategy;
		
	}
	
	public List<? extends Member> getIntroducedMembers() throws LookupException {
		List<Member> result = new ArrayList<Member>();
		result.add(this);
//		List<Member> superMembers = componentType().members();
		ConfigurationBlock configurationBlock = configurationBlock();
		if(configurationBlock != null) {
		  result.addAll(configurationBlock.processedMembers(componentType()));
		}
		return result;
//		return declaredMembers();
	}
	
	@Override
	public List<? extends Member> declaredMembers() {
		return Util.<Member>createSingletonList(this);
	}
	
	private SingleAssociation<ComponentRelation,TypeReference> _typeReference = new SingleAssociation<ComponentRelation,TypeReference>(this);

	public TypeReference componentTypeReference() {
		return _typeReference.getOtherEnd();
	}
	
	public Type referencedComponentType() throws LookupException {
		return componentTypeReference().getElement();
	}
	
	public Type componentType() throws LookupException {
		Type result = componentTypeDeclaration();
		if(result == null) {
		 result = componentTypeReference().getElement();
		}
		return result;
	}

	public void setComponentType(TypeReference type) {
		if(type != null) {
			_typeReference.connectTo(type.parentLink());
		}
		else {
			_typeReference.connectTo(null);
		}
	}
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}

	
  public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature) {
  			_signature.connectTo(signature.parentLink());
  	} else if(signature == null) {
			_signature.connectTo(null);
		} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected SimpleNameSignature");
  	}
  }
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature() {
    return _signature.getOtherEnd();
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public List<Element> children() {
  	List<Element> result = super.children();
  	Util.addNonNull(signature(), result);
  	Util.addNonNull(componentTypeReference(), result);
  	Util.addNonNull(configurationBlock(), result);
  	Util.addNonNull(componentTypeDeclaration(), result);
  	return result;
  }
  
  private SingleAssociation<ComponentRelation, SimpleNameSignature> _signature = new SingleAssociation<ComponentRelation, SimpleNameSignature>(this);


  public void setConfigurationBlock(ConfigurationBlock configurationBlock) {
  	setAsParent(_configurationBlock, configurationBlock);
  }
  
  /**
   * Return the ConfigurationBlock of this member.
   */
  public ConfigurationBlock configurationBlock() {
    return _configurationBlock.getOtherEnd();
  }
  
  private SingleAssociation<ComponentRelation, ConfigurationBlock> _configurationBlock = new SingleAssociation<ComponentRelation, ConfigurationBlock>(this);

	public LookupStrategy targetContext() throws LookupException {
		return componentType().localStrategy();
	}

	public Type declarationType() throws LookupException {
		return componentType();
	}

	public Ternary complete() {
		return Ternary.TRUE;
	}

	public Declaration declarator() {
		return this;
	}

//  public void setBody(ClassBody body) {
//  	setAsParent(_body, body);
//  }
  
  
	private SingleAssociation<ComponentRelation,ComponentType> _body = new SingleAssociation<ComponentRelation,ComponentType>(this);
	
	public void setBody(ClassBody body) {
		if(body == null) {
			_body.connectTo(null);
		} else {
			_body.connectTo((Association<? extends ComponentType, ? super ComponentRelation>) createComponentType(body).parentLink());
		}
	}

  private TypeWithBody createComponentType(ClassBody body) {
  	RegularType anon = new ComponentType();
	  anon.setBody(body);
		return anon;
	}
  
  public ComponentType componentTypeDeclaration() {
  	return _body.getOtherEnd();
  }

  
  private void setComponentTypeDeclaration(ComponentType componentType) {
  	if(componentType == null) {
  		_body.connectTo(null);
  	} else {
  		_body.connectTo((Association<? extends ComponentType, ? super ComponentRelation>) componentType.parentLink());
  	}
  }

//  /**
//   * Return the ConfigurationBlock of this member.
//   */
//  public ClassBody body() {
//    return _body.getOtherEnd();
//  }
  
//  private SingleAssociation<ComponentRelation, ClassBody> _body = new SingleAssociation<ComponentRelation, ClassBody>(this);

}
