package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsUseCase.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsUseCase
 */
public class CoppetecStrutsUseCaseLogicImpl
    extends CoppetecStrutsUseCaseLogic
{

	public String insertModuleNameIntoOutlet(String outlet) {
		return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
    
    public CoppetecStrutsUseCaseLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    
    protected boolean normalizeMessages()
    {
        final String normalizeMessages = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_NORMALIZE_MESSAGES);
        return Boolean.valueOf(normalizeMessages).booleanValue();
    }
    
    protected Map handleGetMessages()
    {
        final boolean normalize = this.normalizeMessages();
        final Map messages = new TreeMap();
        
        messages.put(this.getTitleKey(), this.getTitleValue());
        messages.put(this.getOnlineHelpKey(), this.getOnlineHelpValue());

        final List actions = this.getActions();
        for (int j = 0; j < actions.size(); j++)
        {
            final StrutsAction action = (StrutsAction)actions.get(j);

            // FORWARDS
            final List transitions = action.getTransitions();
            for (int l = 0; l < transitions.size(); l++)
            {
                final StrutsForward forward = (StrutsForward)transitions.get(l);
                messages.putAll(forward.getSuccessMessages());
                messages.putAll(forward.getWarningMessages());
            }

            // TRIGGER
            final StrutsTrigger trigger = action.getActionTrigger();
            if (trigger != null)
            {
                // only add these when a trigger is present, otherwise it's no use having them
                messages.put(action.getOnlineHelpKey(), action.getOnlineHelpValue());
                messages.put(action.getDocumentationKey(), action.getDocumentationValue());

                // the regular trigger messages
                messages.put(trigger.getTitleKey(), trigger.getTitleValue());
                messages.put(trigger.getNotAllowedTitleKey(), trigger.getNotAllowedTitleValue());
                messages.put(trigger.getResetMessageKey(), trigger.getResetMessageValue());
                messages.put(trigger.getResetNotAllowedTitleKey(), trigger.getResetNotAllowedTitleValue());
                messages.put(trigger.getResetTitleKey(), trigger.getResetTitleValue());
                // this one is the same as doing: action.getMessageKey()
                messages.put(trigger.getTriggerKey(), trigger.getTriggerValue());

                // IMAGE LINK
                if (action.isImageLink())
                {
                    messages.put(action.getImageMessageKey(), action.getImagePath());
                }
            }
        }

        final List pages = this.getPages();
        for (int j = 0; j < pages.size(); j++)
        {
            // PAGE
            final StrutsJsp page = (StrutsJsp)pages.get(j);
            messages.put(page.getTitleKey(), page.getTitleValue());
            messages.put(page.getMessageKey(), page.getMessageValue());
            messages.put(page.getOnlineHelpKey(), page.getOnlineHelpValue());
            messages.put(page.getDocumentationKey(), page.getDocumentationValue());

            final List pageVariables = page.getPageVariables();
            for (int k = 0; k < pageVariables.size(); k++)
            {
                // PAGE-VARIABLE
                final StrutsParameter parameter = (StrutsParameter)pageVariables.get(k);

                messages.put(parameter.getMessageKey(), parameter.getMessageValue());
/*
                        if (normalize)
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                        }
                        else
                        {
                            // the next line is in comment because it's not actually being used
                            //messages.put(page.getTitleKey() + '.' + parameter.getTitleKey(), parameter.getTitleValue());
                            messages.put(page.getTitleKey() + '.' + parameter.getMessageKey(),
                                    parameter.getMessageValue());
                        }
*/

                // TABLE
                if (parameter.isTable())
                {
                	
                    final Collection columnNames = parameter.getTableColumnNames();
                    for (final Iterator columnNameIterator = columnNames.iterator();
                         columnNameIterator.hasNext();)
                    {
                        final String columnName = (String)columnNameIterator.next();
                        messages.put(parameter.getTableColumnMessageKey(columnName),
                            parameter.getTableColumnMessageValue(columnName));
                    }
                }
            }

            for (int k = 0; k < actions.size(); k++)
            {
                // ACTION
                final StrutsAction action = (StrutsAction)actions.get(k);

                // ACTION PARAMETERS
                final List parameters = action.getActionParameters();
                for (int l = 0; l < parameters.size(); l++)
                {
                    final CoppetecStrutsParameter parameter = (CoppetecStrutsParameter)parameters.get(l);
                    messages.put(parameter.getMessageKey(), parameter.getMessageValue());
                    messages.put(parameter.getOnlineHelpKey(), parameter.getOnlineHelpValue());
                    messages.put(parameter.getHintKey(), parameter.getHintValue());
                    messages.put(parameter.getDocumentationKey(), parameter.getDocumentationValue());
                    messages.put(parameter.getTitleKey(), parameter.getTitleValue());

                    if (parameter.getValidWhen() != null)
                    {
                        // this key needs to be fully qualified since the valid when value can be different
                        final String completeKeyPrefix = (normalize)
                            ? parameter.getMessageKey()
                            : this.getTitleKey() + '.' +
                            page.getMessageKey() + '.' +
                            action.getMessageKey() + '.' +
                            parameter.getMessageKey();
                        messages.put(completeKeyPrefix + "_validwhen",
                            "{0} is only valid when " + parameter.getValidWhen());
                    }

                    if (parameter.getOptionCount() > 0)
                    {
                        final List optionKeys = parameter.getOptionKeys();
                        final List optionValues = parameter.getOptionValues();

                        for (int m = 0; m < optionKeys.size(); m++)
                        {
                            messages.put(optionKeys.get(m), optionValues.get(m));
                            messages.put(optionKeys.get(m) + ".title", optionValues.get(m));
                        }
                    }
                }
            }
        }
        
        return messages;
    }
    
    
    
    protected List handleGetMessagesWithContext()
    {
    	final Set messages = new HashSet();
        
        String useCaseStr = this.getName(); 
        
        messages.add(useCaseStr + ";" + ";" + "Titulo" + ";" + this.getTitleKey() + ";" + this.getTitleValue());
        messages.add(useCaseStr + ";" + ";" + "Ajuda" + ";" + this.getOnlineHelpKey() + ";" + this.getOnlineHelpValue());
        
        final List actions = this.getActions();
        
        for (int j = 0; j < actions.size(); j++)
        {
            final StrutsAction action = (StrutsAction)actions.get(j);

            // FORWARDS
            final List transitions = action.getTransitions();
            for (int k = 0; k < transitions.size(); k++)
        	{
        		final StrutsForward forward = (StrutsForward)transitions.get(k);
        		
        		
        		Map success = forward.getSuccessMessages();
        		Iterator it = success.keySet().iterator();
        		
        		while(it.hasNext()){
        			String key = (String)it.next();
        			messages.add(useCaseStr + ";" + forward.getSource().getName() + ";" + "SucessoTela ;" + key + ";" + success.get(key));
        		}
        		
        		Map warn = forward.getWarningMessages();
        		it = warn.keySet().iterator();
        		
        		while(it.hasNext()){
        			String key = (String)it.next();
        			messages.add(useCaseStr + ";" + forward.getSource().getName() + ";" + "WarningTela ;" + key + ";" + success.get(key));
        		}
        		
        	}
            
            // TRIGGER
               
            
            final StrutsTrigger trigger = action.getActionTrigger();
            if (trigger != null)
            {
//                    	 only add these when a trigger is present, otherwise it's no use having them
            	
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "Ajuda" + ";" + action.getOnlineHelpKey() + ";" + action.getOnlineHelpValue());
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "Documentaçao" + ";" + action.getDocumentationKey() + ";" + action.getDocumentationValue());
        		
        		// the regular trigger messages
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "Titulo" + ";" + trigger.getTitleKey() + ";" + trigger.getTitleValue());
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "NaoPermitidoTitulo" + ";" + trigger.getNotAllowedTitleKey() + ";" + trigger.getNotAllowedTitleValue());
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "ResetMensagem" + ";" + trigger.getResetMessageKey() + ";" + trigger.getResetMessageValue());
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "ResetNaoPermitidoTitulo" + ";" + trigger.getResetNotAllowedTitleKey() + ";" + trigger.getResetNotAllowedTitleValue());
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "ResetTitulo" + ";" + trigger.getResetTitleKey() + ";" + trigger.getResetTitleValue());
        		//this one is the same as doing: action.getMessageKey()
        		messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "ValorTrigger" + ";" + trigger.getTriggerKey() + ";" + trigger.getTriggerValue());
        		
        		// IMAGE LINK
        		if (action.isImageLink())
        		{
        			messages.add(useCaseStr + ";" + trigger.getStrutsAction().getInput().getName() + ";" + "LinkImagem" + ";" + action.getImageMessageKey() + ";" + action.getImagePath());
        		}
            }
        }
        
        final List pages = this.getPages();
        for (int j = 0; j < pages.size(); j++)
        {
            // PAGE
            final StrutsJsp page = (StrutsJsp)pages.get(j);
            messages.add(useCaseStr + ";" + page.getName() + ";" + "Titulo;" + page.getTitleKey() + ";" + page.getTitleValue());
            messages.add(useCaseStr + ";" + page.getName() + ";" + "MessagePage;" + page.getMessageKey() + ";" + page.getMessageValue());
            messages.add(useCaseStr + ";" + page.getName() + ";" + "OnLineHelp;" + page.getOnlineHelpKey() + ";" + page.getOnlineHelpValue());
            

            final List pageVariables = page.getPageVariables();
            for (int k = 0; k < pageVariables.size(); k++)
            {
                // PAGE-VARIABLE
                final  StrutsParameter parameter = (StrutsParameter)pageVariables.get(k);

                messages.add(useCaseStr + ";" + page.getName() + ";" + "VariavelPagina;" + parameter.getMessageKey() + ";" + parameter.getMessageValue());

                // TABLE
                if (parameter.isTable())
                {
                	
                    final Collection columnNames = parameter.getTableColumnNames();
                    for (final Iterator columnNameIterator = columnNames.iterator();
                         columnNameIterator.hasNext();)
                    {
                        final String columnName = (String)columnNameIterator.next();
                        messages.add(useCaseStr + ";" + page.getName() + ";" + "NomeColuna" + ";" +parameter.getTableColumnMessageKey(columnName) + ";" +
                            parameter.getTableColumnMessageValue(columnName));
                    }
                }
                
                
            }
        }
        
        for (int k = 0; k < actions.size(); k++)
        {
            // ACTION
            final StrutsAction action = (StrutsAction)actions.get(k);

            // ACTION PARAMETERS
            final List parameters = action.getActionParameters();
            for (int l = 0; l < parameters.size(); l++)
            {
                final StrutsParameter parameter = (StrutsParameter)parameters.get(l);
                messages.add(useCaseStr + ";" + action.getInput().getName() + ";" + "ParametroMensagem" + ";" + parameter.getMessageKey() + ";" + parameter.getMessageValue());
                messages.add(useCaseStr + ";" + action.getInput().getName() + ";" + "ParametroAjuda" + ";" + parameter.getOnlineHelpKey() + ";" + parameter.getOnlineHelpValue());
                messages.add(useCaseStr + ";" + action.getInput().getName() + ";" + "ParametroTitulo" + ";" + parameter.getTitleKey() + ";" + parameter.getTitleValue());
            }
        }

        List messagesList = new ArrayList(messages);
        
        Collections.sort(messagesList);
        return messagesList;    
    }    


    /** 
     * Retorna uma coleção contendo os modos de operação de todos os casos de uso da aplicação.
     */
    public Collection handleGetAllModosOperacao() {
    	Set setModosOperacao = new HashSet();
    	Iterator packageUseCases = getAllUseCases().iterator();

    	while (packageUseCases.hasNext()) {
    		CoppetecStrutsUseCase useCase = (CoppetecStrutsUseCase) packageUseCases.next();
    		setModosOperacao.addAll(useCase.getModosOperacao());
    	}
    	
    	return setModosOperacao;
    }
    
    /** 
     * Retorna uma coleção contendo os modos de operação do caso de uso.
     * Busca esses valores nas tagged values definidas nas transições e nos parâmetros.
     */
    public Collection handleGetModosOperacao() {
    	Set setModosOperacao = new HashSet();    	

    	java.util.Iterator useCaseForwards = this.getActivityGraph().getTransitions().iterator();
    	while (useCaseForwards.hasNext()) {
    		StrutsForward forward = (StrutsForward) useCaseForwards.next();
			
			String actionOperationModes = (String) forward.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_OPERATION_MODE);
			String actionInputOperationModes = (String) forward.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ACTION_INPUT_OPERATION_MODE);
			setModosOperacao.addAll(listOperationModes(actionOperationModes));
			setModosOperacao.addAll(listOperationModes(actionInputOperationModes));
			
			Iterator forwardParameters = forward.getForwardParameters().iterator();
			while (forwardParameters.hasNext()) {
				StrutsParameter parameter = (StrutsParameter) forwardParameters.next();
				
    			String fieldOperationModes = (String) parameter.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_FIELD_OPERATION_MODE);
    			setModosOperacao.addAll(listOperationModes(fieldOperationModes));
			}
		}
    	
    	return setModosOperacao;
    }

    /** Retorna os modos de operação contidos em uma String como uma Collection de Strings */ 
	private Collection listOperationModes (String operationModes) {
		List lista = new ArrayList();
		
		if (operationModes != null) {
			StringTokenizer tokenizer = new StringTokenizer(operationModes, ",");
			while (tokenizer.hasMoreTokens()) {
				String opMode = tokenizer.nextToken().trim();
				lista.add(opMode);
			}
		}
		return lista;
	}

	protected boolean handleIsUseCaseOldStruts()
    {
        final boolean struts2 = Boolean.valueOf((String) this.getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_WEB_STRUTS2)).booleanValue();

        Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_OTHER_TECHNOLOGY);

        boolean otherTechnology = false;

        if (value != null && "true".equalsIgnoreCase(value.toString()))
            otherTechnology = true;

        // se estiver marcado como outra tecnologia, retorna true se o Struts2 for o default
        if (otherTechnology)
        {
            return struts2;
        }
        else
        {
            // se nao estiver marcado como outra tecnologia e nao o default nao for Strut2
            if (!struts2)
            {
                return true;
            }
            else if (this.getPackage() instanceof org.andromda.metafacades.uml14.FrontEndPackageLogicImpl)
            {
                org.andromda.metafacades.uml14.FrontEndPackageLogicImpl pacote =
                        (org.andromda.metafacades.uml14.FrontEndPackageLogicImpl) this.getPackage();
                boolean ret = pacote.handleIsPackageOldStruts();
                return ret;
            }
        }

        

//        if ((struts2 && otherTechnology) || (!struts2 && !otherTechnology))
//        {
//            return true;
//        }

        return false;
    }

    protected java.lang.String handleGetUseCaseFormat()
    {
        if (handleIsUseCaseOldStruts())
        {
            return ".do";
        }
        return ".action";
    }

    protected java.lang.String handleGetFormatOfInitAction()
    {
        TransitionFacade firstTransition = this.getActivityGraph().getInitialTransition();
        if (firstTransition instanceof CoppetecStrutsAction)
        {
        	CoppetecStrutsAction firstAction = (CoppetecStrutsAction) firstTransition;
            return firstAction.getActionFormat();
        }
        return ".action";
    }

	protected boolean handleIsOpenAccess() 
	{	
		boolean ret = false;

		if (this.getPackage() instanceof CoppetecPackageFacade)
        {
			CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
            ret = pacote.isOpenAccess();
        }
		
		return ret || this.hasStereotype(Bpm4StrutsProfile.STEREOTYPE_OPEN_ACCESS);
	}

	protected String handleGetWebLayoutName() {
		String ret = null;

		if (this.getPackage() instanceof CoppetecPackageFacade)
        {
			CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
            ret = pacote.getWebLayoutName();
        }
		
		if (ret == null)
		{
			Object value = this.findTaggedValue(UMLProfile.TAGGEDVALUE_LAYOUT_NAME);
			ret = value != null ? (String) value : null;
		}

		return ret;
	}
	
	protected String handleGetCustomLink() {
		
		return (String) this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_CUSTOM_LINK);
	}
}
