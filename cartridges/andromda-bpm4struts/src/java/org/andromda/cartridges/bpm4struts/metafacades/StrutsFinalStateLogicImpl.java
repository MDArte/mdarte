package org.andromda.cartridges.bpm4struts.metafacades;


import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.metafacades.uml.CoppetecFrontEndPackage;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.apache.commons.lang.StringUtils;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * MetafacadeLogic implementation.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.StrutsFinalState
 */
public class StrutsFinalStateLogicImpl
    extends StrutsFinalStateLogic
{
	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
	
	public StrutsFinalStateLogicImpl(
        java.lang.Object metaObject,
        java.lang.String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.ModelElementFacad#getValue()
     */
    public String getName()
    {
        String name = super.getName();

        if (name == null)
        {
            final UseCaseFacade useCase = this.getTargetUseCase();
            if (useCase != null)
            {
                name = useCase.getName();
            }
        }

        return name;
    }

    protected String handleGetFullPath()
    {
        String fullPath = null;
        
        final CoppetecStrutsUseCase useCase = (CoppetecStrutsUseCase) getTargetUseCase();
        if (useCase == null)
        {
        	
            // perhaps this final state links outside of the UML model
            final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_EXTERNAL_HYPERLINK);
            if (taggedValue == null)
            {
                String name = getName();
                if (name != null && (name.startsWith("/") || name.startsWith("http://")))
                {
                    fullPath = name;
                }
                else if (name != null && !name.trim().equals(""))
                {
                	// caso o caso de uso alvo não esteja no mesmo modelo.. monta caminho usando o nome do estado final
                	name = StringUtils.capitalize(name);
                	String formato = ".action";
                	
					if (!this.getActions().isEmpty()) {
						Object a = this.getActions().get(0);

						if (a instanceof CoppetecStrutsAction) {
							formato = ((CoppetecStrutsAction) a).getActionFormat();
						}//Estou assumindo que todas as acoes do caso de uso possuem a mesma tecnlogoia
					}

					return "/" + name + "/" + name + formato;
					//ter cuidado nesse caso, nao se sabe se o caso de uso em outro modelo
					//sera struts1 ou struts2
                }
            }
            else
            {
                fullPath = String.valueOf(taggedValue);
            }
        }
        else
        {
            fullPath = useCase.getActionPath() + useCase.getUseCaseFormat();
        }

        return fullPath;
    }

    /**
     * Overridden for now (@todo need to figure out why it doesn't work correctly when using
     * the one from the FrontEndFinalState).
     *
     * @see org.andromda.metafacades.uml.FrontEndFinalState#getTargetUseCase()
     */
    public FrontEndUseCase getTargetUseCase()
    {
        FrontEndUseCase targetUseCase = null;

        // first check if there is a hyperlink from this final state to a use-case
        // this works at least in MagicDraw
        final Object taggedValue = this.findTaggedValue(UMLProfile.TAGGEDVALUE_MODEL_HYPERLINK);
        if (taggedValue != null)
        {
            if (taggedValue instanceof StrutsActivityGraph)
            {
                targetUseCase = (FrontEndUseCase)((StrutsActivityGraph)taggedValue).getUseCase();
            }
            else if (taggedValue instanceof CoppetecStrutsUseCase)
            {
                targetUseCase = (FrontEndUseCase)taggedValue;
            }
        }
        else // maybe the name points to a use-case ?
        {
            final String name = super.getName();
            if (StringUtils.isNotBlank(name))
            {
                UseCaseFacade useCase = getModel().findUseCaseByName(name);
                if (useCase instanceof FrontEndUseCase)
                {
                    targetUseCase = (FrontEndUseCase)useCase;
                }
            }
        }
        return targetUseCase;
    }

    protected List handleGetActions()
    {
        Set actions = new HashSet();
        Collection incoming = this.getIncoming();

        for (final Iterator incomingIterator = incoming.iterator(); incomingIterator.hasNext();)
        {
            StrutsForward forward = (StrutsForward)incomingIterator.next();
            actions.addAll(forward.getActions());
        }
        return new ArrayList(actions);
    }
    
    public Comparator getComparatorObject(){
    	return ((MetafacadeBase)getSuperFinalStateFacade()).getComparatorObject();
    }
protected Boolean handleValidateWebModule() {
		if(this.getTargetUseCase() == null){
			return new Boolean(true);
		}
		CoppetecFrontEndPackage pacoteDestino = (CoppetecFrontEndPackage)(this.getTargetUseCase().getPackage());
		CoppetecFrontEndPackage pacoteOrigem = (CoppetecFrontEndPackage)(((StrutsAction)this.getActions().get(0)).getUseCase().getPackage());
		return new Boolean(pacoteDestino.getWebModuleName().equals(pacoteOrigem.getWebModuleName()));
		// o operador new é utilizado para criar uma nova instância da classe Boolean pois o compilador não aceita autoboxing
		
	}
    
}
