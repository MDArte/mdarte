package org.andromda.cartridges.hibernate.metafacades;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.andromda.cartridges.hibernate.CriptoUtils;
import org.andromda.cartridges.hibernate.HibernateProfile;
import org.andromda.metafacades.uml.EntityAssociationEnd;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateAssociationEnd.
 *
 * @see org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateAssociationEnd
 */
public class CoppetecHibernateAssociationEndLogicImpl
    extends CoppetecHibernateAssociationEndLogic
{

    public CoppetecHibernateAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateAssociationEnd#getBatch()
     */
    protected int handleGetBatch()
    {
        String batchStr = (String)findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_ASSOCIATION_BATCH);
        int batch = 20;
        if(batchStr != null)
            batch =  new Integer(batchStr).intValue();
        return batch;
    }

    /**
     * @see org.andromda.cartridges.hibernate.metafacades.CoppetecHibernateAssociationEnd#getLazyType()
     */
    protected java.lang.String handleGetLazyType()
    {
        String lazyType = null;

        HibernateAssociationEnd otherEnd = null;

        HibernateAssociation association = (HibernateAssociation)this.getAssociation();

        Iterator ends = association.getAssociationEnds().iterator();

        while(ends.hasNext())
        {
            otherEnd = (HibernateAssociationEnd)ends.next();
            if(!(otherEnd == this))
            {
                break;
            }
        }

        lazyType = (String)otherEnd.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_LAZY_TYPE);

        if(!isLazy())
            return "";

        if(lazyType != null && isLazy())
            return lazyType;

        if(isOne2One() || isMany2One())
            return "proxy";

        if(isOne2Many() || isMany2Many())
            return "extra";

        return "";
    }

    protected boolean handleIsHibernateInverse()
    {
        // inverse can only be true if the relation is bidirectional
        boolean inverse = this.isNavigable() && this.getOtherEnd().isNavigable();

        if (inverse)
        {
            // Se existir tag informando o valor de inverse usa o valor da tag (somente para o caso Many2Many)
            if (this.isMany2Many())
            {
                HibernateAssociationEnd associationEndA = (HibernateAssociationEnd) this.getAssociation().getAssociationEnds().get(0);
                HibernateAssociationEnd associationEndB = (HibernateAssociationEnd) this.getAssociation().getAssociationEnds().get(1);

                String tagInverseA = (String) associationEndA.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INVERSE);
                String tagInverseB = (String) associationEndB.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INVERSE);

                // Verifica a existencia da tag em pelo menos um dos lados da associacao
                if ((tagInverseA != null) || (tagInverseB != null))
                {
                    String tagInverse = (String)this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INVERSE);

                    if (tagInverse != null)
                        return !(Boolean.parseBoolean(tagInverse));

                    if (tagInverseA != null)
                        return Boolean.parseBoolean(tagInverseA);

                    if (tagInverseB != null)
                        return Boolean.parseBoolean(tagInverseB);
                }
            }

            inverse = this.isMany2One();

            // for many-to-many we just put the flag on the side that
            // has the lexically longer fully qualified name for
            // it's type
            if (this.isMany2Many() && !inverse)
            {
                String endTypeName = StringUtils.trimToEmpty(this.getType().getFullyQualifiedName(true));
                String otherEndTypeName =
                    StringUtils.trimToEmpty(this.getOtherEnd().getType().getFullyQualifiedName(true));

                int compareTo = endTypeName.compareTo(otherEndTypeName);

                // if for some reason the fully qualified names are equal,
                // compare the names.
                if (compareTo == 0)
                {
                    String endName = StringUtils.trimToEmpty(this.getName());
                    String otherEndName = StringUtils.trimToEmpty(this.getOtherEnd().getName());
                    compareTo = endName.compareTo(otherEndName);
                }

                inverse = compareTo < 0;
            }
        }

        return inverse;
    }

    protected boolean handleIsOne2OnePrimary()
    {
        boolean primaryOne2One = super.isOne2One();
        HibernateAssociationEnd otherEnd = (HibernateAssociationEnd)this.getOtherEnd();

        if (primaryOne2One)
        {
            primaryOne2One = super.isAggregation() || this.isComposition();
        }

        // if the flag is false delegate to the super class
        if (!primaryOne2One)
        {
            primaryOne2One = super.isOne2One() && !otherEnd.isAggregation() && !otherEnd.isComposition();

            // Usa flag indicando inverse se existir
            String tagInverse = (String) this.findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INVERSE);
            String tagInverseOtherEnd = (String) this.getOtherEnd().findTaggedValue(HibernateProfile.TAGGEDVALUE_HIBERNATE_INVERSE);

            if (tagInverse != null)
                primaryOne2One = primaryOne2One && !Boolean.parseBoolean(tagInverse);

            if (tagInverseOtherEnd != null)
                primaryOne2One = primaryOne2One && Boolean.parseBoolean(tagInverseOtherEnd);
        }

        return primaryOne2One;
    }

	protected String handleGetAssociationIndex()
	{
		String retorno = null;

		// we take into consideration the maximum length allowed
		final String maxLengthString = (String) getConfiguredProperty(UMLMetafacadeProperties.MAX_SQL_NAME_LENGTH);
		final boolean useCode = Boolean.valueOf(String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.USE_CODE_CONSTRAINT))).booleanValue();

		if (useCode && maxLengthString != null && !maxLengthString.trim().equals(""))
		{
			int maxLength = Integer.parseInt(maxLengthString);
			String nomeIndice = ((EntityAssociationEnd)this.getOtherEnd()).getColumnName();
			retorno = "IND_";
			try
			{
				byte[] bytes = CriptoUtils.digest(nomeIndice.getBytes(), "MD5");
				retorno += CriptoUtils.byteArrayToHexString(bytes);

				if (retorno.length() > maxLength)
					retorno = retorno.substring(0, maxLength);
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
        }
		return retorno;
	}
}