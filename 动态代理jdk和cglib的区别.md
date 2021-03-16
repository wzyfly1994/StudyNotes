# 动态代理jdk和cglib的区别

动态代理的描述在前两篇文章已经做了一部分描述[动态代理的详细解读](https://www.jianshu.com/p/d332a0684c02)和[动态代理的简单描述](https://www.jianshu.com/p/d7835ab742e7)，**JDK的动态代理只能针对实现了接口的类生成代理。而cglib的动态代理是针对类实现代理，这两种代理我们可以灵活使用。**

**一.JDK动态代理**

> **Car接口**

```java
package proxy;

public interface Car {

    public void run();
}
```

> **Car实现类**

```java
package proxy;

public class CarImpl implements Car{

    public void run() {
        System.out.println("car running");
    }

}
```

> **Car代理类**

```java
package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
//JDK动态代理代理类 
public class CarHandler implements InvocationHandler{
    //真实类的对象
    private Object car;
    //构造方法赋值给真实的类
    public CarHandler(Object obj){
        this.car = obj;
    }
//代理类执行方法时，调用的是这个方法
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object res = method.invoke(car, args);
        System.out.println("after");
        return res;
    }
}
```

> **main方法**

```java
package proxy;

import java.lang.reflect.Proxy;

public class main {

    public static void main(String[] args) {
        CarImpl carImpl = new CarImpl();
        CarHandler carHandler = new CarHandler(carImpl);
        Car proxy = (Car)Proxy.newProxyInstance(
                main.class.getClassLoader(), //第一个参数，获取ClassLoader
                carImpl.getClass().getInterfaces(), //第二个参数，获取被代理类的接口
                carHandler);//第三个参数，一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上
        proxy.run();
    }
}
```

> **运行结果**

```undefined
before
car running
after
```

> 通过上面的例子三个参数我们可以看到，**JDK的动态代理依靠接口实现，入参必须有被代理类的接口**，也就是`carImpl.getClass().getInterfaces()`,如果有些类并没有实现接口，则不能使用JDK代理，**这就要使用cglib动态代理了。**

**二.Cglib动态代理**

> **没有实现接口的Car**

```csharp
package proxy;

public class CarNoInterface {

    public void run() {
        System.out.println("car running");
    }
}
```

> **cglib代理类**

```kotlin
package proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibProxy implements MethodInterceptor{

    private Object car;
    
    /** 
     * 创建代理对象 
     *  
     * @param target 
     * @return 
     */  
    public Object getInstance(Object object) {  
        this.car = object;  
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(this.car.getClass());  
        // 回调方法  
        enhancer.setCallback(this);  
        // 创建代理对象  
        return enhancer.create();  
    }  
    
    @Override
    public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
        System.out.println("事物开始");  
        proxy.invokeSuper(obj, args);  
        System.out.println("事物结束");  
        return null;  
    }

}
```

> **main方法**

```java
package proxy;

import java.lang.reflect.Proxy;

public class main {

    public static void main(String[] args) {    
        CglibProxy cglibProxy = new CglibProxy();
        CarNoInterface carNoInterface = (CarNoInterface)cglibProxy.getInstance(new CarNoInterface());
        carNoInterface.run();
    }
}
```

> **结果输出**

```undefined
事物开始
car running
事物结束
```

上面两个例子我们已经看到这两种动态代理各自的分工了，在实际开发中，可以根据需求来灵活安排使用接口来代理还是类来代理了。