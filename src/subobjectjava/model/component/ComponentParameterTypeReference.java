package subobjectjava.model.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jnome.core.language.Java;
import jnome.core.type.ArrayTypeReference;
import jnome.core.type.JavaTypeReference;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.DerivedType;
import chameleon.oo.type.IntersectionTypeReference;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public class ComponentParameterTypeReference extends NamespaceElementImpl<ComponentParameterTypeReference,Element> implements JavaTypeReference<ComponentParameterTypeReference>,ComponentArgumentContainer<ComponentParameterTypeReference> {

	public ComponentParameterTypeReference(JavaTypeReference target) {
		setTarget(target);
	}
	
  public JavaTypeReference target() {
  	return _componentType.getOtherEnd();
  }
  
  private SingleAssociation<ComponentParameterTypeReference, JavaTypeReference> _componentType = new SingleAssociation<ComponentParameterTypeReference, JavaTypeReference>(this);

  public void setTarget(JavaTypeReference target) {
    setAsParent(_componentType, target);
  }
  
  public List<ActualComponentArgument> componentArguments() {
  	return _genericParameters.getOtherEnds();
  }
  
  public void addArgument(ActualComponentArgument arg) {
  	if(arg != null) {
  		_genericParameters.add(arg.parentLink());
  	}
  }
  
  public void addAllArguments(List<ActualComponentArgument> args) {
  	for(ActualComponentArgument argument : args) {
  		addArgument(argument);
  	}
  }
  
  public void removeArgument(ActualComponentArgument arg) {
  	if(arg != null) {
  		_genericParameters.remove(arg.parentLink());
  	}
  }
  
  public int indexOf(ActualComponentArgument arg) {
  	return _genericParameters.indexOf(arg);
  }
  
  private OrderedMultiAssociation<ComponentParameterTypeReference,ActualComponentArgument> _genericParameters = new OrderedMultiAssociation<ComponentParameterTypeReference, ActualComponentArgument>(this);

	public Type getType() throws LookupException {
		return getElement();
	}

	public Type getElement() throws LookupException {
		Type componentType = target().getElement();
		List<ComponentParameter> parameters = new ArrayList<ComponentParameter>();
		List<ActualComponentArgument> componentArguments = componentArguments();
		List<ComponentParameter> formalParameters = target().getElement().parameters(ComponentParameter.class);
		if(componentArguments.size() != formalParameters.size()) {
			throw new LookupException("The number of actual component parameters: "+componentArguments.size()+ " does not match the number of formal component parameters: "+formalParameters.size());
		}
		Iterator<ActualComponentArgument> arguments =  componentArguments.iterator();
		Iterator<ComponentParameter> formals = formalParameters.iterator();
		while(arguments.hasNext()) {
			ActualComponentArgument arg = arguments.next();
			Signature sig = formals.next().signature().clone();
			parameters.add(new InstantiatedComponentParameter((SimpleNameSignature) sig, arg));
		}
		DerivedType result = new DerivedType(ComponentParameter.class, parameters, componentType);
		result.setUniParent(componentType.parent());
		return result;
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		IntersectionTypeReference<IntersectionTypeReference> intersectionTypeReference = language(Java.class).createIntersectionReference(clone(), other.clone());
		return intersectionTypeReference;
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other) {
		IntersectionTypeReference<?> result = other.clone();
		result.add(clone());
		return result;
	}

	public Declaration getDeclarator() throws LookupException {
		return target().getDeclarator();
	}

	public List<? extends Element> children() {
		List<Element> result = Util.createNonNullList(target());
		result.addAll(componentArguments());
		return result;
	}

	public JavaTypeReference toArray(int dimension) {
  	JavaTypeReference result;
  	if(dimension > 0) {
  	  result = new ArrayTypeReference(clone(), dimension);
  	} else {
  		result = this;
  	}
  	return result;
	}

	public JavaTypeReference erasedReference() {
		JavaTypeReference erasedReference = target().erasedReference();
		ComponentParameterTypeReference result = new ComponentParameterTypeReference(erasedReference);
		return result;
	}

	public JavaTypeReference componentTypeReference() {
		return target().componentTypeReference();
	}

	@Override
	public ComponentParameterTypeReference clone() {
		JavaTypeReference target = target();
		JavaTypeReference clone = target == null ? null : target.clone();
		ComponentParameterTypeReference result = new ComponentParameterTypeReference(clone);
		for(ActualComponentArgument<?> arg: componentArguments()) {
			result.addArgument(arg.clone());
		}
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public Type containerType(ActualComponentArgument argument) throws LookupException {
		return formalParameter(argument).containerType();
	}

	public FormalComponentParameter formalParameter(ActualComponentArgument argument) throws LookupException {
	  Type enclosing = componentTypeReference().getElement();
	  int index = indexOf(argument);
	  if(index >= 1) {
	  	return (FormalComponentParameter) enclosing.parameter(ComponentParameter.class, index);
	  } else {
	  	throw new LookupException("The given argument does not belong to this component parameter type reference.");
	  }
	}

}
