package org.ofbiz.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.ofbiz.rest.seller.product.ProductList;

// com.ilinks.restful

public class OFBizApplication extends Application {
  @Override
  public Set<Class<?>> getClasses() {
      Set<Class<?>> classes = new HashSet<Class<?>>();
      classes.add(InitSetting.class);
      classes.add(ProductList.class);
      return classes;
  }
}