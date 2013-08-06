package org.andromda.taglibs.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ContainsOperationMode extends TagSupport{


	protected String value;
	
	protected String useCase;
	
	public String getUseCase() {
		return useCase;
	}

	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}

	public ContainsOperationMode(){
		value = null;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;	
	}
	
	public int doStartTag() throws JspException {
		return !condition()? 0 : 1;
	}
	
	private boolean condition() throws JspException {
		boolean condition = false;
		if (this.pageContext.getSession().getAttribute("modoOperacao") != null){
			HashMap map = (HashMap)this.pageContext.getSession().getAttribute("modoOperacao");
			//String inputModoOperacao = (String)this.pageContext.getSession().getAttribute("modoOperacao");
			if(map.get(useCase) != null){
				String inputModoOperacao = (String)map.get(useCase);
				java.util.List listInput = new ArrayList();
				if(inputModoOperacao != null){
					StringTokenizer stInput = new StringTokenizer(inputModoOperacao, ",");
					while(stInput.hasMoreTokens()){
						String nextInput = stInput.nextToken();
						listInput.add(nextInput);
					
					}
				}
			
				
				if(inputModoOperacao != null){
					StringTokenizer st = new StringTokenizer(value, ",");
					while(st.hasMoreTokens()){
						String next = st.nextToken();
						if(listInput.contains(next)){
							condition = true;
							return condition;
						}
							
					}
				}
			}
		}
		return condition;	
	}

}
