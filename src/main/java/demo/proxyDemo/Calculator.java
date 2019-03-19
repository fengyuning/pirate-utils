package demo.proxyDemo;

/**
 * 静态代理,最大的问题就是要为每个代理类写一个具体的代理类，
 * 当很多类的代理逻辑一样时，会很麻烦
 */
public interface Calculator {

    int add(int a, int b);
}

/**
 * 一个具体实现类
 */
class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}

/**
 * 代理类
 */
class CalculatorProxy implements Calculator {
    private Calculator calculator;

    public CalculatorProxy(Calculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public int add(int a, int b) {
        System.out.println("实际计算之前");
        int add = calculator.add(a, b);
        System.out.println("实际计算之后");
        return add;
    }

    public static void main(String[] args) {
        Calculator proxy = new CalculatorProxy(new CalculatorImpl());
        System.out.println(proxy.add(1, 1));
    }
}
