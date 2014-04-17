package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsAction.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsAction
 */
public class CoppetecStrutsActionLogicImpl
    extends CoppetecStrutsActionLogic
{

	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getUseCase().getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
    
	
	public CoppetecStrutsActionLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected String handleGetOnlineHelpValue()
    {
        final StringBuffer buffer = new StringBuffer();
        
        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "" : value);
        
        String message = buffer.toString();
        if (StringUtils.isBlank(message))
        	return message;

        return StringUtilsHelper.toResourceMessage(message);
    }


	
	protected boolean handleIsLookupGrid() {
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_LOOKUPGRID
        .equals(this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE));
	}


	
	protected boolean handleIsImage() {
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_IMAGE
        .equals(this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE));
	}
	
	protected boolean handleIsClientValidation() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_CLIENT_VALIDATION);
		// the default is false
		if (value == null || "false".equalsIgnoreCase(value.toString()))
			return false;
		else
			return true;
	}


	protected List handleGetFieldLinkAsList() {
		Object fieldLik = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_FIELD_LINK);
		List fields = new ArrayList();
		if(fieldLik!= null && !((String)fieldLik).equals("")){
			String strFieldLink = (String)fieldLik;
			String[] arrFieldLink = strFieldLink.split(",");			
			for(int i = 0; i < arrFieldLink.length; i++){
				fields.add(arrFieldLink[i].trim());
			}
			
		}
		return fields;
		
		
	}


	protected boolean handleIsOnlineHelp() {
		if (isTableLink() || !isFormPost())
			return false;
		
		final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
		if (value != null)
			return true;
		
		for (Iterator iterator = getActionParameters().iterator(); iterator.hasNext();) {
			CoppetecStrutsParameter parameter = (CoppetecStrutsParameter) iterator.next();
			if (parameter.isOnlineHelp())
				return true;
		}
		
		return false;
	}

	/**
	 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsActionLogic#handleIsCopyParameters()
	 */
	protected boolean handleIsCopyParameters() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_COPY_PARAMETERS);
		// the default is true
		if (value == null || "true".equalsIgnoreCase(value.toString()))
			return true;
		else
			return false;
	}


	/**
	 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsActionLogic#handleIsTogglePageControl()
	 */
	protected boolean handleIsTogglePageControl()
	{
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TOGGLE_PAGE_CONTROL);
		// the default is false
		if (value == null || "false".equalsIgnoreCase(value.toString()))
			return false;
		else
			return true;
	}
protected String handleGetFullName() {
		String [] tmp = getFullActionPath().split("/");
		return tmp[tmp.length-1];
	}


	protected boolean handleIsAjaxAction() {

		/* Caso retorne para a mesma página os campos devem ser automaticamente carregados por xml
		 * 
		 */
		List targetPages = this.getTargetPages();
		this.getActionInput();
		if(targetPages!=null && targetPages.size()==1){
			if(targetPages.get(0) instanceof StrutsJsp){
				StrutsJsp jsp = (StrutsJsp)targetPages.get(0);
				if(jsp.getFullPath().equals(this.getActionInput())){
					return true;
				}
			}
		}
		
		return false;
	}
	protected boolean handleIsTemParametroTabela(){
		/*Verifico se algum dos parâmetros é do tipo tabela, 
		 * 
		*/
		Collection formFields = this.getActionFormFields();
		for(Iterator it = formFields.iterator();it.hasNext();){
			CoppetecStrutsParameter param = (CoppetecStrutsParameter)it.next();
			if(param.isTable()){
				return true;
			}
		}
		return false;
	}

	protected boolean handleIsActionOldStruts() {
		//testa se o pai dele eh anotado(UseCase)
		CoppetecStrutsUseCase usecase =  (CoppetecStrutsUseCase) getUseCase();
		return usecase.isUseCaseOldStruts();
	}

	protected java.lang.String handleGetActionFormat() {
		if (handleIsActionOldStruts()) {
			return ".do";
		}
		return ".action";
	}

	protected boolean handleIsPopup() {
        return Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE_POPUP
        .equals(this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_TYPE));
	}
}