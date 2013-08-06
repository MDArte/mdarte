package org.andromda.metafacades.uml;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.andromda.core.common.ExceptionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CoppetecEntityMetafacadeUtils extends EntityMetafacadeUtils {
	
	/**
	 * O logger desta classe.
	 */
	private static final Log log = LogFactory.getLog( EntityMetafacadeUtils.class );

    /**
     * <p/> Converts a string following the Java naming conventions to a
     * database attribute name. For example convert customerName to
     * CUSTOMER_NAME.
     * </p>
     *
     * @param modelElementName the string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
	public static String toSqlNameAbreviado(
	        String modelElementName,
	        Object separator, Object relation)
	    {
	    	
	        final String methodName = "EntityMetaFacadeUtils.toSqlName";
	        ExceptionUtils.checkEmpty(
	            methodName,
	            "modelElementName",
	            modelElementName);

	        String sqlNameAbreviado = "";
	        String name = "";
	       
	        StringCharacterIterator iter = new StringCharacterIterator(StringUtils.uncapitalize(modelElementName));

	        String sep = (String)separator;
	        
	        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next())
	        {
	        	        	
	            if (Character.isUpperCase(character)|| ((String)relation).equals(String.valueOf(character)))
	            {
	            	if(((String)relation).equals(String.valueOf(character))){ 
	            		sep = (String)relation;
	            		character = iter.next();
	            	}else
	            		sep = (String)separator; 
	            	
	       
	            	String abreviacao = Abreviacoes.getInstance().abrevie(name.toUpperCase());
	            	
	            	if(abreviacao != null || !StringUtils.isEmpty(abreviacao)){
	            		sqlNameAbreviado += abreviacao;
	            	}
	            	else
	            		sqlNameAbreviado += name.toUpperCase();
	            	
	            	sqlNameAbreviado += sep;
	            	
	            	name = "";
	            }
	            name += character;
	        }
	        
	       
	        String abreviacao = Abreviacoes.getInstance().abrevie(name.toUpperCase());
	        	
	        if(abreviacao != null || !StringUtils.isEmpty(abreviacao))
	        	sqlNameAbreviado += abreviacao;
	        else
	        	sqlNameAbreviado += name.toUpperCase();
	          	
	     
	        
	        return StringEscapeUtils.escapeSql(sqlNameAbreviado);
	    }
    
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
    
	public static String getSqlNameFromTaggedValue2(
	        String prefix,
	        ModelElementFacade element,
	        String name,
	        Short nameMaxLength,
	        Object separator)
	    {
	        return getSqlNameFromTaggedValue2(
	            prefix,
	            element,
	            name,
	            nameMaxLength,
	            null,
	            separator);
	    }
	    
	    public static String getSqlNameFromTaggedValueAbreviado(
	            String prefix,
	            ModelElementFacade element,
	            String name,
	            Short nameMaxLength,
	            Object separator,
	            Object relation)
	        {
	            return getSqlNameFromTaggedValueAbreviado(
	                prefix,
	                element,
	                name,
	                nameMaxLength,
	                null,
	                separator,
	                relation);
	        }
	    
	    
	    public static String getSqlNameFromTaggedValueAbreviado2(
	        String prefix,
	        ModelElementFacade element,
	        String name,
	        Short nameMaxLength,
	        Object separator,
	        Object relation)
	    {
	        return getSqlNameFromTaggedValueAbreviado2(
	            prefix,
	            element,
	            name,
	            nameMaxLength,
	            null,
	            separator,
	            relation);
	    }
	    
	    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
	    public static String getSqlNameFromTaggedValue2(
	            ModelElementFacade element,
	            String name,
	            Short nameMaxLength,
	            String suffix,
	            Object separator)
	        {
	            return getSqlNameFromTaggedValue2(
	                null,
	                element,
	                name,
	                nameMaxLength,
	                suffix,
	                separator);
	        }
	        
	        public static String getSqlNameFromTaggedValueAbreviado(
	                ModelElementFacade element,
	                String name,
	                Short nameMaxLength,
	                String suffix,
	                Object separator,
	                Object relation)
	            {
	                return getSqlNameFromTaggedValueAbreviado(
	                    null,
	                    element,
	                    name,
	                    nameMaxLength,
	                    suffix,
	                    separator,
	                    relation);
	            }
	        
	        public static String getSqlNameFromTaggedValueAbreviado2(
	            ModelElementFacade element,
	            String name,
	            Short nameMaxLength,
	            String suffix,
	            Object separator)
	        {
	            return getSqlNameFromTaggedValueAbreviado2(
	                null,
	                element,
	                name,
	                nameMaxLength,
	                suffix,
	                separator);
	        }
	        
	        //////////////////////////////////////////////////////////////////////////////////////////////////////
    
	        public static String getSqlNameFromTaggedValue2(
	                ModelElementFacade element,
	                String name,
	                Short nameMaxLength,
	                Object separator)
	            {
	                return getSqlNameFromTaggedValue2(
	                    null,
	                    element,
	                    name,
	                    nameMaxLength,
	                    null,
	                    separator);
	            }
	            
	            public static String getSqlNameFromTaggedValueAbreviado(
	                    ModelElementFacade element,
	                    String name,
	                    Short nameMaxLength,
	                    Object separator,
	                    Object relation)
	                {
	                    return getSqlNameFromTaggedValueAbreviado(
	                        null,
	                        element,
	                        name,
	                        nameMaxLength,
	                        null,
	                        separator,
	                        relation);
	                }
	            
	            public static String getSqlNameFromTaggedValueAbreviado2(
	                ModelElementFacade element,
	                String name,
	                Short nameMaxLength,
	                Object separator,
	                Object relation)
	            {
	                return getSqlNameFromTaggedValueAbreviado2(
	                    null,
	                    element,
	                    name,
	                    nameMaxLength,
	                    null,
	                    separator,
	                    relation);
	            }
	            
	            ///////////////////////////////////////////////////////////////////////////////////////////////////
    
	            public static String getSqlNameFromTaggedValue2(
	                    String prefix,
	                    ModelElementFacade element,
	                    String name,
	                    Short nameMaxLength,
	                    String suffix,
	                    Object separator)
	                {
	                	
	                	if (element != null)
	                    {
	                
	                		
	                        Object value = element.findTaggedValue(name);
	                        name = StringUtils.trimToEmpty((String)value);
	                        
	                        if (StringUtils.isEmpty(name))
	                        {
	                        	// if we can't find the tagValue then use the
	                            // element name for the name
	                        	
	                        	if(element instanceof CoppetecAssociationEndFacade) {
	            					name = ((CoppetecAssociationEndFacade)element).getNameWithoutPluralization();
	            				}else
	            					name = element.getName();
	                            
	                        	
	                        	
	                            name = toSqlName(
	                                    name,
	                                    separator);
	                            
	                        }
	                        
	                        
	                        if (StringUtils.isNotBlank(prefix))
	                        {
	                            name = StringUtils.trimToEmpty(prefix) + name;
	                        }
	                        
	                        if (StringUtils.isNotBlank(suffix))
	                        {
	                            name = name + StringUtils.trimToEmpty(suffix);
	                        }
	                        
	                        name = ensureMaximumNameLength(
	                                name,
	                                nameMaxLength);
	                    }
	                    return name;
	                }
	                
	                public static String getSqlNameFromTaggedValueAbreviado(
	                        String prefix,
	                        ModelElementFacade element,
	                        String name,
	                        Short nameMaxLength,
	                        String suffix,
	                        Object separator,
	                        Object relation)
	                    {
	                    	
	                    	if (element != null)
	                        {
	                    		
	                    		
	                            Object value = element.findTaggedValue(name);
	                            name = StringUtils.trimToEmpty((String)value);
	                            
	                            if (StringUtils.isEmpty(name))
	                            {
	                            	// if we can't find the tagValue then use the
	                                // element name for the name
	                            	
	                            
	                            	
	                            	if(element instanceof CoppetecAssociationEndFacade) {
	            						name = ((CoppetecAssociationEndFacade)element).getNameWithoutPluralization();
	            					}else
	            						name = element.getName();
	                                
	                            	
	                                name = toSqlNameAbreviado(
	                                        name,
	                                        separator, relation);
	                                
	                                if (StringUtils.isNotBlank(prefix))
	                                {
	                                    name = StringUtils.trimToEmpty(prefix) + name;
	                                }
	                                
	                                if (StringUtils.isNotBlank(suffix))
	                                {
	                                    name = name + StringUtils.trimToEmpty(suffix);
	                                }
	                             	
	                            }
	                            
	                            name = ensureMaximumNameLength(
	                                    name,
	                                    nameMaxLength);
	                        }
	                        return name;
	                    }

	                
	                public static String getSqlNameFromTaggedValueAbreviado2(
	                    String prefix,
	                    ModelElementFacade element,
	                    String name,
	                    Short nameMaxLength,
	                    String suffix,
	                    Object separator,
	                    Object relation)
	                {
	                	
	                	if (element != null)
	                    {
	                
	                		
	                        Object value = element.findTaggedValue(name);
	                        name = StringUtils.trimToEmpty((String)value);
	                        
	                        if (StringUtils.isEmpty(name))
	                        {
	                        	// if we can't find the tagValue then use the
	                            // element name for the name
	                        	
	                
	                        	
	                        	if(element instanceof CoppetecAssociationEndFacade) {
	            					name = ((CoppetecAssociationEndFacade)element).getNameWithoutPluralization();
	            				}else
	            					name = element.getName();
	                            
	                        	
	                            name = toSqlNameAbreviado(
	                                    name,
	                                    separator, relation);
	                            
	                        }
	                        
	                        
	                        if (StringUtils.isNotBlank(prefix))
	                        {
	                            name = StringUtils.trimToEmpty(prefix) + name;
	                        }
	                        
	                        if (StringUtils.isNotBlank(suffix))
	                        {
	                            name = name + StringUtils.trimToEmpty(suffix);
	                        }
	                        
	                        name = ensureMaximumNameLength(
	                                name,
	                                nameMaxLength);
	                    }
	                    return name;
	                }


    public static String ensureMaximumNameLength(
            String name,
            Short nameMaxLength)
        {
            if (StringUtils.isNotEmpty(name) && nameMaxLength != null)
            {
            	String truncatedName = name;
                short max = nameMaxLength.shortValue();
                if (name.length() > max)
                {
                    truncatedName = name.substring(
                            0,
                            max);
                    
                    name = truncatedName;
                }
                log.debug( "NOME: " + name + " / TRUNCADO: " + truncatedName );
            }
            return name;
        }

    //Usado para constraint
	public static String abrevieNomeSql( String nomeSql, String sqlNameSeparator ) {
		Abreviacoes abreviacoes = Abreviacoes.getInstance();
		int inicioAtual = 0;
		StringBuilder novaPalavra = new StringBuilder();
		for( int proximoUnderscore = nomeSql.indexOf( sqlNameSeparator ); proximoUnderscore != -1; proximoUnderscore = nomeSql.indexOf( sqlNameSeparator, proximoUnderscore + sqlNameSeparator.length() ) ) {
			String palavra = nomeSql.substring( inicioAtual, proximoUnderscore );
			String palavraAbreviada = abreviacoes.abrevie( palavra );
			
			if( inicioAtual > 0 ) novaPalavra.append( sqlNameSeparator );
			novaPalavra.append( palavraAbreviada );
			
			inicioAtual = proximoUnderscore + sqlNameSeparator.length();
		}
	
		if( inicioAtual > 0 ) novaPalavra.append( sqlNameSeparator );
		String palavra = nomeSql.substring( inicioAtual );
		String palavraAbreviada = abreviacoes.abrevie( palavra );
		novaPalavra.append( palavraAbreviada );
		
		return novaPalavra.toString();
	}

}
