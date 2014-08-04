package org.andromda.metafacades.uml14;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.CoppetecUMLMetafacadeProperties;
import org.andromda.metafacades.uml.FinalStateFacade;
import org.andromda.metafacades.uml.FrontEndActionState;
import org.andromda.metafacades.uml.FrontEndUseCase;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.StateVertexFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecPackageFacade.
 *
 * @see org.andromda.metafacades.uml.CoppetecPackageFacade
 */
public class CoppetecPackageFacadeLogicImpl extends CoppetecPackageFacadeLogic {
	private static ArrayList modulesList = new ArrayList();

	static {
		modulesList.add("acompanhamento");
		modulesList.add("administracao");
		modulesList.add("core");
		modulesList.add("execucao");
		modulesList.add("gerencial");
		modulesList.add("interfaces");
		modulesList.add("participe");
		modulesList.add("principal");
		modulesList.add("programa");
		modulesList.add("proposta");
	}

    public CoppetecPackageFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

	/**
	 * @see org.andromda.metafacades.uml.CoppetecPackageFacade#getServiceModuleName()
	 */
	protected String handleGetServiceModuleName()
	{
		for(ModelElementFacade packageFacade = this; packageFacade != null; packageFacade = packageFacade.getPackage()){
			if(packageFacade.hasStereotype(UMLProfile.STEREOTYPE_MODULO_SERVICO)){
				return packageFacade.getName();
			} else if (packageFacade.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_SRV)) {
				return packageFacade.getName();
			}
		}

		return "";
	}

	/**
	 * @see org.andromda.metafacades.uml.CoppetecPackageFacade#getWebModuleName()
	 */
	protected String handleGetWebModuleName()
	{
		for(ModelElementFacade packageFacade = this; packageFacade != null; packageFacade = packageFacade.getPackage()){
			if(packageFacade.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB) || packageFacade.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PAI)){
				if (!modulesList.contains(packageFacade.getName()))
					modulesList.add(packageFacade.getName());

				return packageFacade.getName();
			} else if (modulesList.contains(packageFacade.getName())) {
				return packageFacade.getName();
			}
		}

		return "";
	}

	protected java.lang.String handleReplaceOutletWithWebModuleName(java.lang.String outlet) {
		String modulo = getConfiguredProperty(CoppetecUMLMetafacadeProperties.OUTLET_REPLACE_MODULO).toString();
		return StringUtils.replace(outlet, modulo, getWebModuleName());
	}

	protected java.lang.String handleReplaceOutletWithServiceModuleName(java.lang.String outlet) {
		String modulo = getConfiguredProperty(CoppetecUMLMetafacadeProperties.OUTLET_REPLACE_MODULO).toString();
		return StringUtils.replace(outlet, modulo, getServiceModuleName());
	}

	public java.util.Collection handleGetMessageDrivenBeans() {
		Collection col = getElementsByStereotype("MessageDrivenBean");
    	if(!col.isEmpty() && ((MetafacadeBase)col.iterator().next()).getComparatorObject() != null)
    		Collections.sort((List)col, ((MetafacadeBase)col.iterator().next()).getComparatorObject());
    	return col;
	}

	public java.util.Collection handleGetSessions() {
		Collection col = getElementsByStereotype(UMLProfile.STEREOTYPE_SERVICE);
		if (!col.isEmpty() && ((MetafacadeBase)col.iterator().next()).getComparatorObject() != null)
			Collections.sort((List)col, ((MetafacadeBase)col.iterator().next()).getComparatorObject());
		return col;
	}

	protected java.lang.Boolean handleGetWebServiceModule() {
		Boolean webService = Boolean.FALSE;

		Iterator it = getOwnedElements().iterator();

		while (it.hasNext()) {
			ModelElementFacade element = (ModelElementFacade) it.next();

			if (element instanceof CoppetecPackageFacade) {
				CoppetecPackageFacade pacote = (CoppetecPackageFacade) element;
				webService = Boolean.valueOf(webService.booleanValue() || pacote.getWebServiceModule().booleanValue());
			} else if ((element.hasStereotype(UMLProfile.STEREOTYPE_WEBSRV))
					|| (element.hasStereotype(UMLProfile.STEREOTYPE_WEBSRV_CLIENT))) {
				webService = Boolean.TRUE;
			}
		}

		return webService;
	}

	public java.util.Collection handleGetWebServices() {
		Collection col = getElementsByStereotype(UMLProfile.STEREOTYPE_WEBSRV);
		if (!col.isEmpty() && ((MetafacadeBase) col.iterator().next()).getComparatorObject() != null)
			Collections.sort((List) col, ((MetafacadeBase) col.iterator().next()).getComparatorObject());
		return col;
	}

	protected Collection handleGetUseCases() {
		Collection col = getElementsByStereotype(UMLProfile.STEREOTYPE_FRONT_END_USECASE);
		if (!col.isEmpty() && ((MetafacadeBase) col.iterator().next()).getComparatorObject() != null)
			Collections.sort((List) col, ((MetafacadeBase) col.iterator().next()).getComparatorObject());
		return col;
	}

	protected Collection handleGetServices() {
		Collection col = getElementsByStereotype(UMLProfile.STEREOTYPE_SERVICE);
		if (!col.isEmpty() && ((MetafacadeBase) col.iterator().next()).getComparatorObject() != null)
			Collections.sort((List) col, ((MetafacadeBase) col.iterator().next()).getComparatorObject());
		return col;
	}

	protected Collection handleGetManageables() {
		String manageableStereotypes[] = new String[2];
		manageableStereotypes[0] = UMLProfile.STEREOTYPE_ENTITY;
		manageableStereotypes[1] = UMLProfile.STEREOTYPE_MANAGEABLE;
		return getElementsByStereotypes(manageableStereotypes);
	}


	/**
	 * Retorna os elementos dentro do pacote que possuem o estereótipo passado
	 * como parâmetro
	 */
	private Collection getElementsByStereotype(String stereotype) {
		String stereotypeArray[] = new String[1];
		stereotypeArray[0] = stereotype;
		return getElementsByStereotypes(stereotypeArray);
	}

	/**
	 * Retorna os elementos dentro do pacote que possuem os estereótipos
	 * passados como parâmetro
	 */
	private Collection getElementsByStereotypes(String[] stereotypes) {
		List result = new ArrayList();

		Iterator it = getOwnedElements().iterator();

		while (it.hasNext()) {
			ModelElementFacade element = (ModelElementFacade) it.next();

			if (element instanceof CoppetecPackageFacadeLogicImpl) {
				CoppetecPackageFacadeLogicImpl pacote = (CoppetecPackageFacadeLogicImpl) element;
				result.addAll(pacote.getElementsByStereotypes(stereotypes));
			} else {
				boolean checkStereotypes = true;
				for (int i = 0; i < stereotypes.length; i++) {
					if (!element.hasStereotype(stereotypes[i])) {
						checkStereotypes = false;
						break;
					}
				}

				if (checkStereotypes)
					result.add(element);
			}
		}

		return result;
	}

	protected Collection handleGetFinalStates() {

		Collection processedTransitions = new ArrayList();
		Set finalStates = new HashSet();

		Iterator it = getUseCases().iterator();

		while (it.hasNext()) {

			FrontEndUseCase useCase = (FrontEndUseCase) it.next();

			Iterator actions = useCase.getActions().iterator();

			while (actions.hasNext()) {
				TransitionFacade transition = (TransitionFacade) actions.next();
				collectTransitions(transition, processedTransitions, finalStates);
			}
		}

		List result = new ArrayList(finalStates);

		if (!result.isEmpty() && ((MetafacadeBase) result.iterator().next()).getComparatorObject() != null)
			Collections.sort((List) result, ((MetafacadeBase) result.iterator().next()).getComparatorObject());

		return result;
	}

	private void collectTransitions(TransitionFacade transition, Collection processedTransitions, Set finalStates) {
		if (transition == null || processedTransitions.contains(transition)) {
			return;
		}

		processedTransitions.add(transition);

		final StateVertexFacade target = transition.getTarget();
		if (target instanceof FinalStateFacade) {
			finalStates.add(transition.getTarget());
		} else {
			if (target instanceof FrontEndActionState) {
				TransitionFacade trans = ((FrontEndActionState) target).getForward();
				collectTransitions(trans, processedTransitions, finalStates);
			} else {
				final Collection outcomes = target.getIncoming();
				for (final Iterator iterator = outcomes.iterator(); iterator.hasNext();) {
					final TransitionFacade outcome = (TransitionFacade) iterator.next();
					collectTransitions(outcome, processedTransitions, finalStates);
				}
			}
		}
	}

	protected String handleGetMoreSpecificPackageName() {
		StringTokenizer st = new StringTokenizer(this.getFullyQualifiedName(), ".");
		StringBuffer sb = new StringBuffer();
		String[] tokens = new String[st.countTokens()];

		int position = st.countTokens() - 1;

		while (st.hasMoreTokens()) {
			tokens[position--] = st.nextToken();
		}

		for (int i = 0; i < tokens.length; i++) {
			String point = "";
			if (i != tokens.length - 1)
				point = ".";
			sb.append(tokens[i] + point);
		}

		System.out.println("moreSpecific: " + sb.toString());
		return sb.toString();
	}


	protected Collection handleGetTestCases() {
		Collection col = getElementsByStereotype(UMLProfile.STEREOTYPE_TEST_PROCESS);
		if(!col.isEmpty() && ((MetafacadeBase)col.iterator().next()).getComparatorObject() != null)
    		Collections.sort((List)col, ((MetafacadeBase)col.iterator().next()).getComparatorObject());
    	return col;
	}

	protected String handleGetTestModuleName() {
        for(ModelElementFacade packageFacade = this; packageFacade != null; packageFacade = packageFacade.getPackage()){
        	if(packageFacade.hasStereotype(UMLProfile.STEREOTYPE_TEST_MODULE)){
        		return packageFacade.getName();
        	}
        }
        
       return "";
	}

	protected String handleReplaceOutletWithTestModuleName(String outlet) {
		String modulo = getConfiguredProperty(CoppetecUMLMetafacadeProperties.OUTLET_REPLACE_MODULO).toString();
		return StringUtils.replace(outlet, modulo, getTestModuleName());
	}

	public boolean handleIsPackageOldStruts() {

		final boolean struts2 = Boolean.valueOf((String) this.getConfiguredProperty(UMLMetafacadeProperties.PRESENTATION_STRUTS2)).booleanValue();

		Object value = findTaggedValue(UMLProfile.TAGGEDVALUE_PRESENTATION_OTHER_TECHNOLOGY);

		boolean otherTechnology = false;

		if (value != null && "true".equalsIgnoreCase(value.toString()))
			otherTechnology = true;

		// se estiver marcado como outra tecnologia, retorna true se o Struts2 for o default
		if (otherTechnology) {
			return struts2;
		} else {
			// se nao estiver marcado como outra tecnologia e nao o default nao for Strut2
			if (!struts2) {
				return true;
			} else {
				if (this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB) || this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PAI)) {
					return false;
				}

				// testa se o pai dele esta anotado
				if (this.getPackage() != null) {
					if (this.getPackage() instanceof org.andromda.metafacades.uml.CoppetecPackageFacade) {
						org.andromda.metafacades.uml.CoppetecPackageFacade pacote = (org.andromda.metafacades.uml.CoppetecPackageFacade) this
								.getPackage();
						boolean ret = pacote.isPackageOldStruts();
						return ret;
					}
				}
			}
		}
		return false;
	}

	protected boolean handleIsOpenAccess() {
		if(this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB)
			|| this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PAI)
			|| this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_SERVICO)) {
            return this.hasStereotype(UMLProfile.STEREOTYPE_OPEN_ACCESS);
        }

		boolean ret = false;

		if (this.getPackage() != null) {
            if (this.getPackage() instanceof CoppetecPackageFacade) {
                CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
                ret = pacote.isOpenAccess();
            }
        }

		return ret;
	}

	protected String handleGetWebLayoutName() {
		if (this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB) || this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PAI)) {
			Object value = findTaggedValue(UMLProfile.TAGGEDVALUE_LAYOUT_NAME);
			return value != null ? (String) value : null;
		}

		String ret = null;

		if (this.getPackage() != null) {
			if (this.getPackage() instanceof CoppetecPackageFacade) {
				CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
				ret = pacote.getWebLayoutName();
			}
		}

		return ret;
	}

	protected boolean handleValidateOpenAccess() {
		if (this.hasStereotype(UMLProfile.STEREOTYPE_OPEN_ACCESS)) {
			return this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB) || this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PAI)
					|| this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_SERVICO);
		}
		return true;
	}

	protected boolean handleIsWebModulePrincipal() {
		if (this.hasStereotype(UMLProfile.STEREOTYPE_MODULO_WEB_PRINCIPAL)) {
			return true;
		}

		boolean ret = false;

		if (this.getPackage() != null) {
			if (this.getPackage() instanceof CoppetecPackageFacade) {
				CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
				ret = pacote.isWebModulePrincipal();
			}
		}

		return ret;
	}
}