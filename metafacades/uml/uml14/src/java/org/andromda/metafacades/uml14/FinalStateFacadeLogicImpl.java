package org.andromda.metafacades.uml14;

import java.util.Comparator;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;




/**
 * Metaclass facade implementation.
 */
public class FinalStateFacadeLogicImpl
    extends FinalStateFacadeLogic
{
    public FinalStateFacadeLogicImpl(
        org.omg.uml.behavioralelements.statemachines.FinalState metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.core.metafacade.MetafacadeBase#getValidationOwner()
     */
    public Object getValidationOwner()
    {
        return getStateMachine();
    }

	protected boolean handleIsTransitions() {
		if(this.getIncoming().size() > 0) return true;
		
		return false;
	}
	
	 public Comparator getComparatorObject(){
	    	return new Comparator(){
	    		public int compare(Object obj1, Object obj2){
	    			if(obj1 instanceof FinalStateFacade && obj2 instanceof FinalStateFacade){
	    				FinalStateFacade final1 = (FinalStateFacade)obj1;
	    				FinalStateFacade final2 = (FinalStateFacade)obj2;
	    				
	    				String name1 = final1.getName();
	    				if(name1 == null || name1.equals("")){
	    					name1 = (String)final1.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
	    					name1 = (String)final1.findTaggedValue("@andromda.presentation.view.external_hyperlink_modulo") + name1;
	    				}else
	    					name1 = ((CoppetecPackageFacade)((FrontEndForward)final1.getIncoming().iterator().next()).getUseCase().getPackage()).getWebModuleName() + name1;
	    					
	    				
	    				String name2 = final2.getName();
	    				if(name2 == null || name2.equals("")){
	    					name2 = (String)final2.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
	    					name2 = (String)final2.findTaggedValue("@andromda.presentation.view.external_hyperlink_modulo") + name2;
	    				}else
	    					name2 = ((CoppetecPackageFacade)((FrontEndForward)final2.getIncoming().iterator().next()).getUseCase().getPackage()).getWebModuleName() + name2;
	    				
	    				return name1.compareToIgnoreCase(name2);
	    			}
	    			throw new RuntimeException("Os elementos nao sao FinalStateFacadeFacade");
	    		}
	    	};
	    }
}