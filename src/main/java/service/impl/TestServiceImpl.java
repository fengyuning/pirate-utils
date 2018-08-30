package service.impl;

import service.TestService;

public class TestServiceImpl implements TestService {
    @Override
    public void sayHi() {
        System.out.println("你好");
    }
}
