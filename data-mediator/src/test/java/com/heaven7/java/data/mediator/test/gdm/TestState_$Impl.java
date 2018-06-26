package com.heaven7.java.data.mediator.test.gdm;

import com.heaven7.java.base.util.Objects;
import com.heaven7.java.data.mediator.DataPools;

public class TestState_$Impl implements TestState, DataPools.Poolable {
  private int state;

  public TestState_$Impl() {
  }

  @Override
  public void recycle() {
    DataPools.recycle(this);
  }

  @Override
  public void clearProperties() {
    this.state = 0;
  }

  @Override
  public String toString() {
    Objects.ToStringHelper helper = Objects.toStringHelper(this)
        .add("state", String.valueOf(this.state));
    return helper.toString();
  }

  public int getState() {
    return state;
  }

  public TestState setState(int state1) {
    this.state = state1;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TestState_$Impl)) {
      return false;
    }
     TestState_$Impl that = (TestState_$Impl) o;
    if (getState() != that.getState()) {
      return false;
    }
    return true;
  }
}
