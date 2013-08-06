package org.andromda.metafacades.uml14;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.andromda.CriptoUtils;
import org.andromda.metafacades.uml.Abreviacoes;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.CoppetecEntityMetafacadeUtils;
import org.andromda.metafacades.uml.CoppetecUMLMetafacadeProperties;
import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * MetafacadeLogic implementation for org.andromda.metafacades.uml.CoppetecEntityAssociationEnd.
 *
 * @see org.andromda.metafacades.uml.CoppetecEntityAssociationEnd
 */
public class CoppetecEntityAssociationEndLogicImpl
    extends CoppetecEntityAssociationEndLogic
{
	/**
	 * O logger desta classe.
	 */
	private static final Log log = LogFactory.getLog( CoppetecEntityAssociationEndLogicImpl.class );

    public CoppetecEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
        
        String caminhoDoArquivoDeAbreviacoes = getConfiguredProperty(CoppetecUMLMetafacadeProperties.CAMINHO_DO_ARQUIVO_DE_ABREVIACOES).toString();
        Abreviacoes.getInstance().setCaminhoDoArquivoDePropriedades( new File( caminhoDoArquivoDeAbreviacoes ) );
    }

    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyPrefix()
     */
    public String handleGetForeignKeyPrefix()
    {
        return (String)this.getConfiguredProperty( CoppetecUMLMetafacadeProperties.FOREIGN_KEY_PREFIX );
    }
    
    /**
     * @see org.andromda.metafacades.uml.EntityAssociationEnd#getForeignKeyColumnName()
     */
    public String handleGetColumnName()
    {
        String foreignKeyColumnName = null;
        // prevent ClassCastException if the association isn't an Entity
        if (this.getType() instanceof Entity)
        {
        	String columnTag = StringUtils.trimToEmpty((String)this.findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN));
        	String tableTag = StringUtils.trimToEmpty((String)this.getType().findTaggedValue(UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE));
        	
        	if(!StringUtils.isEmpty(columnTag) || StringUtils.isEmpty(tableTag) || (this.getName() != null && !StringUtils.isEmpty(this.getName())  && !StringUtils.isEmpty(tableTag) && !this.getName().toUpperCase().equals(this.getType().getName().toUpperCase()))){
        		foreignKeyColumnName =	
        		    CoppetecEntityMetafacadeUtils.getSqlNameFromTaggedValueAbreviado(this.getForeignKeyPrefix(),this,UMLProfile.TAGGEDVALUE_PERSISTENCE_COLUMN,	
                        ((Entity)this.getType()).getMaxSqlNameLength(), this.getForeignKeySuffix(),
                        this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
                        this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR));
        	}
        	else{
        		foreignKeyColumnName =
                    CoppetecEntityMetafacadeUtils.getSqlNameFromTaggedValueAbreviado2(this.getForeignKeyPrefix(),this.getType(),UMLProfile.TAGGEDVALUE_PERSISTENCE_TABLE,	
                        ((Entity)this.getType()).getMaxSqlNameLength(), this.getForeignKeySuffix(),
                        this.getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR),
                        this.getConfiguredProperty(UMLMetafacadeProperties.RELATION_NAME_SEPARATOR));
        	}
        }
        
        return foreignKeyColumnName;
    }

    
	private String getSuperTableName(Entity entity){
		Entity superClasse = (Entity)entity.getGeneralization();
		if(superClasse==null) return entity.getTableName();
		while(true){
			if((Entity)superClasse.getGeneralization()!=null){
				superClasse = (Entity)superClasse.getGeneralization();
			}else{
				break;
			}
		}
		String heranca = this.getInheritance(superClasse);
		if(heranca!=null && heranca.equals("class")){
			return superClasse.getTableName();
		}
		return entity.getTableName();
	}
	private String getInheritance(Entity entity)
    {
        String inheritance = null;

        if (entity != null)
        {
            Object value = entity.findTaggedValue("@andromda.hibernate.inheritance");

            if (value != null)
            {
                inheritance = String.valueOf(value);
            }
        }

        return inheritance;
    }
    /**
     * @see AssociationEndFacadeLogic#getForeignKeyConstraintName()
     */
    protected String handleGetForeignKeyConstraintName()
    {
        String constraintName = null;

        final Object taggedValueObject = findTaggedValue(
                UMLProfile.TAGGEDVALUE_PERSISTENCE_FOREIGN_KEY_CONSTRAINT_NAME);
        if (taggedValueObject == null)
        {
            // we construct our own foreign key constraint name here
            StringBuffer buffer = new StringBuffer();

            String constraintPrefix = getConfiguredProperty( CoppetecUMLMetafacadeProperties.CONSTRAINT_PREFIX ).toString();
            
            Object sqlNameSeparator = getConfiguredProperty( UMLMetafacadeProperties.SQL_NAME_SEPARATOR );
			if( !StringUtils.isEmpty( constraintPrefix ) ) {
	            buffer.append( constraintPrefix );
	            buffer.append( sqlNameSeparator );
            }

            final ClassifierFacade type = getOtherEnd().getType();
            if (type instanceof Entity)
            {
                Entity entity = (Entity)type;
                String entityTableName = this.getSuperTableName(entity);
                buffer.append( entityTableName );
            }
            else
            {
                // should not happen
                buffer.append(type.getName().toUpperCase());
            }

            buffer.append( sqlNameSeparator );
            buffer.append( this.getName().toUpperCase() );

            String constraintSuffix = getConfiguredProperty( CoppetecUMLMetafacadeProperties.CONSTRAINT_SUFFIX ).toString();

            if( !StringUtils.isEmpty( constraintSuffix ) ) {
	            buffer.append( sqlNameSeparator );
	            buffer.append( constraintSuffix );
            }
            
            constraintName = buffer.toString();
        }
        else
        {
            // use the tagged value
            constraintName = taggedValueObject.toString();
        }

        constraintName = CoppetecEntityMetafacadeUtils.abrevieNomeSql(constraintName, getConfiguredProperty(UMLMetafacadeProperties.SQL_NAME_SEPARATOR ).toString());

        // we take into consideration the maximum length allowed
        final String maxLengthString = (String) getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
        final boolean useCode = Boolean.valueOf(String.valueOf(getConfiguredProperty(UMLMetafacadeProperties.USE_CODE_CONSTRAINT))).booleanValue();

        if (useCode && maxLengthString != null && !maxLengthString.equals(""))
        {
        	int tamanho= Integer.parseInt(maxLengthString);

        	String retorno = "FKC_";

			try
			{
				byte[] bytes = CriptoUtils.digest(constraintName.getBytes(), "MD5");

				retorno += CriptoUtils.byteArrayToHexString(bytes);

				if (retorno.length() > tamanho)
					constraintName = retorno.substring(0, tamanho);
				else
					constraintName = retorno;
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
        }

        return constraintName;
    }
}
