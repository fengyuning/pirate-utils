package demo;

public class Singleton {
//    private Singleton() {
//    }
//
//    private static Singleton singleton;
//
//    public static Singleton getInstance() {
//        if (singleton == null) {
//            //如果为null,保证线程安全的创建
//            synchronized (Singleton.class) {
//                //上面只是保证了为null时,创建为串行的，需要在判断一次
//                if (singleton == null) {
//                    singleton = new Singleton();
//                }
//            }
//        }
//        return singleton;
//    }

    private Singleton() {
    }

    private static class holder {
        public final static Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {

        return holder.instance;
    }
}
