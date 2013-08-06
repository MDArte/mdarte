package org.andromda.cartridges.ejb;

import java.util.Collection;
import java.util.StringTokenizer;

import org.andromda.core.metafacade.MetafacadeFactory;

public class EJBUtils {

	public static Collection getMetafacadesByStereotype(String stereotype){
		MetafacadeFactory factory = MetafacadeFactory.getInstance();
		return factory.getMetafacadesByStereotype(stereotype);
	}
	
	public static Collection getWebServiceData(){
		return getMetafacadesByStereotype(EJBProfile.STEREOTYPE_WEB_SERVICE_DATA);
	}

	public static String moreSpecificPackageName(String pacote){
		StringTokenizer st = new StringTokenizer(pacote, ".");
		StringBuffer sb = new StringBuffer();
		String[] tokens = new String[st.countTokens()];
		
		int position = st.countTokens() - 1;
		
		while(st.hasMoreTokens()){
			tokens[position--] = st.nextToken();
		}
		
		for(int i = 0;i < tokens.length;i++){
			String point = "";
			if(i != tokens.length -1) point = ".";
			sb.append(tokens[i] + point);
		}
		return  sb.toString();
	}
}
