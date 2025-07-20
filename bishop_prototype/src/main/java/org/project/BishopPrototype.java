package org.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(basePackages = {"org.project"})
public class BishopPrototype {
  public static void main(String[] args) {
    SpringApplication.run(BishopPrototype.class, args);
  }
}
