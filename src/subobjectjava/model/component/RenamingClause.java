package subobjectjava.model.component;



import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.QualifiedName;
import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.Stub;
import chameleon.core.member.Member;
import chameleon.oo.type.Type;

public class RenamingClause extends AbstractClause<RenamingClause> {

	public RenamingClause(Signature newSignature, QualifiedName oldFqn) {
		setNewSignature(newSignature);
		setOldFqn(oldFqn);
	}
	
	@Override
	public List<Member> process(Type type) throws LookupException {
		List<Member> resultList = new ArrayList<Member>();
		if(type.nearestAncestor(ComponentRelation.class) == nearestAncestor(ComponentRelation.class)) {
			System.out.println("debug");
		}
		Member member = (Member) oldDeclaration();
		if(member != null) {
			Member resulting = member.clone();
			resulting.setOrigin(member);
			
			// change the name
//			resulting.signature().setName(newSignature().name());
//			resulting.setSignature(newSignature().clone());
			resulting.setName(newSignature().name());
			
			// reroute lookup from within the clone (result) to the parent of the original member. 
//			LookupRedirector redirector = new LookupRedirector(member.parent(),resulting);
			Stub redirector = new ComponentStub(nearestAncestor(ComponentRelation.class), resulting);
			redirector.setUniParent(nearestAncestor(Type.class));

			resultList.add(resulting);
		}
		

		return resultList;
	}

	@Override
	public RenamingClause clone() {
		return new RenamingClause(newSignature().clone(), oldFqn().clone());
	}

}
