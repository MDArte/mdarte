package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsGlobals;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsJsp.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsJsp
 */
public class CoppetecStrutsJspLogicImpl
    extends CoppetecStrutsJspLogic
{

	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getUseCase().getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
    
    public CoppetecStrutsJspLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
    /**
     * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsJsp#getAllUniqueActionParameters()
     */
    protected java.util.List handleGetAllUniqueActionParameters()
    {
    	final List actionParameters = new ArrayList();
        final List actionParametersNames = new ArrayList();
        final Collection actions = getActions();
        for (final Iterator iterator = actions.iterator(); iterator.hasNext();)
        {
        	final StrutsAction action = (StrutsAction)iterator.next();
        	if (!action.isTableAction() && !action.isTableLink() && !action.isImageLink() && !action.isHyperlink())
        	{
	            for (final Iterator iterator2 = action.getActionParameters().iterator(); iterator2.hasNext();)
	            {
	            	Object parameter = iterator2.next();
	                String parameterName = ((StrutsParameter) parameter).getName();
	                if (!actionParametersNames.contains(parameterName))
	                {
	                   	actionParametersNames.add(parameterName);
	                   	actionParameters.add(parameter);
	                }
	            }
        	}
        }
        return actionParameters;
    }

    protected String handleGetOnlineHelpValue()
    {
        final String crlf = "<br/>";
        final StringBuffer buffer = new StringBuffer();

        final String value = StringUtilsHelper.toResourceMessage(getDocumentation("", 64, false));
        buffer.append((value == null) ? "" : value);
        buffer.append(crlf);

        return StringUtilsHelper.toResourceMessage(buffer.toString());
    }

	protected Boolean handleGetGenerateHelp() {
		String help = (String)getConfiguredProperty(Bpm4StrutsGlobals.PROPERTY_HELP);
        if(!Boolean.valueOf(help).booleanValue()) return Boolean.FALSE;
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_GENERATE_HELP);
		if(value == null) return Boolean.TRUE;
	    return new Boolean(Bpm4StrutsUtils.isTrue(String.valueOf(value)));
	}
	
	protected boolean handleIsGenerateHelpLink() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HELP_LINK);

		if(value == null || ((String)value).length() <= 0)
			return false;
		else
			return true;
	}

	protected String handleGetHelpLink() {
		return (String) findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HELP_LINK);
	}

	protected List handleGetHelpLinkList() {
		List listaLinks = new ArrayList();
		
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HELP_LINK);
		
		StringTokenizer tokenizer = new StringTokenizer((String) value, ";");
		
		while (tokenizer.hasMoreTokens()) {
			String linkModo = tokenizer.nextToken();
			
			StringTokenizer tokenizerModos = new StringTokenizer(linkModo, "=");
			
			String link = tokenizerModos.nextToken();
			String modos = tokenizerModos.hasMoreTokens() ? tokenizerModos.nextToken() : null;
			
			listaLinks.add(new LinkHelp(link, modos));
		}
		
		return listaLinks;
	}

	protected boolean handleIsHelpLinkModoOperacao() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_HELP_LINK);
		
		if (value == null)
			return false;
		
		return ((String) value).contains("=");
	}
	
	public class LinkHelp {
		private String modoOperacao;
		private String link;
		
		public LinkHelp(String link, String modoOperacao) {
			this.link = link;
			this.modoOperacao = modoOperacao;			
		}
		
		public String getModoOperacao() {
			return modoOperacao;
		}
		
		public String getLink() {
			return link;
		}
		
		public boolean isModoOperacaoSet() {
			if (modoOperacao == null || modoOperacao.length() <= 0)
				return false;
			else
				return true;
		}
		
	}

	protected boolean handleIsOnlineHelp() {
		final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
		if (value != null)
			return true;
		
		for (Iterator iterator = getActions().iterator(); iterator.hasNext();) {
			CoppetecStrutsAction action = (CoppetecStrutsAction)iterator.next();
			if (action.isOnlineHelp())
				return true;
		}
		
		return false;
	}
	 protected boolean handleIsOldStruts() {

		 //testa se o pai dele eh anotado(UseCase)
		 CoppetecStrutsUseCase usecase =  (CoppetecStrutsUseCase) getUseCase();
		 return usecase.isUseCaseOldStruts();
	 }

	protected boolean handleIsTemTabela() {
		List variables = this.getPageVariables();
		for(Iterator it = variables.iterator();it.hasNext();){
			Object obj = it.next();
			if(obj instanceof StrutsParameter){
				StrutsParameter param = (StrutsParameter)obj;
				if(param.isTable()){
					return true;
				}
			}
		}
		return false;
	}

	
	
}