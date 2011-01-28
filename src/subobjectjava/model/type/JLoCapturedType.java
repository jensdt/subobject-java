package subobjectjava.model.type;

import java.util.List;

import subobjectjava.model.component.ComponentRelation;

import jnome.core.type.CapturedType;
import chameleon.oo.type.ParameterSubstitution;
import chameleon.oo.type.Type;
import chameleon.oo.type.generics.ActualTypeArgument;
import chameleon.oo.type.inheritance.InheritanceRelation;

public class JLoCapturedType extends CapturedType {

	public JLoCapturedType(Type baseType, List<ActualTypeArgument> typeParameters) {
		super(baseType,typeParameters);
	}

	public JLoCapturedType(List<ParameterSubstitution> parameters, Type baseType) {
		super(parameters,baseType);
	}

	public JLoCapturedType(ParameterSubstitution substitution, Type baseType) {
		super(substitution, baseType);
	}
	
	@Override
	public JLoCapturedType clone() {
		return new JLoCapturedType(clonedParameters(),baseType());
	}

//	@Override
//	public List<InheritanceRelation> inheritanceRelations() {
//		// first take the subtype relations
//		List<InheritanceRelation> result = super.inheritanceRelations();
//		// then add the component relations
//		List<ComponentRelation> components = directlyDeclaredElements(ComponentRelation.class);
//		result.addAll(components);
//		return result;
//	}
//	
//	public List<InheritanceRelation> nonMemberInheritanceRelations() {
//		return super.inheritanceRelations();
//	}

	
}
