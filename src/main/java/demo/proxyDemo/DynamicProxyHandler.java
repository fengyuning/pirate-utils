package demo.proxyDemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyHandler implements InvocationHandler {
    Object proxyObject;

    public DynamicProxyHandler(Object proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doBefore();
        Object invoke = method.invoke(proxyObject, args);
        doAfter();
        return invoke;
    }

    public void doBefore() {
        System.out.println("动态代理处理之前");
    }

    public void doAfter() {
        System.out.println("动态代理处理之后");
    }

    //Client
    public static void main(String[] args) {
        //要代理的类
        Calculator calculator = new CalculatorImpl();
        //加入动态带咯
        ClassLoader classLoader = calculator.getClass().getClassLoader();
        Class<?>[] interfaces = calculator.getClass().getInterfaces();
        DynamicProxyHandler dynamicProxyHandler = new DynamicProxyHandler(calculator);
        //生成动态代理对象
        Calculator proxyInstance =
                (Calculator) Proxy.newProxyInstance(classLoader, interfaces, dynamicProxyHandler);
        proxyInstance.add(1,2);
    }

}

