package util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyUtils {
	private ProxyUtils() {
	}

	public static ITest createProxy(InvocationHandler handler) {
		return (ITest) Proxy.newProxyInstance(ITest.class.getClassLoader(), new Class<?>[] {ITest.class}, handler);
	}
}
