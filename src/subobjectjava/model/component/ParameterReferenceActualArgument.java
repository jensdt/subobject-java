package subobjectjava.model.component;

import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.DeclarationWithType;

public class ParameterReferenceActualArgument extends SingleActualComponentArgument<ParameterReferenceActualArgument> {

	public ParameterReferenceActualArgument(String name) {
		super(name);
	}

	@Override
	public ParameterReferenceActualArgument clone() {
		return new ParameterReferenceActualArgument(name());
	}

	@Override
	public ComponentParameter declaration() throws LookupException {
		return lexicalLookupStrategy().lookUp(new SelectorWithoutOrder<ComponentParameter>(selector(), ComponentParameter.class));
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		try {
			if(declaration() == null) {
				result = result.and(new BasicProblem(this, "Cannot resolve component parameter reference."));
			}
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "Cannot resolve component parameter reference."));
		}
		return result;
	}
}
