package com.heaven7.java.data.mediator.test.gdm;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.data.mediator.BaseMediator;
import com.heaven7.java.data.mediator.PropertyInterceptor;

public class TestState_$Proxy extends BaseMediator<TestState> implements TestState {
  public TestState_$Proxy(TestState base) {
    super(base);
  }

  public int getState() {
    return _getTarget().getState();
  }

  public TestState setState(int state1) {
    TestState target = _getTarget();
    int oldValue = target.getState();
    if(_getEqualsComparator().isEquals(oldValue, state1)) {
      return this;
    }
    target.setState(state1);
    dispatchValueChanged(PROP_state, oldValue, state1);
    return this;
  }

  @Override
  public void applyProperties(PropertyInterceptor interceptor) {
    Throwables.checkNull(interceptor);
    startBatchApply(interceptor)
      .addProperty(PROP_state, getState())
    .apply();
  }

  @Override
  public String toString() {
    return _getTarget().toString();
  }

  @Override
  public void clearProperties() {
    throw new UnsupportedOperationException("proxy class can't call this method.");
  }

  @Override
  public void recycle() {
    throw new UnsupportedOperationException("proxy class can't call this method.");
  }
}
