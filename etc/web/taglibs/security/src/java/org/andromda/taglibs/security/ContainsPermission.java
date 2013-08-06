package org.andromda.taglibs.security;

import java.util.HashMap;

import javax.security.auth.Subject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import accessControl.ControleAcesso;
import accessControl.PrincipalImpl;
import accessControl.Servico;
import accessControl.ServicosSingleton;


public class ContainsPermission extends TagSupport{
	
		static public final  String SERVICOS = "servicos";
		static public final  String USER_SESSION = "UserSession";
		protected String name;
		
		public ContainsPermission(){
			name = null;
		}
		
		public String getName(){
			return name;
		}
		
		public void setName(String name){
			this.name = name;	
		}
		
		public int doStartTag() throws JspException {
			return !condition()? 0 : 1;
		}
		
		private boolean condition() throws JspException {
			//HashMap mapServicos = (HashMap) this.pageContext.getServletContext().getAttribute(SERVICOS);
			HashMap servicos = ServicosSingleton.instance().getServicos();		
			Subject subject = (Subject)this.pageContext.getSession().getAttribute(USER_SESSION);
			PrincipalImpl principal = ControleAcesso.getCallerPrincipal(subject);
			HashMap mapServicos; 
    		if(servicos == null){
    			mapServicos = new HashMap();
    		}else{
    			mapServicos = (HashMap)servicos.get(principal.getNomeProjeto());
    		}
					
			Servico servico = new Servico(name);
			
			try {
				return accessControl.ControleAcesso.verificaPermissao(subject, mapServicos, servico, true);

			}catch(Exception e){
				throw new JspException(e);
			}
		}
}
