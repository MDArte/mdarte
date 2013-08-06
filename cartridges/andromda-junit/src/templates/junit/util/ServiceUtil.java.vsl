package util;

import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceUtil {
	private static Hashtable getInitialContext(String login, String senha,
			String projeto) throws NamingException {

		Hashtable environment = new Hashtable();

		environment.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.security.jndi.JndiLoginInitialContextFactory");
		environment.put(Context.URL_PKG_PREFIXES,
				"org.jboss.naming:org.jnp.interfaces");
		environment.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		environment.put(Context.SECURITY_PRINCIPAL, login + "@" + projeto);
		environment.put(Context.SECURITY_CREDENTIALS, senha);

		return environment;
	}

	private static Object lookupHome(Hashtable environment, String jndiName,
			Class narrowTo) throws javax.naming.NamingException {
		InitialContext initialContext = new javax.naming.InitialContext(
				environment);
		try {
			Object object = initialContext.lookup(jndiName);
			// only narrow if necessary
			if (narrowTo.isInstance(java.rmi.Remote.class)) {
				object = javax.rmi.PortableRemoteObject
						.narrow(object, narrowTo);
			}
			return object;
		} finally {
			initialContext.close();
		}
	}

	public static Object getService(String login, String senha, String projeto,
			String jndiName, Class narrowTo) {
		try {
			Object home = lookupHome(getInitialContext(login, senha, projeto),
					jndiName, narrowTo);
			Class cls = home.getClass();
			Class[] varargs = null;
			Method meth = cls.getMethod("create", varargs);
			return meth.invoke(home, varargs);
		} catch (Exception ex) {
			throw new javax.ejb.EJBException(ex);
		}
	}
}
